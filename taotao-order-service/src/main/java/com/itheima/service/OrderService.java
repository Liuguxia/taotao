package com.itheima.service;

import com.itheima.pojo.Order;

public interface OrderService {
    //提交订单，返回订单号
    String saveOrder(Order order);
    //根据orderId去查询订单信息
    Order queryOrderByOrderId(String orderId);

}
