package com.itheima;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.User;

public interface UserService {
    void save();

    User selectOne(long id);

    PageInfo<User> findByPage(int currentPage, int pageSize);
}
