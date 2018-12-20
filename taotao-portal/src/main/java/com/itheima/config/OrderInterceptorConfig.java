package com.itheima.config;

import com.itheima.interceptor.OrderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
    配置拦截器，需要继承父类WebMvcConfigurerAdapter，重写拦截方法吧
 */
@Component  //表明这是一个组件
public class OrderInterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private OrderInterceptor orderInterceptor;
    //重写拦截方法，明确要拦截的是哪个拦截器，并且添加拦截路径
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("开始注册订单拦截器了");

        registry.addInterceptor(orderInterceptor).addPathPatterns("/order/**");
        //registry.addInterceptor(new OrderInterceptor()).addPathPatterns("");
    }
}
