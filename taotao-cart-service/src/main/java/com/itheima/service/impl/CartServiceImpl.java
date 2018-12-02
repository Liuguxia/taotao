package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.mapper.ItemMapper;
import com.itheima.pojo.Cart;
import com.itheima.pojo.Item;
import com.itheima.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class CartServiceImpl implements CarService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //把商品添加到购物车中
    @Override
    public void addItemToCart(long userId, long id, int num) {

        System.out.println("要添加商品到购物车了");

        //1.加入购物车之前，先查询以前购物车的商品
        List<Cart> cartList = queryCartByUserId(userId);

        /*
            2.遍历上面购物车集合，看看里面的商品是否和现在添加进来的商品一样，
            如果是一样的商品，那么就让这个商品的购买数量+1，
            如果不一样，那么就去查询数据库，获取这个商品的数据，然后追加到cartList中

         */
        Cart c=null;

        for (Cart cart : cartList) {
            //购物车里面有这件商品
            if (cart.getItemId()==id){
                c=cart;
                break;
            }
        }

        //判断是否有一样的商品
        if(c!=null){
            c.setNum(c.getNum()+num);
            c.setUpdate(new Date());
        }else {
            //没有这件商品---去查询数据库，获取这个商品，然后追加到购物车
            Item item = itemMapper.selectByPrimaryKey(id);
            Cart cart=new Cart();
            cart.setItemId(id);
            cart.setItemTitle(item.getTitle());
            cart.setItemImage(item.getImages()[0]);
            cart.setItemPrice(item.getPrice());
            cart.setCreate(new Date());
            cart.setUpdate(new Date());
            cart.setUserId(userId);
            cart.setNum(num);

            cartList.add(cart);
        }

        //把这个list集合保存到redis中
        String json = new Gson().toJson(cartList);
        System.out.println("现在购物车的商品有"+json);
        redisTemplate.opsForValue().set("iitcart_" + userId,json);
    }

    //根据用户的id查询它对应的购物车数据
    @Override
    public List<Cart> queryCartByUserId(long userId) {

        //1.根据用户id查询redis，获取以前的购物车数据，是一个json数据
        String json = redisTemplate.opsForValue().get("iitcart_" + userId);

        //第一次加入购物车，判定json是否为空
        if (!StringUtils.isEmpty(json)){
            //不为空
            //2.把json字符串转化成list<Cart>集合
            List<Cart> cartList=new Gson().fromJson(json,new TypeToken<List<Cart>>(){}.getType());
            //返回以前的购物车数据
            return  cartList;
        }

        //为空，则表示第一次来添加shangp到购物车，所以redis里面不会有任何数据，直接返回一个空的购物车
        return new ArrayList<Cart>();
    }


    //测试
    public static void main(String[] args) {
        List<Teacher> list=new ArrayList<>();

        list.add(new Teacher("张三",18));
        list.add(new Teacher("李四",19));
        list.add(new Teacher("王五",16));

        //吧list集合转成json
        String json = new Gson().toJson(list);
        System.out.println("json===" + json);
        //json===[{"name":"张三","age":18},{"name":"李四","age":19},{"name":"王五","age":16}]
        //list1===[{name=张三, age=18.0}, {name=李四, age=19.0}, {name=王五, age=16.0}]

        //把一个json数组转化成list集合，怎么办？  fromJson（）？这转成一个对象就好办，其实也可以，但不能直接转
        /*
            Type type = new TypeToken<List<ToJsonBeanOne>>() {}.getType();
            List<ToJsonBeanOne> beanOnes = gson.fromJson(jsonString, type);
         */
        Type type=new TypeToken<List<Teacher>>() {}.getType();
        List<Teacher> list1 = new Gson().fromJson(json, type);
        System.out.println("list1===" + list1.get(0).getName());

    }

    static class Teacher{
        private String name;
        private int age;

        public Teacher() {
        }

        public Teacher(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }



}
