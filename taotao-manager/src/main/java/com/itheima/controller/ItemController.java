package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.service.ItemService;
import com.itheima.pojo.Item;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/*
    这是有关商品处理的控制器，商品信息和详情涉及到两张表
 */
//Restful  api风格
//@PutMapping       更新
//@PostMapping      添加
//@GetMapping       查询
//@DeleteMapping    删除
@Controller
public class ItemController {
    @Reference
    private ItemService itemService;


    @RequestMapping(value = "/rest/item",method = RequestMethod.POST)
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

    @RequestMapping(value = "/rest/item",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> list(int page,int rows){
        PageInfo<Item> pageInfo = itemService.list(page, rows);
        //easyUI的数据表格显示数据，要求有固定的格式，json属性里面有：total和rows
        //total:表示总共有多少条记录
        //rows：表示当前这一页的记录数，也就是list集合
        Map<String,Object> map=new HashMap<>();
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        return map;
    }

    //根的映射
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
