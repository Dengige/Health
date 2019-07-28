package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.User;
import com.itheima.service.PasswordService;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 修改密码控制层
 */
@RestController
@RequestMapping("/password")
public class PasswordController {

    @Reference
    private PasswordService passwordService;

    @Reference
    private UserService userService;
    //注入密码加密对象  为明文密码加密
    @Autowired
    private BCryptPasswordEncoder encoder;

    @RequestMapping(value="/change",method = RequestMethod.POST)
    public Result update(@RequestBody Map map){
        try {
            //1,通过username查询user对象
            String username =(String) map.get("usernames");
            User user = passwordService.findUserByUsername(username);
            //return new Result(true, MessageConstant.GET_SETMEAL_LIST_PASSWORD_SUCCESS);
            //获取重置前密码
            String drpassword = (String)map.get("password");
            //获取重置后的密码
            String dbpassword = (String)map.get("dbpassword");
            //TODO:无法进行密码效验，密码不可逆
            //获取数据库中的user的密码
            String SqlPassword = user.getPassword();

            //2,先将重置前密码加密
            //比对密码
            boolean matches = encoder.matches(drpassword,user.getPassword());
            //如果为true
            if(!matches){
                //比对不成功成功，提醒修改密码失败
                return new Result(false, MessageConstant.GET_SETMEAL_LIST_PASSWORD_FATL);
            }
            String password = encoder.encode(dbpassword);
            //保存到数据库
            user.setPassword(password);
            passwordService.update(user);
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_PASSWORD_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.GET_SETMEAL_LIST_PASSWORD_FATL);
    }
}
