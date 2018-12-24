package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.OrderItemMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.mapper.OrderShippingMapper;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderItem;
import com.itheima.pojo.OrderShipping;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service //对外提供服务
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;

    //提交订单，返回订单号
    //下一条订单，就要往三张表填东西（订单明细表，商品表，物流表）
    @Override
    public String saveOrder(Order order) {
        //设置订 单id
        String ordeId=order.getUserId()+new Random().nextInt(10000)+"";
        order.setOrderId(ordeId);
        //设置订单的状态----未付款：1
        order.setStatus(1);
        //设置订单的下单时间
        order.setCreateTime(new Date());
        //设置订单的更新时间
        order.setUpdateTime(order.getCreateTime());
        //1.往订单表里面添加记录

        orderMapper.insertSelective(order);

        //2.往订单条目表添加记录
            /*
                由于一条订单可以购买多件商品，所以这里需要遍历订单，
                取出每一个orderItem，然后设置它属于哪一个订单
             */
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.setId(UUID.randomUUID().toString().replace("-",""));
            orderItem.setOrderId(ordeId);
            orderItemMapper.insertSelective(orderItem);
        }

        //3.往物流表添加记录---要给物流信息设置属于那一条订单
        OrderShipping shipping = order.getOrderShipping();
        shipping.setOrderId(ordeId);//给物流信息设置属于那一条订单
        shipping.setCreated(new Date());
        shipping.setUpdated(shipping.getCreated());
        orderShippingMapper.insertSelective(shipping);
        return ordeId;
    }
}
