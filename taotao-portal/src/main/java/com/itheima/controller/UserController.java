package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.User;

import com.itheima.service.UserService;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/*
    用户的注册
 */
@Controller
public class UserController {


    @Reference
    private UserService userService;

    @PostMapping("/user/doRegister.shtml")
    @ResponseBody
    public Map<String,String> register(User user){
        System.out.println("user=" + user);
        int result = userService.addUser(user);
        System.out.println("result=" + result);

        Map<String,String> map=new HashMap<>();
        if (result>0){
            map.put("status","200");
        }else {
            map.put("status","500");
        }

        return map;
    }

    /*
        1.登录成功后，生成一个唯一的key（ticket），把这个key和用户的数据保存在redis数据库中

        2.为了确保下一次直接添加到购物车，能够知道是谁完成的操作，需要从redis里面获取用户的信息，
            要想获取用户的信息，必须拥有key（ticket），把这个key写到cookie里面去，传输给客户端（浏览器）

     */
    @PostMapping("/user/doLogin.shtml")
    @ResponseBody
    public Map<String,String> login(User user, HttpServletResponse response){
        System.out.println(user);

        Map<String,String> map=new HashMap<>();

        //ticket就是redis保存用户数据的key
        String ticket = userService.login(user);
        System.out.println("loginUser=" + ticket);
        //ticket不是空的话，表示登录成功，并且也存到redis里
        if (!StringUtils.isEmpty(ticket)){

            //把用户登录过后的凭证放到cookie里面
            Cookie cookie = new Cookie("ticket",ticket);
            //cookie有效期是7天
            cookie.setMaxAge(60 * 60*24*7);
            //把cookie抛给浏览器response
            response.addCookie(cookie);

            System.out.println("1");
            map.put("status","200");
            System.out.println("2");
            map.put("succes","http://www.baidu.com");
            map.put("success","http://www.taotao.com");
            System.out.println("3");

            return map;

        }

        System.out.println("4");
        map.put("status","500");
//        session.setAttribute("user",loginUser);
        System.out.println("5");
        //登录成功，就回到index页面
        return map;
    }
}
