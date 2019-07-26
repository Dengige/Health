package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map; /**
 * 持久层 预约设置管理接口
 */
public interface OrderSettingDao {
    /**
     * 预约设置保存
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据日期来更新预约人数
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 根据预约日期查询是否已经设置预约
     * @param orderDate
     * @return
     */
    int findCountByOrderDate(Date orderDate);

    /**
     * 根据年月查询 预约数据
     * @param map
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    /**
     * 根据预约日期查询预约数据
     * @param newOrderDate
     * @return
     */
    OrderSetting findByOrderDate(Date newOrderDate);

    /**
     * 根据预约日期更新已经预约人数
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
