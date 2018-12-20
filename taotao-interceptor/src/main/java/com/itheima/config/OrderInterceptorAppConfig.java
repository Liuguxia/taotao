package com.itheima.config;

import com.itheima.interceptor.OrderInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/*
    拦截器的配置
 */
@Component  //表示这个类是一个组件
public class OrderInterceptorAppConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        System.out.println("开始注册拦截器");
        //实现指定拦截哪个类，及其路径
        registry.addInterceptor(new OrderInterceptor()).addPathPatterns("/order/*");
    }
}

