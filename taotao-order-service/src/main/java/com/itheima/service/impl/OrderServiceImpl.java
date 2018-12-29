package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.OrderItemMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.mapper.OrderShippingMapper;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderItem;
import com.itheima.pojo.OrderShipping;
import com.itheima.OrderService;
import com.itheima.util.RedisUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service //对外提供服务
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;
    @Autowired
    private RedisTemplate<String,String> template;

    //提交订单，返回订单号
    //下一条订单，就要往三张表填东西（订单明细表，商品表，物流表）
    @Override
    public String saveOrder(Order order) {
        //设置订 单id
        //String ordeId=order.getUserId()+new Random().nextInt(10000)+"";
        long useId=order.getUserId();
        String orderId = useId+RedisUtil.getOrderId(template, "order_" + useId);
        order.setOrderId(orderId);
        System.out.println("====orderId+++++"+orderId);
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
            orderItem.setOrderId(orderId);
            orderItemMapper.insertSelective(orderItem);
        }

        //3.往物流表添加记录---要给物流信息设置属于那一条订单
        OrderShipping shipping = order.getOrderShipping();
        shipping.setOrderId(orderId);//给物流信息设置属于那一条订单
        shipping.setCreated(new Date());
        shipping.setUpdated(shipping.getCreated());
        orderShippingMapper.insertSelective(shipping);
        return orderId;
    }

    @Override
    public Order queryOrderByOrderId(String orderId) {
        //订单信息是存放在三张表里面，tb_order,tb_order_item,tb_order_shipping
        //要查看某一条订单的信息，就必须查询三张表
        //1.查询订单表
        Order order = orderMapper.selectByPrimaryKey(orderId);
        //2.查询订单商品条目表
        OrderItem item=new OrderItem();
        item.setOrderId(orderId);
        List<OrderItem> orderItems = orderItemMapper.select(item);//因为select查询需要的是对象，而不是主键

        order.setOrderItems(orderItems);
        //3.查询订单物流表
//        OrderShipping shipping=new OrderShipping();
//        shipping.setOrderId(orderId);
//        List<OrderShipping> orderShippings = orderShippingMapper.select(shipping);
        OrderShipping shipping = orderShippingMapper.selectByPrimaryKey(orderId);
        order.setOrderShipping(shipping);

        return order;
    }

    @Override
    public void clearOrder() {
        /*
            订单状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
         */
        //清除无效订单(更新订单，状态为1，时间为2天前)
        Example example=new Example(Order.class);
        Example.Criteria criteria=example.createCriteria();
        //设置例子的条件
        criteria.andEqualTo("status",1);//相当于where status=1
        criteria.andEqualTo("paymentType",1);//相当于支付为在线支付

        criteria.andLessThanOrEqualTo("createTime",new DateTime().minusDays(2).toDate());


        Order order=new Order();
        order.setStatus(6);//交易关闭
        order.setCloseTime(new Date());

        int rows = orderMapper.updateByExampleSelective(order, example);
        System.out.println("rows:"+rows);
    }
}
