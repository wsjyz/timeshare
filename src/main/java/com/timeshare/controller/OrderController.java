package com.timeshare.controller;


import com.timeshare.domain.Item;
import com.timeshare.domain.ItemOrder;
import com.timeshare.domain.SystemMessage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.ItemService;
import com.timeshare.service.OrderService;
import com.timeshare.service.UserService;
import com.timeshare.utils.Contants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/7/1.
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseController{

    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;

    @RequestMapping(value = "/to-start/{itemId}/{userId}")
    public String toStart(@PathVariable String itemId,@PathVariable String userId,Model model) {
        model.addAttribute("itemId",itemId);
        model.addAttribute("userId",userId);
        return "appointment/begin";
    }

    @RequestMapping(value = "/to-my-order-list")
    public String toMyOrderList() {

        return "appointment/myOrderlist";
    }

    @RequestMapping(value = "/my-order-list")
    @ResponseBody
    public List<ItemOrder> findOrderList(@RequestParam String orderStatus,@RequestParam int startIndex, @RequestParam int loadSize) {
        List<ItemOrder> itemList = new ArrayList<ItemOrder>();
        ItemOrder parms = new ItemOrder();
        parms.setOrderUserId("admin");
        parms.setOrderStatus(orderStatus);
        itemList = orderService.findItemPage(parms,startIndex,loadSize);
        return itemList;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public String save(ItemOrder order,Model model) {

        SystemMessage message = new SystemMessage();
        if(order != null){
            //TODO createuser
            order.setCreateUserName("admin");
            order.setUserId("admin");
            order.setOrderUserId("admin");
            order.setOrderStatus(Contants.ORDER_STATUS.BEGIN.toString());

            UserInfo user = getCurrentUser("admin");
            order.setOrderUserId(user.getUserId());
            order.setOrderUserName(user.getNickName());

            Item item = itemService.findItemByItemId(order.getItemId());
            order.setPrice(item.getPrice());
            order.setItemTitle(item.getTitle());

            String result = orderService.saveOrder(order);
            message.setMessageType(result);
            if(result.equals(Contants.SUCCESS)){
                message.setContent("预约成功！已经向卖家发送短信通知！");
            }
        }
        model.addAttribute("message",message);
        model.addAttribute("jumpUrl","/order/to-my-order-list");
        return "info";
    }
}
