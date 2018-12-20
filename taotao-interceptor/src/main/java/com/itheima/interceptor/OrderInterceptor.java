package com.itheima.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    自定义的拦截器,需要实现接口HandlerInterceptor，但仍要声明拦截什么东西，即它的配置
    1.创建拦截器   ---OrderInterceptor implements HandlerInterceptor
    2.配置拦截器（spring---xml）   ---OrderInterceptor
 */


@Component  //表示这个类是项目中的一个组件（成员），作用类似于service和controller
public class OrderInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //false：不放行；true，放行
        System.out.println("preHandle-----");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle-----");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion-----");

    }
}
