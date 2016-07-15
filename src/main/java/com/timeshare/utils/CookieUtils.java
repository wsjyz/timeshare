package com.timeshare.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by user on 2016/7/12.
 */
public class CookieUtils {

    public static void setCookie(HttpServletResponse response,String key,String value,int age){
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request,String key){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(key)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
