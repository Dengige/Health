package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预约设置业务层接口
 */
public interface OrderSettingService {
    /**
     * 批量导入预约设置信息
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList);

    /**
     * 根据年月 查询当月预约设置信息
     * @param date
     * @return
     */
    List<Map> getOrderSettingByMonth(String date);
    /**
     *  根据用户输入的预约人数进行预约设置
     */
    void editNumberByDate(OrderSetting orderSetting);
}
