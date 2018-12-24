package com.itheima.util;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtil {
    public static  String getOrderId(RedisTemplate template,String key){
        return ""+template.opsForValue().increment(key,1);
    }
}
