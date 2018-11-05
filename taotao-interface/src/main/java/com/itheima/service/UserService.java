package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.User;

public interface UserService {
    Boolean check(String param,int type);

    String selectUser(String ticket);

    /*
        注册用户
     */
    int addUser(User user);

    String login(User user);
//    void save();
//
//    User selectOne(long id);
//
//    PageInfo<User> findByPage(int currentPage, int pageSize);
}
