package com.timeshare.controller;


import com.timeshare.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by user on 2016/6/28.
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController {

    @Autowired
    ItemService itemService;

    @RequestMapping(value = "/to-index")
    public String toIndex() {
        return "index";
    }

}
