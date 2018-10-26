package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.service.ItemService;
import com.itheima.mapper.ItemDescMapper;
import com.itheima.mapper.ItemMapper;
import com.itheima.pojo.Item;
import com.itheima.pojo.ItemDesc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/*
    service要操作两张表,item和item_desc
 */
@Service
public class ItemServiceImpl implements ItemService {


    //注入通用mapper，才能操作数据库
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemDescMapper itemDescMapper;


    //新增商品
    @Override
    public int addItem(Item item, String desc) {
        //先添加item表
        /*
        itemMapper.insert();            //添加数据，10个列，item（5个有值，5个默认值）
        itemMapper.insertSelective();   //添加数据,Selective有选择性的
         */
        //从页面传过来的item还不完整
        long id=(long)(System.currentTimeMillis()+Math.random()*10000);

        item.setStatus(1);
        item.setId(id);
        item.setCreated(new Date());
        item.setUpdated(new Date());

        int result=itemMapper.insertSelective(item);
        //再添加item_desc表
        ItemDesc itemDesc=new ItemDesc();

        itemDesc.setItemId(id);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());

        itemDescMapper.insertSelective(itemDesc);

        return result;

    }

    @Override
    public PageInfo<Item> list(int page, int rows) {

        //必须要设置这一行，才能分页查询
        PageHelper.startPage(page,rows);
        List<Item> list = itemMapper.select(null);
        return new PageInfo<Item>(list);
        //return new PageInfo(list);
    }

    //查询一个商品的实现类的方法
    @Override
    public Item getItemById(long id) {

        return itemMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Item> getItem() {
        return itemMapper.selectAll();
    }

    @Override
    public int deleteItem(long id) {
        //按照主键来删除
        int i = itemMapper.deleteByPrimaryKey(id);
        return i;
    }

    @Override
    public int updateItem(Item item) {
        //updateByPrimaryKeySelective()方法是有选择的更新，选择想更新的属性即可
        return itemMapper.updateByPrimaryKeySelective(item);
    }

}
