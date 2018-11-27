package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Reference
    private SearchService searchService;

    //search.taotao.com/search.shtml
    @RequestMapping("/search")
    public String search(String q,@RequestParam(defaultValue = "1") int page){

        System.out.println("要跳转到搜索页面啦:  "+q);
        searchService.searchItem(q,page);

        return "search";
    }

}
