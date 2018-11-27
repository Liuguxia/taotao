package com.itheima.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {


    @RequestMapping("/page/search.shtml")  //这个路径要琢磨一下怎样得出,首页（其实路径随便写，下面return就是要跳转的页面）
    public String page(){

        System.out.println("要跳转到搜索页面了");

        return "search";
    }
}
