package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.service.UserService;
import com.itheima.pojo.User;
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

    @RequestMapping("selectOne")
    public User selectOne(){
        System.out.println("调用了UserController的selectOne()方法");
        User user = userService.selectOne(7L);
        return user;
    }

    //分页功能
    @RequestMapping("findByPage")
    public PageInfo<User> findByPage(int currentPage, int pageSize){
        System.out.println("调用了UserController的findByPage()方法");
        //PageInfo<User> info = userService.findByPage(0, 8);   ---写死了
        return userService.findByPage(currentPage,pageSize);
    }
}
