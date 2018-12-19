package com.itheima.cart;

import com.itheima.pojo.Cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CartCookieService {
    /*
        没有登录状态下的购物车crud，购物车保存在cookie里，
        而登录的则保存在redis里面
     */
    void addItemByCookie(long itemId, int num, HttpServletRequest request, HttpServletResponse response);

    //从cookie里面查询购物车
    List<Cart> queryCartByCookie(HttpServletRequest request);

    //未登录状态下更新购物车
    void updateCartByCookie(long itemId,int num,HttpServletRequest request, HttpServletResponse response);

    //未登录状态下删除购物车
    void deleteCartByCookie(long itemId,HttpServletRequest request, HttpServletResponse response);
}
