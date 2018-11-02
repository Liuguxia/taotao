package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

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



        return userMapper.insert(user);
    }
}
