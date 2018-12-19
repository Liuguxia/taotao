package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Cart;
import com.itheima.pojo.User;
import com.itheima.service.CarService;
import com.itheima.cart.CartCookieService;
import com.itheima.utils.CookieUtil;
import com.itheima.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Controller
public class CartController {

//    @Reference
//    private ItemService itemService;
    @Autowired
    private RedisTemplate<String,String> template;

    @Reference
    private CarService carService;

    @Autowired  //内部调用Autowired
    private CartCookieService cartCookieService;


    //添加商品到购物车  "http://www.taotao.com/cart/add/${item.id}.html?num="+num;
    //rest风格        http://www.taotao.com/cart/add/{id}/{num}
    @RequestMapping("/cart/add/{id}.html")
    public String addToCart(@PathVariable long id, int num, HttpServletRequest request, HttpServletResponse response){
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

            //把商品添加到购物车，处于登录的分支(搜狗浏览器读不出)
            carService.addItemToCart(user.getId(),id,num);
        }else{
            //没有登录---购物车商品保存在cookie里面
            System.out.println("    ####没有登录的状态下####      添加商品到购物车");
            cartCookieService.addItemByCookie(id,num,request,response);

//            //1.先获取以前的购物车
//            Cookie[] cookies = request.getCookies();
//            if(cookies!=null){
//                for (Cookie cookie : cookies) {
//                    if ("iit_cart".equals(cookie.getName())){
//                        String json=cookie.getValue();
//                        List<Cart> cartList = new Gson().fromJson(json, new TypeToken<List<Cart>>() {}.getType());
//
//                    }
//                }
//            }
//            //2.判断购物车里是否有这一件商品，若有就累加数量，反之就构建全新的cart对象
//            //3.把组装好的List<Cart>====》json放到cookie里面去
        }

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
        List<Cart> cartList =null;

        if (ticket!=null){
            //登录了
            //根据ticket去redis中获取用户数据
            User user = RedisUtil.findUserByTicket(template, ticket);

            cartList = carService.queryCartByUserId(user.getId());

            //model.addAttribute("cartList",cartList);


        }else{
            //没有登录
            cartList = cartCookieService.queryCartByCookie(request);

            //model.addAttribute("cartList",cartList);
        }

        //3.存储到model里面
        model.addAttribute("cartList",cartList);
        return "cart";
    }

    //购物车的数量更新
    //http://www.taotao.com/service/cart/update/num/1385872974/4
    @RequestMapping("/service/cart/update/num/{id}/{num}")
    @ResponseBody
    public void updateNumByCart(@PathVariable long id,@PathVariable int num,HttpServletRequest request,HttpServletResponse response){
        //又需要登录的信息
        String ticket=CookieUtil.findTicket(request);
        if (ticket!=null){
            //根据ticket去redis中获取用户数据
            User user=RedisUtil.findUserByTicket(template,ticket);

            carService.updateNumByCart(user.getId(),id,num);
        }else {
            //表示没有登录下，从cookie里更新购物车数量
            cartCookieService.updateCartByCookie(id,num,request,response);


        }
        //return null;
    }


    //删除购物车商品
    //http://www.taotao.com/cart/delete/1385872974.shtml
    //http://www.taotao.com/cart/delete/1460827382.shtml
    @RequestMapping("/cart/delete/{id}")
    //@ResponseBody
    public String deleteItemByCart(@PathVariable long id, HttpServletRequest request,HttpServletResponse response){
        String ticket=CookieUtil.findTicket(request);
        if (ticket!=null){
            User user = RedisUtil.findUserByTicket(template, ticket);
            //调用删除方法（重点学习）
            carService.deleteItemByCart(user.getId(),id);
        }else{
            //没登录
            cartCookieService.deleteCartByCookie(id,request,response);

        }
        //return "删除成功";
        //return showCart();不要直接调用方法，也不要写请求转发，使用重定向
        /*
            从定向作用：请求的路径点击后，页面地址不变
         */
        return "redirect:/cart/cart.html";
    }


}
