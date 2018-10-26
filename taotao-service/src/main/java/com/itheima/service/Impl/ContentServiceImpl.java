package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.itheima.mapper.ContentMapper;
import com.itheima.pojo.Content;
import com.itheima.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import java.util.*;
        /*
            单纯的查询，都是直接先从redis缓存拿数据，有则直接返回给前台页面，
            没有则从数据库查找，再存入redis缓存，然后再返回给页面

            除单纯的查询外，增删改，都是先操作数据库，然后清空该字段的redis缓存
         */

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private RedisTemplate<String ,String > redisTemplate;

    //添加操作
    @Override
    public int add(Content content) {
        Date date=new Date();
        content.setCreated(date);
        content.setUpdated(date);
        int result =contentMapper.insert(content);
        //先添加一条数据到数据库，然后再清空redis缓存（毕竟单纯查找的时候，redis为空，直接存数据库查找）
        //然后再存到redis缓存中去
//        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
//        opsForValue.set("bingAd","");

        return result;
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

    //更新用户
    @Override
    public int edit(Content content) {
        content.setUpdated(new Date());

        int result = contentMapper.updateByPrimaryKeySelective(content);

        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("bingAd","");

        return result;
    }

    //删除操作
    @Override
    public int delete(String ids) {//1&35,71
        String[] idArray=ids.split(",");
        int result =0;
        for (String id:idArray){
            result += contentMapper.deleteByPrimaryKey(Long.parseLong(id));
        }

        //在删除mysql记录完毕之后，也跟着删除redis数据（删一条数据库数据，就清空redis所有缓存）
        /*
            数据库删除一条数据后，就会清空redis缓存，
            此时，再查询前台页面，首先找redis缓存，但缓存被清空了，故又要找数据库，然后又将数据库的数据
            存到redis缓存中去
         */
        ValueOperations<String, String> opForValues = redisTemplate.opsForValue();
        opForValues.set("bingAd","");
        return result;
    }


    //查询操作
    @Override
    //public List<Content> selectByCategoryId(long cid) {
    public String selectByCategoryId(long cid) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String json = operations.get("bingAd");
        System.out.println("从缓存里面获取广告数据  : " + json);
        //判断字符串json是否为空
        if (!StringUtils.isEmpty(json)){//false.非空
            //如果是空，表示缓存没有
            System.out.println("**缓存里面有广告的数据，直接返回**");
            return json;
        }
        System.out.println("---缓存里面没有广告的数据，从mysql里面查找---");


        //如果执行到这里，表示缓存里没有。要查数据库了
        Content c=new Content();
        //由于是按照分类id去查询的，所以一定要给这个对象的属性分类id赋值
        c.setCategoryId(cid);
        //从mysql数据库查询出来广告的数据
        List<Content> contents = contentMapper.select(c);

        //自己组拼页面要求的字段信息
        List<Map<String,Object>> list=new ArrayList<>();

        //把从数据库查询出来的集合遍历，一个content就对应一个map集合
        for (Content content:contents){
            Map<String,Object> map=new HashMap<>();
            map.put("src",content.getPic());
            map.put("height",240);
            map.put("width",670);
            map.put("href",content.getUrl());
            list.add(map);
        }

        //把这个list变成json字符串然后存进去
        json = new Gson().toJson(list);
        //存到redis里面去
        operations.set("bingAd",json);
        System.out.println("....从mysql数据库查到的数据，存到redis缓存中去....");
        return json;
    }
    /*
        1.先从redis里面拿

        2.有就直接返回，没有的话，就去查询mysql数据库

        3.查询完毕，需要把查询到的数据缓存到redis里面，并且返回这份数据给页面显示
     */
    //这里是获取首页的的内容，目前仅仅是获取了6张广告图片






//    首页直接从数据拿的数据，没有使用redis缓存
//    @Override
//    public List<Content> selectByCategoryId(long cid) {
//        Content content=new Content();
//        //由于是按照分类id去查询的，所以一定要给这个对象的属性分类id赋值
//        content.setCategoryId(cid);
//        return contentMapper.select(content);
//
//    }
}
