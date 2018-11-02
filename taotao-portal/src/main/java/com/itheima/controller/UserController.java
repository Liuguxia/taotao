package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.User;

import com.itheima.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
    用户的注册
 */
@Controller
public class UserController {


    @Reference
    private UserService userService;

    @PostMapping("/user/doRegister")
    public String register(User user){
        System.out.println("user=" + user);
        int result = userService.addUser(user);
        System.out.println("result=" + result);

        return null;
    }

}
