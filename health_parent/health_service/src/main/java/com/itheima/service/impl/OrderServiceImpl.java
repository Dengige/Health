package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约体检业务处理类
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    //预约设置dao
    @Autowired
    private OrderSettingDao orderSettingDao;

    //会员dao
    @Autowired
    private MemberDao memberDao;

    //提交预约dao
    @Autowired
    private OrderDao orderDao;

    /**
     * 提交预约
     *
     * @param map
     * @return
     */
    @Override
    public Result submitOrder(Map map) throws Exception {
        //获取预约方式
        String orderType = (String)map.get("orderType");
        //获取身份证
        String idCard = (String) map.get("idCard");
        //获取性别
        String sex = (String) map.get("sex");
        //获取姓名
        String name = (String) map.get("name");
        //套餐id
        String setmealId = (String) map.get("setmealId");
        //手机号码
        String telephone = (String) map.get("telephone");
        // 1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约
        String orderDate = (String) map.get("orderDate");
        //将string转date
        Date newOrderDate = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(newOrderDate);
        System.out.println(orderSetting);

        // 2. 判断当前日期是否预约已满(判断reservations是否==number)
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        int reservations = orderSetting.getReservations();///已经预约人数
        int number = orderSetting.getNumber();///可预约人数
        //大于 当预约满了返回错误信息
        if (reservations >= number) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3. 判断是否是会员(根据手机号码查询t_member)
        Member dbMember = memberDao.findByTelephone(telephone);
        //    - 如果是会员(能够查询出来),
        if (dbMember != null) {
            // 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
            Order queryOrder = new Order(dbMember.getId(), newOrderDate, Integer.parseInt(setmealId));
            List<Order> listOrder = orderDao.findByCondition(queryOrder);
            if (listOrder.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        //  - 如果不是会员(不能够查询出来),自动注册为会员(直接向t_member插入一条记录)
        if (dbMember == null) {
            dbMember = new Member();
            dbMember.setName(name);
            dbMember.setSex(sex);
            dbMember.setIdCard(idCard);
            dbMember.setPhoneNumber(telephone);
            dbMember.setRegTime(new Date());
            memberDao.add(dbMember);
        }

        // 4. 进行预约
        //   - 向t_order表插入一条记录
        //   - t_ordersetting表里面预约的人数+1
        Integer dbMemberId = dbMember.getId(); //会员id 预约表中需要的记录信息
        Order order = new Order();
        order.setMemberId(dbMemberId);//会员id
        order.setOrderDate(newOrderDate);//预约日期
        order.setOrderType(orderType);//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//默认状态为未到诊
        order.setSetmealId(Integer.parseInt(setmealId));
        orderDao.add(order);
        System.out.println("预约成功。。。。。。");

        //预约成功后 预约设置表 已经预约人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);//+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //返回预约成功页面需要的预约表中主键id
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 根据预约id查询预约详细信息
     * @param id
     * @return
     */
    @Override
    public Map findById4Detail(Integer id) {
        return orderDao.findById4Detail(id);
    }
}
