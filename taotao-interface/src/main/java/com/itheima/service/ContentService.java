package com.itheima.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Content;

import java.util.List;

public interface ContentService {
    int add(Content content);
    PageInfo<Content> list(int categoryId,int page,int rows);
    int edit(Content content);
    int delete(String ids);
    //把大广告位的六张图片显示的接口
    //List<Content> selectByCategoryId(long cid);
    String selectByCategoryId(long cid);
}
