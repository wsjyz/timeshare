package com.timeshare.controller;

import com.alibaba.fastjson.JSON;
import com.timeshare.domain.Item;
import com.timeshare.domain.SystemMessage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.ItemService;
import com.timeshare.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/6/21.
 */
@Controller
@RequestMapping(value = "/item")
public class ItemController extends BaseController{

    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/to-add")
    public String toAdd(String itemJson,ModelMap modelMap) {
        if(StringUtils.isNotBlank(itemJson)){
            try {
                itemJson = URLDecoder.decode(itemJson,"UTF-8");
                Item item = JSON.parseObject(itemJson,Item.class);
                modelMap.addAttribute("item", item);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return "additem";
    }

    @RequestMapping(value = "/to-list")
    public String itemList() {
        return "selltime";
    }

    @RequestMapping(value = "/to-view/{itemId}/{userId}")
    public String toView(@PathVariable String itemId,@PathVariable String userId,Model model) {
        model.addAttribute("itemId",itemId);
        model.addAttribute("userId",userId);
        return "iteminfo";
    }

    @RequestMapping(value = "/get-item")
    @ResponseBody
    public ItemDTO getItem(@RequestParam String itemId,@RequestParam String userId) {
        Item item = new Item();
        if(StringUtils.isNotBlank(itemId)){
            item = itemService.findItemByItemId(itemId);
        }
        UserInfo userInfo = userService.findUserByUserId(userId);
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItem(item);
        itemDTO.setUserInfo(userInfo);
        return itemDTO;

    }


    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Item> findItemList(@RequestParam int startIndex, @RequestParam int loadSize) {
        List<Item> itemList = new ArrayList<Item>();
        Item parms = new Item();
        parms.setUserId("admin");
        itemList = itemService.findItemPage(parms,startIndex,loadSize);
        return itemList;
    }

    @RequestMapping(value = "/list-by-condition")
    @ResponseBody
    public List<ItemDTO> listByCondition(@RequestParam String condition ,@RequestParam int startIndex, @RequestParam int loadSize) {
        List<ItemDTO> itemList = itemService.findSellItemListByCondition(condition,startIndex,loadSize);
        return itemList;
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

    @RequestMapping(value = "/preview")
    public String preview(Item item, ModelMap modelMap,@CookieValue(value="user-id", defaultValue="") String userId) {
        modelMap.addAttribute("item",item);
        UserInfo userInfo = getCurrentUser(userId);
        modelMap.addAttribute("user",userInfo);
        String itemJson = JSON.toJSONString(item);
        try {
            modelMap.addAttribute("itemJson", URLEncoder.encode(itemJson,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "previewitem";
    }
}
