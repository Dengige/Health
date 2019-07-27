package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PasswordDao;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.PasswordService;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户服务实现类
 */
@Service(interfaceClass = PasswordService.class)
@Transactional
public class PasswordServiceImpl implements PasswordService {


    @Autowired
    private PasswordDao passwordDao;

    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        return passwordDao.findUserByUsername(username);
    }


    /**
     * 修改密码
     * @param user
     */
    @Override
    public void update(User user) {
        passwordDao.update(user);
    }

}
