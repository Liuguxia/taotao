package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Content;
import com.itheima.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ContentController {
    @Reference
    private ContentService contentService;

    //添加  Post
    //@RequestMapping("/rest/content")
    @PostMapping("/rest/content")
    public String add(Content content){
        contentService.add(content);
        return "success";
    }

    //查询  Get
    //@RequestMapping("/rest/content")
    @GetMapping("/rest/content")
    public Map<String, Object> list(int categoryId,int page,int rows){
        PageInfo<Content> pageInfo = contentService.list(categoryId,page, rows);

        //easyUI显示列表数据的时候，要求两个字段total，rows
        Map<String, Object> map = new HashMap<>();
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());   //这一页的记录数
        return map;
    }
}
