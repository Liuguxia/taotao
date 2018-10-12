package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.ContentCategoryService;
import com.itheima.mapper.ContentCategoryMapper;
import com.itheima.pojo.ContentCategory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private ContentCategoryMapper mapper;
    @Override
    public List<ContentCategory> getCategoryByParentId(Long id) {
        ContentCategory category=new ContentCategory();
        category.setParentId(id);

        return mapper.select(category);
    }
}
