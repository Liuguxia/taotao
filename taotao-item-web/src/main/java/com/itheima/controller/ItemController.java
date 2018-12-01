package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Item;
import com.itheima.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/*
    商品详情页
    url：   http://item.taotao.com/item/${item.id }.html

 */
@Controller
public class ItemController {

    @Reference
    private ItemService itemService;

    @RequestMapping("/item/{id}")
    public String item(@PathVariable Long id, Model model){

        //根据id查询到商品数据
        Item item = itemService.getItemById(id);

        model.addAttribute("item",item);

        return "item";
    }
}
