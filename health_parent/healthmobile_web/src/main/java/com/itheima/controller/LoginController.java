package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
/**
 * 快速登录
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberSerivce memberSerivce;

    /**
     * 快速登录方法
     */
    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public Result login(@RequestBody Map map, HttpServletResponse response){
        String telephone = (String)map.get("telephone");
        String validateCode =(String)map.get("validateCode");
        //从redis中获取验证码 从页面获取验证码
        String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone);
        //1.验证码校验
        if(StringUtils.isEmpty(validateCode) || StringUtils.isEmpty(redisCode) || !validateCode.equals(redisCode)){
            //返回验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //2.根据手机号码 查询会员表 看是否是会员
        Member member = memberSerivce.findByTelephone(telephone);
        //3.不是会员，自动注册
        if(member == null){
            member = new Member();
            member.setPhoneNumber(telephone);//手机号码
            member.setRegTime(new Date());//当前时间
            memberSerivce.add(member);//自动注册会员
        }
        //4.登录成功把用户信息存入cookie
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*30);//一个月有效
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
