package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.ContentCategoryService;
import com.itheima.pojo.ContentCategory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCategoryController {
    @Reference
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/rest/content/category")
    @ResponseBody   //返回的不是页面，而是对象或者数据，则要用ResponseBody或者RestController
    public List<ContentCategory> getCategoryByParentId(@RequestParam(defaultValue = "0")Long id){
        List<ContentCategory> list = contentCategoryService.getCategoryByParentId(id);
        return list;
    }
}
