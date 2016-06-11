package com.timeshare.controller;

import com.timeshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by adam on 2016/6/11.
 */
@Controller
@RequestMapping(value = "/User")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/toRegist")
    public String toLogin() {
        return "regist";
    }

}
