package com.itheima;

import com.google.gson.Gson;
import com.itheima.pojo.User;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Date;

public class SsoTest {
    @Test
    public void testTicket(){
        //创建一个user对象，并把对象变成json字符串
        User user = new User();
        user.setId(10L);
        user.setUsername("sajia");
        user.setEmail("1102356056@qq.com");
        user.setPhone("1164970619");
        user.setPassword("123456");
        user.setCreated(new Date());
        user.setUpdated(new Date());

        String json = new Gson().toJson(user);

        Jedis jedis = new Jedis("192.168.227.128",7003);
        jedis.set("iit_liuziwei",json);
        jedis.close();
    }
}
