package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * 验证码发送
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;
    /**
     * 预约体检的验证码发送
     * @return
     */
    @RequestMapping(value = "/send4Order", method = RequestMethod.POST)
    public Result send4Order(String telephone) {
        try {
            //1.先从redis中查询 ，如果当前用户记录存在，提示用户5分钟内无法重复获取验证码

            //1.生成验证码
            Integer number = ValidateCodeUtils.generateValidateCode(4);
            System.out.println("手机号码：：："+telephone+"验证码：：："+number);
            //2.发送验证码
            //SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,number.toString());
            //存入resdi，后续提交预约的时候进行校验 key=001_1311222333 value time:5分钟过期 5*60
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone,5*60,number.toString());//
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 快速登录的验证码发送
     * @return
     */
    @RequestMapping(value = "/send4Login", method = RequestMethod.POST)
    public Result send4Login(String telephone) {
        //1.生成验证码
        Integer number = ValidateCodeUtils.generateValidateCode(4);
        //2.调用短信接口发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,number.toString());
            //3.将验证码存入redis
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone,5*60,number.toString());
            //4.返回结果给页面
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
