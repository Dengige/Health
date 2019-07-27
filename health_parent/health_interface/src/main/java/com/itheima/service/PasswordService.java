package com.itheima.service;

import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.Set;

/**
 * 用户服务接口
 */
public interface PasswordService {
    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 修改密码
     * @param user
     */
    void update(User user);
}
