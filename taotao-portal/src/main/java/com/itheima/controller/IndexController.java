package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.itheima.pojo.Content;
import com.itheima.pojo.User;
import com.itheima.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Reference
    private ContentService contentService;

    //www.taotoa.com/register.html，，，通用页面？？？2018.12.20
    @RequestMapping("/page/{pageName}")
    public String page(@PathVariable String pageName,String url,HttpServletRequest request){

        request.setAttribute("url",url);
        //这个方法会跳转到登录页面
        return pageName;
    }

    //www.taotao.com
    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request){
        System.out.println("要查询的首页广告的数据出来了~~~~");

        //在这里获取ticket，然后到redis里面去查询用户数据，然后放到页面显示就好了
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for(Cookie cookie:cookies){
                String name = cookie.getName();
                System.out.println("name =  " + name);
                if ("ticket".equals(name)){

                    String key = cookie.getValue();
                    //对象：{"username":zahngsan,"password":123}
                    System.out.println(name + "  =  " + key);

                    String useInfo = redisTemplate.opsForValue().get(key);
                    User user = new Gson().fromJson(useInfo, User.class);
                    model.addAttribute("user",user);
                    break;
                }
            }
        }

        //要把大广告位的6张图片查询出来
        int categoryId=89;
    /*
        这里查询回来的是集合，里面装的是content对象，但是页面显示的6张图片要求的数据格式不是这样的
        [{
            "srcB": "http://image.taotao.com/images/2015/03/03/2015030304324649807137.jpg",
            "height": 240,
            "alt": "",
            "width": 670,
            "src": "http://192.168.227.131/group1/M00/00/00/wKjjg1vEfnOAUZs_AAFNz-hpDa8056.jpg",
            "widthB": 550,
            "href": "http://sale.jd.com/act/eDpBF1s8KcTOYM.html?cpdad=1DLSUE",
            "heightB": 240
        }
        {}，
        {}，
        {}
        ]
     */
        //List<Content> contents = contentService.selectByCategoryId(categoryId);
        //System.out.println("contents=" + contents);

        String json = contentService.selectByCategoryId(categoryId);
        System.out.println("json=" + json);

//        List<Map<String,Object>> list=new ArrayList<>();
//        //把从数据库查询出来的集合遍历，一个content就对应一个map集合
//        for (Content content:contents){
//            Map<String,Object> map=new HashMap<>();
//            map.put("src",content.getPic());
//            map.put("height",240);
//            map.put("width",670);
//            map.put("href",content.getUrl());
//            list.add(map);
//        }



        //把list ---->Gson|Fastjson ---->json字符串转化
        //String json = new Gson().toJson(list);

        model.addAttribute("list",json);


        //这里的index仅仅起到的是跳转页面
        return "index";
    }
}
