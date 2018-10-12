package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.service.UserService;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service//一定要使用dubbo的
public class UserServiceImpl implements UserService {
    //要想在service里面使用Mapper，那么service模块必须依赖mapper模块
    @Autowired
    private UserMapper userMapper;
    @Override
    public void save() {

        System.out.println("调用了UserServiceImpl的save（）方法");
    }

    @Override
    public User selectOne(long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<User> findByPage(int currentPage, int pageSize) {
        //要做分页效果，首先实现分页配置
        PageHelper.startPage(currentPage,pageSize);

        List<User> users = userMapper.selectAll();
        PageInfo info=new PageInfo<>(users);
        return info;
    }
}
