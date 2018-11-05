package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.User;

import com.itheima.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/*
    用户的注册
 */
@Controller
public class UserController {


    @Reference
    private UserService userService;

    @PostMapping("/user/doRegister")
    @ResponseBody
    public Map<String,String> register(User user){
        System.out.println("user=" + user);
        int result = userService.addUser(user);
        System.out.println("result=" + result);

        Map<String,String> map=new HashMap<>();
        if (result>0){
            map.put("status","200");
        }else {
            map.put("status","500");
        }

        return map;
    }

}
