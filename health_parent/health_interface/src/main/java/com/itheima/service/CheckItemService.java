package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 接口服务层 检查项接口管理
 */
public interface CheckItemService {
    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 删除检查项
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据id查询检查项对象  findById
     * @param id
     * @return
     */
    CheckItem findById(Integer id);

    /**
     * 根据id修改检查项对象
     * @param checkItem
     */
    void edit(CheckItem checkItem);
    /**
     * 查询所有检查项数据
     */
    List<CheckItem> findAll();
}
