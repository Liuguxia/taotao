package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
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
