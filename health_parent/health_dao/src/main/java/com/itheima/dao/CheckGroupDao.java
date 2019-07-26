package com.itheima.dao;
import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * dao持久层 检查组管理接口
 */
public interface CheckGroupDao {
    /**
     * 保存检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 保存检查组和检查项中间表
     */
    void setCheckGroupAndCheckItem(Map<String,Integer> map);

    /**
     * 检查组分页
     * @param queryString
     * @return
     */
    Page<CheckGroup> selectByCondition(String queryString);
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
     * 根据检查组id更新检查组表
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 根据检查组id删除中间表已经关联的数据
     * @param groupId
     */
    void deleteAssociation(Integer groupId);
    /**
     * 查询所有检查组信息
     * @return
     */
    List<CheckGroup> findAll();
}
