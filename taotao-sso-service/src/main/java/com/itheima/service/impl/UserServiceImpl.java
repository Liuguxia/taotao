package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /*
        检查给的参数是否存在，true存在，false不存在
     */
    @Override
    public Boolean check(String param, int type) {
        User user=new User();
        switch (type){
            case 1:  //用户名
                user.setUsername(param);
                break;
            case 2:  //电话
                user.setPhone(param);
                break;
            case 3:  //邮箱
                user.setEmail(param);
                break;
            default:
                user.setUsername(param);
                break;
        }
        List<User> list = userMapper.select(user);

        //false:表示不能用了，已经被占用
        //true:表示可以使用

        return list.size()>0?false:true;
    }

    @Override
    public String selectUser(String ticket) {
        //这里从redis缓存中获取用户的信息
        String key="iit_"+ticket;
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public int addUser(User user) {
        return 0;
    }


}
