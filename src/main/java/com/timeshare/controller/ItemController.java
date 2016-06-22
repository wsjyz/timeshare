package com.timeshare.controller;

import com.timeshare.domain.Item;
import com.timeshare.domain.SystemMessage;
import com.timeshare.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2016/6/21.
 */
@Controller
@RequestMapping(value = "/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @RequestMapping(value = "/to-add")
    public String toAdd() {
        return "additem";
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ModelAndView save(Item item) {
        ModelAndView modelAndView = new ModelAndView("info");
        SystemMessage message = new SystemMessage();
        if(item != null){
            item.setCreateUserName("admin");
            item.setUserId("admin");
            String result = itemService.saveItem(item);
            message.setMessageType(result);
            if(result.equals("success")){
                message.setContent("添加成功！");
            }
        }
        modelAndView.addObject("message",message);
        modelAndView.addObject("content","添加成功！");
        return modelAndView;
    }
}
