package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.User;

public interface UserService {
    Boolean check(String param,int type);

    String selectUser(String ticket);

//    void save();
//
//    User selectOne(long id);
//
//    PageInfo<User> findByPage(int currentPage, int pageSize);
}
