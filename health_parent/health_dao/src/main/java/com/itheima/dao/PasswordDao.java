package com.itheima.dao;

import com.itheima.pojo.User;

/**
 * 用户dao层接口
 */
public interface PasswordDao {
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
