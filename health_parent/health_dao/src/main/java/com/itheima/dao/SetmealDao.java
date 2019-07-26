package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐持久层接口
 */
public interface SetmealDao {
    /**
     * 保存套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 套餐和检查组中间表
     * @param map
     */
    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> selectByCondition(String queryString);
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

    /**
     * 运营数据统计热门套餐数据查询
     * @return
     */
    List<Map> hotSetmeal();
}
