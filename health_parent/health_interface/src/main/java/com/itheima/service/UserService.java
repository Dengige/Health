package com.itheima.service;

import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.Set;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 根据用户id查询角色（角色中包含权限信息）
     * @param id
     * @return
     */
    Set<Role> findRoleByUserId(Integer id);
}
