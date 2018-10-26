package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Item;

import java.util.List;

/**
 * 测试restful风格
 */

public interface ItemService {
    //新增商品
    int addItem(Item item, String desc);

    PageInfo<Item> list(int page, int rows);

    //查询商品restful，查一个
    Item getItemById(long id);

    //查询所有商品
    List<Item> getItem();

    //删除商品
    int deleteItem(long id);

    //更新商品
    int updateItem(Item item);

}
