package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.ItemCatService;
import com.itheima.pojo.ItemCat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/*
        商品分类控制器
 */
@Controller
public class ItemCatController {

    //如果把@Reference注释了，就是单单启动子模块
    @Reference
    private ItemCatService itemCatService;

    //默认请求的时候，不带id过来，所以给定一个默认值0，表示获取所有的一级分类数据
    @ResponseBody
    @RequestMapping("/rest/item/cat")
    public List<ItemCat> selectItemCat(@RequestParam(defaultValue = "0") long id){
        //查询所有分类
        List<ItemCat> list = itemCatService.selectItemCatByParentId(id);

        //打印list集合
        System.out.println("list==" + list);
        return list;
    }
}
