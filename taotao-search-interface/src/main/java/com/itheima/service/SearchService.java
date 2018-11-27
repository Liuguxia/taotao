package com.itheima.service;

import com.itheima.pojo.Item;
import com.itheima.pojo.Page;

public interface SearchService {
    /*
        搜索商品
        q：搜索的关键字
        page：查询的页数
        返回值是封装好的Page对象
     */

   Page<Item> searchItem(String q, int page);

}
