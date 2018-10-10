package com.itheima;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Item;

public interface ItemService {
    int addItem(Item item, String desc);
    PageInfo<Item> list(int page, int rows);
}
