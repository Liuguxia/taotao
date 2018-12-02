package com.itheima.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
    public static String findTicket(HttpServletRequest request){
        String ticket=null;

        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if ("ticket".equals(name)){
                    ticket=cookie.getValue();

                    break;
                }
            }
        }
        return ticket;
    }
}
