package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.mapper.ContentMapper;
import com.itheima.pojo.Content;
import com.itheima.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentMapper contentMapper;

    @Override
    public int add(Content content) {
        Date date=new Date();
        content.setCreated(date);
        content.setUpdated(date);
        return contentMapper.insert(content);
    }

    @Override
    public PageInfo<Content> list(int categoryId, int page, int rows) {

        //设置分页
        PageHelper.startPage(page,rows);

        //查询
        Content content = new Content();  //目前还是空对象
        content.setCategoryId((long) categoryId);
        List<Content> list =contentMapper.select(content);
        return new PageInfo<>(list);
    }

    @Override
    public int edit(Content content) {
        content.setUpdated(new Date());
        return contentMapper.updateByPrimaryKeySelective(content);
    }

    @Override
    public int delete(String ids) {//1&35,71
        String[] idArray=ids.split(",");
        int result =0;
        for (String id:idArray){
            result += contentMapper.deleteByPrimaryKey(Long.parseLong(id));
        }

        return result;
    }
}
