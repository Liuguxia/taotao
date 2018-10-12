package com.itheima;

import com.itheima.pojo.ContentCategory;

import java.util.List;

public interface ContentCategoryService {
    List<ContentCategory> getCategoryByParentId(Long id);
}
