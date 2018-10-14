package com.itheima.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Content;

public interface ContentService {
    int add(Content content);
    PageInfo<Content> list(int categoryId,int page,int rows);
    int edit(Content content);
}
