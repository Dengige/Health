package com.itheima.dao;

import com.itheima.pojo.Permission;
import com.itheima.pojo.User;

import java.util.Set;

/**
 * 权限dao层接口
 */
public interface PermissionDao {

    Set<Permission> findPermissionByRoleId(Integer roleId);
}
