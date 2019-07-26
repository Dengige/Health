package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 认证（根据用户名从数据库查询密码交给框架），密码是否正确是由框架进行对比的
 * 授权 ：根据用户查询角色和权限数据  授权给当前登录的用户
 *
 */
@Component
public class SecurityUserService implements UserDetailsService{

    //注入密码加密对象  为明文密码加密
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Reference
    private UserService userService;

    /**
     * 根据用户名加载用户对象
     * @param username 页面用户输入用户名 传入此方法中
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询用户对象（自己的定义用户对象）
        User user = userService.findUserByUsername(username);
        //2.用户对象不存在 return null
        if(user == null){
            return null;//验证失败
        }
        //3.进行授权 写死的，后续从数据库中查询
        String dbPassword = user.getPassword();
        List<GrantedAuthority> list = new ArrayList<>();
        //4.授权 根据用户id查询用户对象（角色信息 权限信息）
        Set<Role> roles = userService.findRoleByUserId(user.getId());
        if(roles != null && roles.size()>0){
            for (Role role : roles) {
                String keyword = role.getKeyword();//授权的权限关键字
                list.add(new SimpleGrantedAuthority(keyword));//授权角色权限
                Set<Permission> permissions = role.getPermissions();//获取所有的权限 粒度最细
                if(permissions != null && permissions.size()>0){
                    for (Permission permission : permissions) {
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));///授权单个权限 删除检查项 查询检查项
                    }
                }
            }
        }
       return new org.springframework.security.core.userdetails.User(username,dbPassword,list);
    }

}
