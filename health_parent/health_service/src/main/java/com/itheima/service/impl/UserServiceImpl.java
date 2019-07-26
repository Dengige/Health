package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户服务实现类
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }
    /**
     * 根据用户id查询角色（角色中包含权限信息）
     * @param userId
     * @return
     */
    @Override
    public Set<Role> findRoleByUserId(Integer userId) {
        //根据用户id查询角色数据
        Set<Role> roleSet =roleDao.findRoleByUserId(userId);
        //根据角色查询权限信息
        if(roleSet!=null && roleSet.size()>0){
            for (Role role : roleSet) {
                Integer roleId = role.getId();//角色id
                //根据角色id查询权限信息
                Set<Permission> permissionSet = permissionDao.findPermissionByRoleId(roleId);
                role.setPermissions(permissionSet);//将权限跟角色关联
            }
        }
        return roleSet;
    }

}
