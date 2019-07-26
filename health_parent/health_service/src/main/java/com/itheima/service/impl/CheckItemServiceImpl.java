package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务服务层 检查项管理
 */
@Service(interfaceClass=CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 检查项分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //PageHelper分页查询 PageHelper设置分页参数 紧跟着的第二行代码一定要进行分页的语句代码
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);
        return new PageResult(checkItemPage.getTotal(),checkItemPage.getResult());
    }

    /**
     * 删除检查项
     * @param checkItemId
     */
    @Override
    public void deleteById(Integer checkItemId) {
        //1.先查询中间表t_checkgroup_checkitem 是否关联检查组
        int count = checkItemDao.findCountByCheckItemId(checkItemId);
        //2.如果已经关联检查组 抛出异常，返回页面
        if(count > 0){
            throw  new RuntimeException("已经关联检查项，无法删除");
        }
        //3.如果未关联检查组，直接删除
        checkItemDao.deleteById(checkItemId);
    }

    /**
     * 根据id查询检查项对象  findById
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 根据id修改检查项对象
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    /**
     * 查询所有检查项数据
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
