package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 业务服务层 检查组管理
 */
@Service(interfaceClass=CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void add(Integer[] checkitemIds,CheckGroup checkGroup) {
        //1.保存检查组
        checkGroupDao.add(checkGroup);
        //获取检查组id
        Integer groupId = checkGroup.getId();
        //2.保存检查组和检查项中间表  检查项id 检查组id(保存检查组后 select LAST_INSERT_ID())
        setCheckGroupAndCheckItem(groupId,checkitemIds);
     }

    /**
     * 保存检查组和检查项中间表：
     * 为了更新检查组的时候 能代码公用 提取一个方法
     */
    public void setCheckGroupAndCheckItem(Integer groupId,Integer[] checkitemIds){
        if(checkitemIds != null && checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                //将两个参数放入map，为后续配置文件使用map方便
                Map<String,Integer> map = new HashMap<>();
                map.put("groupId",groupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    /**
     * 检查组分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //PageHelper分页查询 PageHelper设置分页参数 紧跟着的第二行代码一定要进行分页的语句代码
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> checkGroupPage = checkGroupDao.selectByCondition(queryString);
        return new PageResult(checkGroupPage.getTotal(),checkGroupPage.getResult());
    }
    /**
     * 根据检查组id 查询检查项对象
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }
    /**
     * 根据检查组id查询所有关联的检查项ids
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }
    /**
     * 编辑检查组
     */
    @Override
    public void edit(Integer[] checkitemIds, CheckGroup checkGroup) {
        // 1.根据检查组id更新检查组表
        checkGroupDao.edit(checkGroup);
        // 2.根据检查组id删除中间表已经关联的数据
        Integer groupId = checkGroup.getId();
        checkGroupDao.deleteAssociation(groupId);
        // 3.重新建立检查组和检查项的关联关系
        setCheckGroupAndCheckItem(groupId,checkitemIds);
    }

    /**
     * 查询所有检查组信息
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

}
