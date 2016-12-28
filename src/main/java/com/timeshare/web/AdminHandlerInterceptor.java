package com.timeshare.web;

import com.timeshare.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by user on 2016/12/28.
 */
public class AdminHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String loginSign = CookieUtils.getCookie(request,"time_m_sid");
        if(StringUtils.isBlank(loginSign)){
            response.sendRedirect(request.getContextPath()+"/manager/main/to-login");
        }
        return true;
    }
}
