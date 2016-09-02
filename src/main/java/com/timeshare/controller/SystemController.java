package com.timeshare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by user on 2016/9/1.
 */
@Controller
@RequestMapping(value = "/sys")
public class SystemController {

    @RequestMapping(value = "/to-feedback")
    public String toFeedback() {

        return "sysfeedback";
    }

    @RequestMapping(value = "/to-connect-us")
    public String toConnectUs() {

        return "sysconnectus";
    }
}
