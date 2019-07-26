package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 接口服务层 套餐接口管理
 */
public interface SetmealService {

    /**
     * 新增套餐
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    /**
     * 查询套餐列表
     */
    List<Setmeal> findAll();

    /**
     * 查询套餐数据成功
     * @param id
     * @return
     */
    Setmeal findById(Integer id);

    /**
     * 查询套餐名称 以及套餐预约数量
     * @return
     */
    List<Map> setmealCount();
}
