package com.itheima.service;

import com.itheima.pojo.ItemCat;

import java.util.List;

public interface ItemCatService {
    List<ItemCat> selectItemCatByParentId(long parentId);
}
