package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Cart;
import com.itheima.pojo.Order;
import com.itheima.pojo.User;
import com.itheima.service.CarService;
import com.itheima.OrderService;
import com.itheima.utils.CookieUtil;
import com.itheima.utils.RedisUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {
    /*
        订单控制器
     */

    @Autowired
    private RedisTemplate<String,String> template;
    @Reference
    private CarService carService;
    @Reference
    private OrderService orderService;


    //跳转到预览订单页面(去结算订单)
    //http://www.taotao.com/order/order-cart.shtml  登不登录都是使用这个路径，故使用拦截器
    @RequestMapping("/order/order-cart.shtml")
    public String create(HttpServletRequest request,Model model){
        //1.查询购物车商品---redis
        //String ticket = CookieUtil.findTicket(request);
        //User user = RedisUtil.findUserByTicket(template, ticket);
        //此处的request是一个作用域，在同一次请求中，不再需要去查找user数据，因为在拦截器哪里已经
        //存储好登录的user对象，放到request作用域里面了request.setAttribute()
        User user = (User) request.getAttribute("user");
        List<Cart> carts = carService.queryCartByUserId(user.getId());

        //2.存储到model
        model.addAttribute("carts",carts);

        return "order-cart";
    }

    //提交订单  http://www.taotao.com/service/order/submit
    @RequestMapping("/service/order/submit")
    @ResponseBody
    public Map<String,Object> sumbitOrder(Order order,HttpServletRequest request){
        //1.给订单设置下单的用户，谁提交的订单
            //先查询用户
        String ticket = CookieUtil.findTicket(request);
        User user=RedisUtil.findUserByTicket(template,ticket);

        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());  //买家昵称
        //2.执行订单的添加操作，然后返回订单的id
        String orderId = orderService.saveOrder(order);
        System.out.println("orderId=" + orderId);
        //3.把订单的id封装到结果，然后抛出给页面
        Map<String,Object> map=new HashMap<>();
        map.put("status",200);
        map.put("data",orderId);


        System.out.println("order "+order);

        return map;
    }

    //点击提交订单之后跳转到---http://www.taotao.com/order/success.html?id=226
    //也就是显示订单
    /*
        提交完订单之后，来到这个方法，然后由这个方法根据id去查询订单信息，再跳转到订单详情页面
     */
    //@RequestMapping("/order/success.html?id={orderId}")
    @RequestMapping("/order/success.html")   //get的请求
    public String showOrder(String id,Model model){
        //1.根据订单id，查询订单对象
        Order order = orderService.queryOrderByOrderId(id);
        model.addAttribute("order",order);

        //到达时间
        String date=new DateTime().plusDays(2).toString("yyyy年MM月dd日HH时mm分ss秒SSS毫秒");

        model.addAttribute("date",date);

        //跳转到success页面
        return "success";
    }


}
