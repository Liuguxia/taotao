package com.itheima.service;

import com.itheima.pojo.Order;

public interface OrderService {
    //提交订单，返回订单号
    String saveOrder(Order order);
}
