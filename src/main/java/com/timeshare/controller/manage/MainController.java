package com.timeshare.controller.manage;

import com.timeshare.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by user on 2016/7/11.
 */
@Controller
@RequestMapping(value = "manager/main")
public class MainController {

    @RequestMapping(value = "/to-login")
    public String toLogin() {
        return "manager/login";
    }
    @ResponseBody
    @RequestMapping(value = "/login")
    public String login(String username, String password, HttpServletResponse response) {
        String result = "failed";
        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
            if(username.equals("admin") && password.equals("xHsk001")){
                result = "success";
                CookieUtils.setCookie(response,"time_m_sid","1",60*60*24);
            }else{
                result = "account_not_correct";
            }
        }
        return result;
    }

    @RequestMapping(value = "/to-index")
    public String toIndex() {

        return "manager/index";
    }

    @ResponseBody
    @RequestMapping(value = "/test")
    public String test() {
        System.out.println("ok");
        return "ok";
    }

}
