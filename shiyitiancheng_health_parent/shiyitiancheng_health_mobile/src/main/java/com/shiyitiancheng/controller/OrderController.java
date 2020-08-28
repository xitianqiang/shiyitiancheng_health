package com.shiyitiancheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.shiyitiancheng.constant.MessageConstant;
import com.shiyitiancheng.constant.RedisMessageConstant;
import com.shiyitiancheng.entity.Result;
import com.shiyitiancheng.pojo.Order;
import com.shiyitiancheng.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

//    在线体检预约
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        String telephone = (String) map.get("telephone");

//        从Redis中获取保存的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        Object validateCode =(String) map.get("validateCode");

//        将用户输入的验证码和Redis中保存的验证码预约业务处理
        if (validateCodeInRedis != null && validateCode != null && validateCode.equals(validateCodeInRedis) ){
            //        如果比对成功,调用服务完成预约业务处理
            map.put("orderType", Order.ORDERTYPE_WEIXIN);//设置预约类型,分为微信预约,电话预约
            Result result = null;
            try{
                result = orderService.order(map);
                return result;
            }catch(Exception e){
                e.printStackTrace();
                return result;
            }
//            if (result.isFlag()){
//                try {
//                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,(String) map.get("orderDate"));
//                } catch (ClientException e) {
//                    e.printStackTrace();
//                    return result;
//                }
//            }

        }else{
            //        如果比对不成功,返回结果给页面
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Map order = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,order);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }
}
