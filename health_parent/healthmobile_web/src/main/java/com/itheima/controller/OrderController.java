package com.itheima.controller;

/**
 * 体检预约控制层
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * 体检预约控制层
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 查询套餐列表
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Result submit(@RequestBody Map map) {
        //获取手机号码
        String telephone = (String) map.get("telephone");
        String orderDate = (String) map.get("orderDate");
        String validateCode = (String) map.get("validateCode");
        //获取redis中验证码和用户的输入验证码进行校验
        String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);
        if (StringUtils.isEmpty(redisCode) || StringUtils.isEmpty(validateCode) || !redisCode.equals(validateCode)) {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //调用业务层代码
        Result result = null;
        try {
            //后台服务和移动端都可以调用service服务 体检预约分为两种预约方式：电话预约 微信预约
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //预约成功需要发送通知短信
        if (result.isFlag()) {
            //调用短信接口发送成功通知 您已经成功预约传智健康体检，体检时间为2019-07-19
            try {
                System.out.println("您已经成功预约传智健康体检，体检时间为" + orderDate);
                //SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
                //删除redis中验证码
            } catch (Exception e) {
                e.printStackTrace();
                //当发送失败 需要记录到失败表中，后续通过其它程序循环处理这个表 直到短信发送成功为止。
            }
        }
        return result;
    }

    /**
     * 预约成功页面数据
     */
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public Result findById(Integer id) {
        Map map = orderService.findById4Detail(id);
        return new Result(false, MessageConstant.ORDER_SUCCESS,map);
    }
}
