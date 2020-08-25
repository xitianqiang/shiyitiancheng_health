package com.shiyitiancheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shiyitiancheng.constant.MessageConstant;
import com.shiyitiancheng.entity.Result;
import com.shiyitiancheng.pojo.Setmeal;
import com.shiyitiancheng.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    //查询所有
    @RequestMapping("/getSetmeal")
    public Result getAllSetmeal(){
        try {
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);

        }

    }

    //查询所有
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setmealService.findById(id);

            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);

        }

    }
}
