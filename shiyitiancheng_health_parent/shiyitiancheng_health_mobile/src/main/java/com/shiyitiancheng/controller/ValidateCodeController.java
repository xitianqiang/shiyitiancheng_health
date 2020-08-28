package com.shiyitiancheng.controller;

import com.shiyitiancheng.constant.MessageConstant;
import com.shiyitiancheng.constant.RedisMessageConstant;
import com.shiyitiancheng.entity.Result;
import com.shiyitiancheng.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;


//    预约的验证码
    @RequestMapping("/send4Order")
    public Result sendForOrder(String telephone){
//        随机生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        try{
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
            System.out.println(validateCode);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
//        保存到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //    用户登录的验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
//        随机生成4位数字验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        try{
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
            System.out.println(validateCode);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
//        保存到redis
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,300,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
