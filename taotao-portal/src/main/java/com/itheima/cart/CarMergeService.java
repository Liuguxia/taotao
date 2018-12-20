package com.itheima.cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CarMergeService {
    /*
        合并购物车，因为项目在portal这里，如果写在taotao-cart-service，实例是需要
        是序列化接口，而HttpServletRequest是封装好的接口，故不能改写了
     */
    void mergeCart(String ticket,HttpServletRequest request,HttpServletResponse response);



}
