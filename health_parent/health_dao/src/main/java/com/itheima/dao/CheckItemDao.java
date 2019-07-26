package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * dao持久层 检查项管理接口
 */
public interface CheckItemDao {
    /**
     * 新建检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项分页 @Param("abc") 需要跟配置文件的 条件要一致 ，默认值字符串是value属性
     * @param queryString
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 先查询中间表t_checkgroup_checkitem 是否关联检查组
     * @param checkItemId
     * @return
     */
    int findCountByCheckItemId(Integer checkItemId);

    /**
     * 如果未关联检查组，直接删除
     * @param checkItemId
     */
    void deleteById(Integer checkItemId);

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
