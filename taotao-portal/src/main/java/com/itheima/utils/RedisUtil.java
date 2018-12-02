package com.itheima.utils;

import com.google.gson.Gson;
import com.itheima.pojo.User;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtil {
    public static User findUserByTicket(RedisTemplate template,String ticket){
        String json = (String) template.opsForValue().get(ticket);

        return new Gson().fromJson(json,User.class);
    }
}
