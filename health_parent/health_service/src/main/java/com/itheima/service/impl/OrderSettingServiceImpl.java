package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.OrderSettingDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderSettingService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 业务服务层 预约设置管理
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {


    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入预约设置信息
     *
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                // 保存预约设置信息
                // 1.根据预约日期查询是否已经预约设置
                //select count(*) from t_ordersetting where orderDate = '2019-07-16'
                //Date orderDate = orderSetting.getOrderDate();//预约日期
                orderSettingByOrderDate(orderSetting);
                /*int count = orderSettingDao.findCountByOrderDate(orderDate);
                if (count > 0) {
                    // 2.如果有预约设置则更新预约设置信息
                    //根据预约日期 来更新预约人数 update  t_ordersetting set number = xxx where orderDate = 'xxx'
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    // 3.如果没有预约设置则直接插入预约设置信息
                    orderSettingDao.add(orderSetting);
                }*/
            }
        }

    }

    /**
     * 根据年月 查询当月预约设置信息
     *
     * @param date 2019-07-01
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //select * from t_ordersetting where orderdate between '2019-07-01' and '2019-07-31'
        String startDate = date + "-01";//起始日期 2019-07-01
        String endDate = date + "-31";//结束日期 2019-07-31
        //用map形式来存放
        Map<String, String> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(map);//根据年月查询 预约数据
        List<Map> mapList = new ArrayList<>();
        //将List<OrderSetting> 转 list<map>返回
        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {
                //单个预约设置对象   { date: 1, number: 120, reservations: 1 }
                Map<String, Object> rsMap = new HashMap<>();
                rsMap.put("date", orderSetting.getOrderDate().getDate());//获取当天2019-07-16==>16
                rsMap.put("number", orderSetting.getNumber());//可预约人数
                rsMap.put("reservations", orderSetting.getReservations());//已经预约人数
                mapList.add(rsMap);
            }
        }
        return mapList;
    }

    /**
     * 根据用户输入的预约人数进行预约设置
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //1.根据预约日期查询预约信息
        orderSettingByOrderDate(orderSetting);
    }

    /**
     * 批量预约设置和单个预约设置方法抽取
     * @param orderSetting
     */
    private void orderSettingByOrderDate(OrderSetting orderSetting) {
        int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            // 2.如果有预约设置则更新预约设置信息
            //根据预约日期 来更新预约人数 update  t_ordersetting set number = xxx where orderDate = 'xxx'
            orderSettingDao.editNumberByOrderDate(orderSetting);
        } else {
            // 3.如果没有预约设置则直接插入预约设置信息
            orderSettingDao.add(orderSetting);
        }
    }


}
