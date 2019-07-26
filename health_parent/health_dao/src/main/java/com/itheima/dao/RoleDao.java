package com.itheima.dao;

import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.Set;

/**
 * 角色dao层接口
 */
public interface RoleDao {

    Set<Role> findRoleByUserId(Integer userId);
}
