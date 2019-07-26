package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 接口服务层 检查组接口管理
 */
public interface CheckGroupService {
    /**
     * 新增检查项
     * @param checkGroup
     */
    void add(Integer[] checkitemIds,CheckGroup checkGroup);

    /**
     * 检查组分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据检查组id 查询检查项对象
     * @param id
     * @return
     */
    CheckGroup findById(Integer id);
    /**
     * 根据检查组id查询所有关联的检查项ids
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
    /**
     * 编辑检查组
     */
    void edit(Integer[] checkitemIds, CheckGroup checkGroup);

    /**
     * 查询所有检查组信息
     * @return
     */
    List<CheckGroup> findAll();
}
