package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    //userService在别的项目，需要在build.gradle里面添加
    @Reference
    private UserService userService;

    @RequestMapping("save")
    public String save(){
        System.out.println("调用了UserController的save()方法");
        userService.save();
        return "success啊";
    }
}
