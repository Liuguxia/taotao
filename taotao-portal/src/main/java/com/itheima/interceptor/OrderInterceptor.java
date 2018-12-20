package com.itheima.interceptor;
/*
    1.创建拦截器：OrderInterceptor此类就是，需实现接口HandlerInterceptor
        拦截的作用：没有登录，经跳转到登录页面或者是首页，
            检测到登录了，就直接生成预订单
    2.配置拦截器，需继承父类WebMvcConfigurerAdapter
 */

import com.itheima.pojo.User;
import com.itheima.utils.CookieUtil;
import com.itheima.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component  //表示是一个组件
public class OrderInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String,String> template;


    //处理拦截请求，生成预定订单前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*
            关于用户的登录，我们会保存两份信息，一份是ticket，一份是user数据
            ticket是保存在cookie里面，cookie是保存在客户端上ticket的数据本来就小，所有cookie的额数据
            可以保存很长很久

            用户的数据是存放在服务器端，redis里面存放数据，其实可以设置数据的额有效期时间
            redis.set("key","value"),还可以redis.set("key","value",时间)--->>>redis.set("key","value",1000*60)
         */

        String ticket = CookieUtil.findTicket(request);
        //User user = RedisUtil.findUserByTicket(, ticket);
        if(StringUtils.isEmpty(ticket)){
            //没有登录，拦截
            System.out.println("----没有登录，拦截请求,将跳转到登录页面");

            //先获取本来这个请求要到达的具体位置
            String url=request.getRequestURL().toString();//取的是全路径
            String uri=request.getRequestURI();           //取的是后半段路径
            System.out.println("---  url  :" + url);
            System.out.println("---  uri  :" + uri);

            response.sendRedirect("/page/login.shtml?url="+uri);
            return false;
        }

        //除了判定ticket是否有值以外，还多做一重判定，就是判定redis里面的user是否有值
        User user = RedisUtil.findUserByTicket(template, ticket);
        if (user==null){
            //登录失效了
            System.out.println("----登录失效了，拦截请求,将跳转到登录页面");
            response.sendRedirect("/page/login.shtml");

            return false;
        }

        //已经登录了，放行
        System.out.println("----已经登录，放行");
        request.setAttribute("user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
