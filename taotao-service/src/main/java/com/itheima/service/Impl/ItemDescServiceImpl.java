package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.ItemDescMapper;
import com.itheima.pojo.ItemDesc;
import com.itheima.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class ItemDescServiceImpl implements ItemDescService {

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Override
    public ItemDesc getDescById(long id) {
        //返回的就是商品描述的详细信息
        return itemDescMapper.selectByPrimaryKey(id);
    }
}
