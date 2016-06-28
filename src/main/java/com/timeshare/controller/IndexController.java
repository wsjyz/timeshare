package com.timeshare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by user on 2016/6/28.
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController {

    @RequestMapping(value = "/to-index")
    public String toIndex() {
        return "index";
    }
}
