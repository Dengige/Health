package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderSettingService;
import com.itheima.service.SetmealService;
import com.itheima.utils.POIUtils;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Map;
/**
 * 预约设置控制层
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 上传文件
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Result upload(MultipartFile excelFile){
        try {
            System.out.println("原始文件名"+excelFile.getOriginalFilename());
            //读取excel对象 返回集合
            List<String[]> list = POIUtils.readExcel(excelFile);
            //将List<String[]> 转 List<OrderSetting>
            if(list != null && list.size()>0){

                List<OrderSetting> orderSettingList = new ArrayList<>();
                for (String[] strs : list) {
                    //遍历每一行数据
                    OrderSetting orderSetting = new OrderSetting();//每一行数据
                    orderSetting.setOrderDate(new Date(strs[0]));//预约日期设置
                    orderSetting.setNumber(Integer.parseInt(strs[1]));//预约数量设置
                    orderSettingList.add(orderSetting);//将每一行数据放入list集合中
                }
                //调用service服务
                orderSettingService.add(orderSettingList);
            }
            //文件名一定要返回
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据年月 查询当月预约设置信息 返回List<map> { date: 1, number: 120, reservations: 1 }
     */
    @RequestMapping(value = "/getOrderSettingByMonth",method = RequestMethod.GET)
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> listMap = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,listMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }



    /**
     *  根据用户输入的预约人数进行预约设置
     */
    @RequestMapping(value = "/editNumberByDate",method = RequestMethod.POST)
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
