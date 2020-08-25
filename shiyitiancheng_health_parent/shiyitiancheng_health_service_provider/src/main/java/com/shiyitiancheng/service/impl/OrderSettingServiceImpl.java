package com.shiyitiancheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.shiyitiancheng.dao.OrderSettingDao;
import com.shiyitiancheng.pojo.OrderSetting;
import com.shiyitiancheng.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void addOrderSetting(List<OrderSetting> data) {
        if (data!=null && data.size()>0){
            for (OrderSetting orderSetting : data) {
                editNumberByDate(orderSetting);
                //判断当前日期是否已经进行了预约设置4
               /* OrderSetting orderSettingBack = orderSettingDao.findConutByOrderDate(orderSetting.getOrderDate());
                int oldData = 0;
                int newData = orderSetting.getNumber();
                if (orderSettingBack!=null){
                    oldData = orderSettingBack.getNumber();

                }
                if (oldData>0&&oldData!=newData){
//                    有预约设置，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else{
//                    没有进行预约设置，执行插入操作
                    orderSettingDao.addOrderSetting(orderSetting);
                }*/
            }
        }

    }

//    根据月份查询对应的预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) {//格式：yyyy-MM
        String begin = date + "-1";
        String end = date + "-31";
        Map<String,String> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        List<Map> list = orderSettingDao.getOrderSettingByMonth(map);
//        List<OrderSetting> orderSettings = orderSettingDao.getOrderSettingByMonth(map);
//        List<Map> list = new ArrayList<>();
//        if (orderSettings!=null && orderSettings.size()>0){
//
//            for (OrderSetting orderSetting : orderSettings) {
//                Map<String,Object> m = new HashMap<>();
//                m.put("date",orderSetting.getOrderDate().getDate());//获取日期数字
//                m.put("reservations",orderSetting.getReservations());
//                m.put("number",orderSetting.getNumber());
//                list.add(m);
//            }
//        }

        return list;
    }

    //根据日期设置对应的预约设置数据
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        Date orderDate = orderSetting.getOrderDate();
        //根据日期查询是否已经进行了预约设置
        OrderSetting orderSettingBack = orderSettingDao.findConutByOrderDate(orderDate);
        int oldData = 0;
        int newData = orderSetting.getNumber();
        if (orderSettingBack!=null){
            oldData = orderSettingBack.getNumber();

        }
        if (oldData>0&&oldData!=newData){
            //当前日期已经进行预约设置，需要进行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            //当前日期没有就进行预约设置，需要进行插入操作
            orderSettingDao.addOrderSetting(orderSetting);

        }



    }




}
