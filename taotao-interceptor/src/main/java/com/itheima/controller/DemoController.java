package com.itheima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    @ResponseBody//返回的不是页面，而是一个封装的集合或对象huo字符串
    @RequestMapping("/order/create")
    public String create(){
        System.out.println("开始创建订单");
        return "订单生成成功";
    }
}
