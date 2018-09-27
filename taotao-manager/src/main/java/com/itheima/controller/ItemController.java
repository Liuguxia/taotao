package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.ItemService;
import com.itheima.pojo.Item;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
    这是有关商品处理的控制器，商品信息和详情涉及到两张表
 */
@Controller
public class ItemController {
    @Reference
    private ItemService itemService;


    @RequestMapping("/rest/item")
    @ResponseBody   //显示字符串，而不是网页
    public String addItem(Item item,String desc){

        int result=itemService.addItem(item,desc);
        System.out.println("result===" + result);
        return "success";
    }


    /*
        添加商品的时候，大部分的内容都会装载到item对象里面去，然后item对象要添加到item表里

        商品的描述，使用desc来接收，然后要添加到item_desc这张表
     */
}
