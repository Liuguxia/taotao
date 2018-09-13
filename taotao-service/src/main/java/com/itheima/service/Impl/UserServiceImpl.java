package com.itheima.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.UserService;
@Service//一定要使用dubbo的
public class UserServiceImpl implements UserService {

    @Override
    public void save() {

        System.out.println("调用了UserServiceImpl的save（）方法");
    }
}
