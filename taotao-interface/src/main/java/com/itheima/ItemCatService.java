package com.itheima;

import com.itheima.pojo.ItemCat;

import java.util.List;

public interface ItemCatService {
    List<ItemCat> selectItemCatByParentId(long parentId);
}
