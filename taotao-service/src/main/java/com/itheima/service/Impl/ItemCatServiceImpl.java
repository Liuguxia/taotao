package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.ItemCatService;
import com.itheima.mapper.ItemCatMapper;
import com.itheima.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;
    @Override
    public List<ItemCat> selectItemCatByParentId(long parentId) {

        //因为这是按照普通列来查询的，所以不能使用主键的查询方法selectByPrimaryKey()
        ItemCat itemCat=new ItemCat();
        itemCat.setParentId(parentId);

        //相当于按照学生姓名查询
//        Student stu=new Student();
//        stu.setName("zhangsan");
//        itemCatMapper.select(stu);

        return itemCatMapper.select(itemCat);
    }
}
