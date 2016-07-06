package com.timeshare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by user on 2016/7/5.
 */
@Controller
@RequestMapping(value = "/wxPay")
public class WxPayController {

    @RequestMapping(value = "/pub")
    public String pub() {
        return "appointment/begin";
    }

    @RequestMapping(value = "/t")
    public String test() {
        return "appointment/begin";
    }

    @ResponseBody
    @RequestMapping(value = "/notify-url")
    public String notifyUrl() {
        return "fuck";
    }
}
