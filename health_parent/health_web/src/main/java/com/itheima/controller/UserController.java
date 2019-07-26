package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * 用户控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = "/getUsername",method = RequestMethod.GET)
    public Result getUsername(){
        //认证成功后用户信息 就放入springsecurity上下文(SecurityContextHolder)中
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();//getPrincipal():用户对象 getAuthentication():认证对象

        //String password = user.getPassword();//spring securtiy：不能获取密码 shiro:可以获取
        String username = user.getUsername();
        //Collection<GrantedAuthority> authorities = user.getAuthorities();/
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }
}
