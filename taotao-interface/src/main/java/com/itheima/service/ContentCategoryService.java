package com.itheima.service;

import com.itheima.pojo.ContentCategory;

import java.util.List;

public interface ContentCategoryService {
    List<ContentCategory> getCategoryByParentId(Long id);

    ContentCategory add(ContentCategory contentCategory);

    int update(ContentCategory contentCategory);
}
