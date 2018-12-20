package com.itheima.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.pojo.Cart;
import com.itheima.pojo.User;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class RedisUtil {
    public static User findUserByTicket(RedisTemplate template,String ticket){
        String json = (String) template.opsForValue().get(ticket);

        return new Gson().fromJson(json,User.class);
    }

    //获取redis购物车
    public static List<Cart> findCartFromRedis(RedisTemplate<String,String> redisTemplate,String key){
        //2.获取redis购物车（为准）
        String json=redisTemplate.opsForValue().get(key);
        //String json=redisTemplate.opsForValue().get(CART_KEY+userId);
        //把json字符串转化成list集合
        List<Cart> redisList=new Gson().fromJson(json,new TypeToken<List<Cart>>(){}.getType());

        return redisList;
    }
}
