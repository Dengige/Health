package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map; /**
 * 预约体检业务接口
 */
public interface OrderService {
    /**
     * 体检预约提交
     * @param map
     * @return
     */
    Result submitOrder(Map map) throws Exception;

    /**
     * 根据预约id查询预约详细信息
     * @param id
     * @return
     */
    Map findById4Detail(Integer id);
}
