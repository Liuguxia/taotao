package com.itheima.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.pojo.Cart;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class RedisUtil {
    /*
        Redis工具类，
            从Redis查找购物车
            crud后，重新保存购物车到Redis
     */
    //从Redis查找购物车
    public static List<Cart> findCartFromRedis(RedisTemplate<String,String> redisTemplate,String key){
        //1.已登录，获取购物车--redis
        String json=redisTemplate.opsForValue().get(key);
        //2.把json字符串转化为List<Cart>
        List<Cart> cartList = new Gson().fromJson(json, new TypeToken<List<Cart>>(){}.getType());
        return cartList;
    }

    //crud后，重新保存购物车到Redis
    public static void saveCartToRedis(RedisTemplate<String,String> redisTemplate,List<Cart> cartList,String key){
        String json=new Gson().toJson(cartList);
        redisTemplate.opsForValue().set(key,json);
    }


}
