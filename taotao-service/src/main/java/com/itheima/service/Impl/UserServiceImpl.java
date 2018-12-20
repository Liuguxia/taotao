package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Boolean check(String param, int type) {
        return null;
    }

    @Override
    public String selectUser(String ticket) {
        return null;
    }

    @Override
    public int addUser(User user) {
        user.setCreated(new Date());
        user.setUpdated(new Date());

        //使用MD5对密码进行加密
        String password=user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);

        return userMapper.insert(user);
    }

    @Override
    public String login(User user) {
        //先对用户的密码进行加密处理
        String password=user.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);

        //根据用户名和密码查询用户
        List<User> list = userMapper.select(user);
        //如果有，集合里面只有一个用户，因为用户名是唯一的
        if (list.size()>0){
            //已经把用户的信息查询出来了，下一步要做的工作是：把用户的信息保存在redis里面
            User loginUser=list.get(0);
            System.out.println("6");
            //把对象变未json字符串
            String json = new Gson().toJson(loginUser);

            String key="iit02_"+UUID.randomUUID().toString();

            System.out.println("key-----" + key+"  "+json);
            //把用户数据json存到redis里面
            redisTemplate.opsForValue().set(key,json);
            //redisTemplate.opsForValue().set(key,json,7,TimeUnit.HOURS);
            System.out.println("7");
            return key;
        }
        return null;
    }


    //使用MD5对密码进行加密,小测试，也就是demo
    //md5不可逆，密文不可转换成明文
    public static void main(String[] args) {
//        String s="123";
        //把字符串转化成字节数组
//        byte[] bytes = s.getBytes();
//        String md5 = DigestUtils.md5DigestAsHex(bytes);
//        System.out.println("123字符经 1次md5加密后:" + md5);
//        for (int i = 0; i < 10; i++) {
//            s=DigestUtils.md5DigestAsHex(bytes);
//        }
//        System.out.println("123字符经11次md5加密后:" + s);



        String s="123";
        for (int i = 0; i < 10; i++) {
            s = DigestUtils.md5DigestAsHex(s.getBytes());
        }
        System.out.println(s);
    }

}
