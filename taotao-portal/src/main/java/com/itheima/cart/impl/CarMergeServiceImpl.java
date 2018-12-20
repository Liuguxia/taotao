package com.itheima.cart.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.cart.CarMergeService;
import com.itheima.cart.CartCookieService;
import com.itheima.pojo.Cart;
import com.itheima.pojo.User;
import com.itheima.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service //本地调用，pringframework.stereotype的Service
public class CarMergeServiceImpl implements CarMergeService {
    //cookie 的key
    //private final static  String CART_KEY="iit_cart";
    //redis  的key
    private static final String CART_KEY="iitcart_";


    @Autowired
    private RedisTemplate<String,String> template;

    @Autowired
    private CartCookieService cartCookieService;

    //合并购物车
    @Override
    public void mergeCart(String ticket, HttpServletRequest request, HttpServletResponse response) {
        //1.获取cookie购物车---request
        List<Cart> cookieList = cartCookieService.queryCartByCookie(request);
        System.out.println("从cookie里边获取的购物车是 ： " + cookieList);

        //2.获取redis购物车（为准）
        User user = RedisUtil.findUserByTicket(template, ticket);
        //List<Cart> redisList = RedisUtil.findCartFromRedis(template, CART_KEY + user.getId());
        long userId=user.getId();
        List<Cart> redisList = RedisUtil.findCartFromRedis(template, CART_KEY + userId);
//        String json=redisTemplate.opsForValue().get(CART_KEY+userId);
        System.out.println("从redis里边获取的购物车是 ： " + redisList);


        //3.遍历购物车，判断重复商品，增加数量，合并成一个集合
        for (Cart cart : cookieList) {
            //判断redis的购物车里面是否有cookie相同的商品
            if (redisList.contains(cart)){
                //有这件商品
                int index = redisList.indexOf(cart);
                Cart c=redisList.get(index);
                c.setNum(c.getNum()+cart.getNum());
                c.setUpdate(new Date());
            }else{
                //没有这件商品,直接加到redis的购物车
                redisList.add(cart);
            }
        }
        System.out.println("合并后的购物车是 ： " + redisList);

        //4.再存到redis中
        String json = new Gson().toJson(redisList);
        template.opsForValue().set(CART_KEY + userId,json);

        //5.注意：合并购物车到redis后，原来的cookie购物车直接清除就可以了
        Cookie cookie=new Cookie("iit_cart","");
        cookie.setMaxAge(0);//-1,表示浏览器关闭之后再删除；0，表示收到之后删除
        //cookie.setPath("/");
        response.addCookie(cookie);

    }

    static class Student{
        private int id;
        private String name;

        public Student() {
        }

        public Student(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Student student = (Student) o;
            return id == student.id;
        }

        @Override
        public int hashCode() {

            return Objects.hash(id);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    //main方法，测试一下来个list集合的对象里有一样的元素木有
    public static void main(String[] args) {
        ArrayList<Student> cookieList=new ArrayList<>();
        cookieList.add(new Student(1,"张三"));
        cookieList.add(new Student(2,"李四"));

        ArrayList<Student> redisList=new ArrayList<>();
        redisList.add(new Student(1,"张三"));
        redisList.add(new Student(3,"王五"));
        redisList.add(new Student(4,"马力"));

        //熟悉的代码来了
        for (Student student : cookieList) {
            String name=student.getName();
            System.out.println("取到的名字是  ：" + name);
            boolean flag=redisList.contains(student);//判定包含对象
            System.out.println("redisList是否包含这个学生：" + flag);

//            if(redisList.contains(student)){
//                //有一样的元素
//                int index = redisList.indexOf(student);
//                Student s = redisList.get(index);
//            }else {
//                //没有

//            }
        }
    }
}
