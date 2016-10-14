package com.timeshare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    /**
     * /to-pay/ 这里最后一定要有一个/否则会出现目录未注册的提示，这是微信极其愚蠢的设计
     * 举例：如果你请求to-pay?xx=xx这样是不行的，你一定要请求to-pay/?xx=xx,在java的世界里只有SB才会这样要求
     * @param jsApiParams
     * @param payTip
     * @param backUrl
     * @param model
     * @return
     */
    @RequestMapping(value = "/to-pay/")
    public String toPay(String jsApiParams,String payTip,String backUrl,Model model) {
        model.addAttribute("jsApiParams",jsApiParams);
        model.addAttribute("payTip",payTip);
        model.addAttribute("backUrl",backUrl);
        return "pay";
    }
}
