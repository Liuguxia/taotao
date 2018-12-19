package com.itheima.cart.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.pojo.Cart;
import com.itheima.pojo.Item;
import com.itheima.cart.CartCookieService;
import com.itheima.service.ItemService;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/*
    因为是同一个项目的controller调用，即controller调用service，故serivce注解用自己的，不须远程调用
 */
@Service
public class CartCookieServiceImpl implements CartCookieService {

    private final static  String CART_KEY="iit_cart";

    @Reference //远程引用dubbo
    private ItemService itemService;

    @Override
    public void addItemByCookie(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
        //没有登录---购物车商品保存在cookie里面
        //先从cookie里面查询购物车
        List<Cart> cartList = queryCartByCookie(request);
//        //1.先获取以前的购物车,cartList仍然有可能为空：因为第一次上来的时候
//        List<Cart> cartList =null;
//
//        try {
//            Cookie[] cookies = request.getCookies();
//            if(cookies!=null){
//                for (Cookie cookie : cookies) {
//                    if (CART_KEY.equals(cookie.getName())){
//                        String json=cookie.getValue();
//                        //解码----对字符串进行解码处理，把%那些变成正常的文本
//                        json=URLDecoder.decode(json,"utf-8");
//                        //List<Cart> cartList=new Gson().fromJson(json,new TypeToken<List<Cart>>(){}.getType());
//                        cartList = new Gson().fromJson(json,new TypeToken<List<Cart>>(){}.getType());
//
//                    }
//                }
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        //从cookie里面拿出来的购物车cartList为空，表示以前没有创建过购物车，所以这里要创建一个新的购物车集合cartList
//        if (cartList==null){
//            cartList=new ArrayList<>();
//        }



        //2.判断购物车里是否有这一件商品，若有就累加数量，反之就构建全新的cart对象
        Cart c=null;
        for (Cart cart : cartList) {
            if (itemId==cart.getItemId()){
//                cart.setNum(num);
//                cart.setUpdate(new Date());
                c=cart;
                break;
            }
        }
        if (c!=null){
            //表示购物车有这件商品
            c.setNum(c.getNum()+num);
        }else {
            //表示购物车没有这件商品
            Item item = itemService.getItemById(itemId);

            Cart cart=new Cart();
            cart.setItemId(itemId);
            cart.setNum (num);
            cart.setCreate(new Date());
            cart.setUpdate(new Date());
            cart.setItemImage(item.getImages()[0]);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());

            //把这件新商品存到购物车
            cartList.add(cart);
        }
        //3.把组装好的List<Cart>====》json放到cookie里面去
        String json = new Gson().toJson(cartList);
        System.out.println("购物车已经添加到cookie去了  ----没有登录的状态下  *%^&*  "+json);
        //编码------对字符串内进行url编码，那些空格都会变成%20，因为cookie不支持空格
        try {
                json=URLEncoder.encode(json,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        Cookie cookie=new Cookie(CART_KEY,json);
        cookie.setMaxAge(60*60*24*7);//cookie存活一周
        cookie.setPath("/");//设置cookie为全路径

        response.addCookie(cookie);

    }

    //从cookie里面查询购物车
    @Override
    public List<Cart> queryCartByCookie(HttpServletRequest request){
        //1.先获取以前的购物车,cartList仍然有可能为空：因为第一次上来的时候
        List<Cart> cartList =null;
        try {
            Cookie[] cookies = request.getCookies();
            if(cookies!=null){
                for (Cookie cookie : cookies) {
                    if (CART_KEY.equals(cookie.getName())){
                        String json=cookie.getValue();
                        //解码----对字符串进行解码处理，把%那些变成正常的文本
                        json=URLDecoder.decode(json,"utf-8");
                        //List<Cart> cartList=new Gson().fromJson(json,new TypeToken<List<Cart>>(){}.getType());
                        cartList = new Gson().fromJson(json,new TypeToken<List<Cart>>(){}.getType());

                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //从cookie里面拿出来的购物车cartList为空，表示以前没有创建过购物车，所以这里要创建一个新的购物车集合cartList
        if (cartList==null){
            cartList=new ArrayList<>();
        }
        return cartList;
    }



    //未登录状态下更新购物车
    @Override
    public void updateCartByCookie(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
        //1.查询以前的购物车
        List<Cart> cartList = queryCartByCookie(request);
        //2.遍历购物车，找到匹配的的商品，修改数量
        //Cart c=null;   //接一下这个cart
        for (Cart cart : cartList) {
            if (itemId==cart.getItemId()){
                //直接修改
                cart.setNum(num);
                cart.setUpdate(new Date());
            }
        }
        //3.修改好的购物车，重新存储到cookie里面去,又需要json转换
        try {
            String json=new Gson().toJson(cartList);
            json=URLEncoder.encode(json,"utf-8");
            Cookie cookie=new Cookie(CART_KEY,json);
            //设置一下cookie
            cookie.setMaxAge(60*60*24*7);
            cookie.setPath("/");

            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //未登录状态下删除购物车
    @Override
    public void deleteCartByCookie(long itemId, HttpServletRequest request, HttpServletResponse response) {
        //从cookie里查询购物车
        List<Cart> cartList = queryCartByCookie(request);
        //根据itemId查找购物

        //为了确保删除万无一失，建议使用迭代器删除
        Iterator<Cart> iterator = cartList.iterator();
        while (iterator.hasNext()){
            Cart cart = iterator.next();
            if (cart.getItemId()==itemId){
                iterator.remove();
            }
        }
//        for (Cart cart : cartList) {
//            if (cart.getItemId()==itemId){
//                cartList.remove(cart);
//                break;
//            }
//        }
        //合并购物，从新放回cookie
        try {
            String json=new Gson().toJson(cartList);
            json=URLEncoder.encode(json,"utf-8");

            Cookie cookie=new Cookie(CART_KEY,json);
            cookie.setMaxAge(60*60*24*7);
            cookie.setPath("/");

            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
