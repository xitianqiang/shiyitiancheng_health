package com.shiyitiancheng.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.shiyitiancheng.constant.MessageConstant;
import com.shiyitiancheng.constant.RedisMessageConstant;
import com.shiyitiancheng.entity.Result;
import com.shiyitiancheng.pojo.Member;
import com.shiyitiancheng.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map){
        String telephone = (String) map.get("telephone");
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        String validateCode = (String) map.get("validateCode");
        if(validateCode!=null && validateCodeInRedis!=null && validateCodeInRedis.equals(validateCode)){
            Member member = memberService.login(telephone);
            if (member == null){
                //不是会员,自动完成注册
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);//有效期30天
            response.addCookie(cookie);
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);

            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }else{
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
