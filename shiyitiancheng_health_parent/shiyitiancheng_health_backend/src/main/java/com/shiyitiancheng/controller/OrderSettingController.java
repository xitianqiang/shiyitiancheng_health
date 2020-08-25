package com.shiyitiancheng.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.shiyitiancheng.constant.MessageConstant;
import com.shiyitiancheng.entity.Result;
import com.shiyitiancheng.pojo.OrderSetting;
import com.shiyitiancheng.service.OrderSettingService;
import com.shiyitiancheng.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try{
            List<String[]> list = POIUtils.readExcel(excelFile);//使用POI解析表格数据
            List<OrderSetting> data = new ArrayList<>();
            for (String[] strings : list) {
                String orderDate = strings[0];
                String number = strings[1];
                OrderSetting orderSetting = new OrderSetting(new Date(orderDate),Integer.parseInt(number));
                data.add(orderSetting);
//                通过dubbo远程调用
                orderSettingService.addOrderSetting(data);
            }
        }catch (Exception e){
            e.printStackTrace();
            //文件解析失败
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS) ;
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){//格式 yyyy-MM
        try{
            List<Map> orderSettingByMonth = orderSettingService.getOrderSettingByMonth(date);

            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,orderSettingByMonth);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try{
            orderSettingService.editNumberByDate(orderSetting);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);

    }

}
