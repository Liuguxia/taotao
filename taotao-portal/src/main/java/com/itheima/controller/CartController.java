package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Cart;
import com.itheima.pojo.User;
import com.itheima.service.CarService;
import com.itheima.utils.CookieUtil;
import com.itheima.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CartController {

//    @Reference
//    private ItemService itemService;
    @Autowired
    private RedisTemplate<String,String> template;

    @Reference
    private CarService carService;


    //添加商品到购物车  "http://www.taotao.com/cart/add/${item.id}.html?num="+num;
    //rest风格        http://www.taotao.com/cart/add/{id}/{num}
    @RequestMapping("/cart/add/{id}.html")
    public String addToCart(@PathVariable long id,int num,HttpServletRequest request){
        /*
            controller负责请求，至于请求怎么处理，怎么做，都交给service，也就是服务层来实现
         */
        //从cookie里面获取用户的登录凭证
        String ticket = CookieUtil.findTicket(request);

        //1.查询商品
        //Item item = itemService.getItemById(id);
        //如果凭证不为空，表示已经登录了
        if (ticket!=null){
            //登录了
            //根据ticket去redis中获取用户数据
            User user = RedisUtil.findUserByTicket(template, ticket);

            //把商品添加到购物车，处于登录的分支
            carService.addItemToCart(user.getId(),id,num);
        }else{
            //没有登录
        }


        //2.添加到购物车


        //要去购物车显示数据了
        return "cartSuccess";
    }

    //显示购物车  http://www.taotao.com/cart/cart.html
    @RequestMapping("/cart/cart.html")
    public String showCart(HttpServletRequest request,Model model){
        //1.获取用户数据,   从cookie里面获取用户的登录凭证
        String ticket = CookieUtil.findTicket(request);
        //2.到redis中获取购物车的数据
        //如果凭证不为空，表示已经登录了
        if (ticket!=null){
            //登录了
            //根据ticket去redis中获取用户数据
            User user = RedisUtil.findUserByTicket(template, ticket);

            List<Cart> cartList = carService.queryCartByUserId(user.getId());

            model.addAttribute("cartList",cartList);


        }else{
            //没有登录
        }

        //3.存储到model里面


        return "cart";
    }
}
