package com.itheima.service;

import com.itheima.pojo.Cart;

import java.util.List;

public interface CarService {
    //添加商品到购物车
    /*
        添加商品到购物车，该方法主要用于把商品添加到redis数据库，
        所以这里必须要知道是谁添加的，故引入属性long userId。
        如果在没有登录的情况，加入购物车，那么直接使用cookie来保存添加的商品数据即可。
     */
    void addItemToCart(long userId,long id,int num);

    /*
        根据用户的id，也就是userId，来查询购物车
        如果是未登录的情况，要查看购物车，那么不需要调用，
        这个方法，只需要获取cookie的数据到购物车页面显示即可。
     */

    List<Cart> queryCartByUserId(long userId);

    //更新购物车的数量
    void updateNumByCart(long userId,long itemId,int num);

    //删除购物车的商品
    void deleteItemByCart  (long userId,long itemId);

    //合并购物车
    //因为request，跨模块，要实现序列化接口，但此类封装，故不能被Spring调用，所以合并代码不在此写
    //void mergeCart();
}
