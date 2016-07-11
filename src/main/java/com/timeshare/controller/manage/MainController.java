package com.timeshare.controller.manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping(value = "/to-index")
    public String toIndex() {

        return "manager/index";
    }
}
