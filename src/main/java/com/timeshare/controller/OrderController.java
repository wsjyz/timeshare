package com.timeshare.controller;


import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.timeshare.domain.*;
import com.timeshare.service.*;
import com.timeshare.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    RemindService remindService;
    @Autowired
    FeedbackService feedbackService;

    public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/";

    @RequestMapping(value = "/to-start/{itemId}")
    public String toStart(@PathVariable String itemId,Model model) {
        model.addAttribute("itemId",itemId);

        Item item = itemService.findItemByItemId(itemId);
        model.addAttribute("userId",item.getUserId());
        return "appointment/begin";
    }
    @RequestMapping(value = "/fix-buyer-order/{orderId}")
    public String toFixBuyerOrder(@PathVariable String orderId,Model model,@CookieValue(value="time_sid", defaultValue="") String userId) {

        ItemOrder order = orderService.findOrderByOrderId(orderId);
        remindService.deleteRemindByObjIdAndUserId(orderId,userId);
        Feedback sellerFeedback = null;//对方的评价
        String toStr = "";
        switch (order.getOrderStatus()){
            case "BEGIN":
                toStr = "appointment/begin";
                break;
            case "SELLER_APPLY":
                toStr = "appointment/buyerConfirm";
                break;
            case "BUYER_CONFIRM":

                String canFinish = "false";
                if(new Date().after(CommonStringUtils.stringToDate(
                        order.getFinalAppointmentTime()))){
                    canFinish = "true";
                }
                model.addAttribute("canFinish",canFinish);
                toStr = "appointment/buyerFinish";
                break;
            case "BUYLLER_FINISH":
                Feedback feedback = feedbackService.findFeedBackByOrderId(order.getUserId(),orderId);
                sellerFeedback = feedbackService.findFeedBackByOrderId(order.getOrderUserId(),orderId);
                model.addAttribute("feedback",feedback);
                model.addAttribute("canFinish","false");
                model.addAttribute("sellerFeedback",sellerFeedback);
                toStr = "appointment/buyerFinish";
                break;
            case "SELLER_FINISH":
                String canFinish1 = "false";
                if(new Date().after(CommonStringUtils.stringToDate(
                        order.getFinalAppointmentTime()))){
                    canFinish1 = "true";
                }
                Feedback feedback1 = feedbackService.findFeedBackByOrderId(order.getUserId(),orderId);
                sellerFeedback = feedbackService.findFeedBackByOrderId(order.getOrderUserId(),orderId);
                model.addAttribute("sellerFeedback",sellerFeedback);
                model.addAttribute("feedback",feedback1);
                model.addAttribute("canFinish",canFinish1);
                toStr = "appointment/buyerFinish";
                break;
            case "FINISH":
                Feedback feedback2 = feedbackService.findFeedBackByOrderId(order.getUserId(),orderId);
                sellerFeedback = feedbackService.findFeedBackByOrderId(order.getOrderUserId(),orderId);
                model.addAttribute("sellerFeedback",sellerFeedback);
                model.addAttribute("feedback",feedback2);
                model.addAttribute("canFinish","false");
                toStr = "appointment/buyerFinish";
                break;


        }
        model.addAttribute("order",order);
        return toStr;
    }

    @RequestMapping(value = "/fix-seller-order/{orderId}")
    public String toFixSellerOrder(@PathVariable String orderId,Model model,@CookieValue(value="time_sid", defaultValue="") String userId) {

        ItemOrder order = orderService.findOrderByOrderId(orderId);
        remindService.deleteRemindByObjIdAndUserId(orderId,userId);
        String toStr = "";
        Feedback buyerFeedback = null;
        switch (order.getOrderStatus()){
            case "BEGIN":
                UserInfo seller = userService.findUserByUserId(userId);
                if(seller != null){
                    model.addAttribute("mobile",seller.getMobile());
                }
                toStr = "appointment/sellerApply";
                break;
            case "SELLER_APPLY":
                toStr = "appointment/sellerApply";
                break;
            case "BUYER_CONFIRM":

                String canFinish = "false";
                if(new Date().after(CommonStringUtils.stringToDate(
                        order.getFinalAppointmentTime()))){
                    canFinish = "true";
                }
                model.addAttribute("canFinish",canFinish);
                toStr = "appointment/sellerFinish";
                break;
            case "SELLER_FINISH":
                Feedback feedback = feedbackService.findFeedBackByOrderId(order.getOrderUserId(),orderId);
                buyerFeedback = feedbackService.findFeedBackByOrderId(order.getUserId(),orderId);
                model.addAttribute("buyerFeedback",buyerFeedback);
                model.addAttribute("feedback",feedback);
                model.addAttribute("canFinish","false");
                toStr = "appointment/sellerFinish";
                break;
            case "BUYLLER_FINISH":

                String canFinish1 = "false";
                if(new Date().after(CommonStringUtils.stringToDate(
                        order.getFinalAppointmentTime()))){
                    canFinish1 = "true";
                }
                Feedback feedback1 = feedbackService.findFeedBackByOrderId(order.getOrderUserId(),orderId);
                buyerFeedback = feedbackService.findFeedBackByOrderId(order.getUserId(),orderId);
                model.addAttribute("buyerFeedback",buyerFeedback);
                model.addAttribute("feedback",feedback1);
                model.addAttribute("canFinish",canFinish1);
                toStr = "appointment/sellerFinish";
                break;
            case "FINISH":
                Feedback feedback2 = feedbackService.findFeedBackByOrderId(order.getOrderUserId(),orderId);
                buyerFeedback = feedbackService.findFeedBackByOrderId(order.getUserId(),orderId);
                model.addAttribute("buyerFeedback",buyerFeedback);
                model.addAttribute("feedback",feedback2);
                model.addAttribute("canFinish","false");
                toStr = "appointment/sellerFinish";
                break;



        }
        model.addAttribute("order",order);
        return toStr;
    }
    @RequestMapping(value = "/to-buyer-confirm/{orderId}")
    public String toByerConfirm(HttpServletRequest request,@PathVariable String orderId) {

        ItemOrder order = orderService.findOrderByOrderId(orderId);
        remindService.deleteRemindByObjIdAndUserId(orderId,order.getUserId());
        request.setAttribute("order",order);

        return "appointment/buyerConfirm";

    }
    @RequestMapping(value = "/to-pay-for-confirm")
    public String toPayForConfirm(HttpServletRequest request,RedirectAttributes attr) {

        String code = request.getParameter("code");
        String orderId = request.getParameter("state");
        ItemOrder order = orderService.findOrderByOrderId(orderId);

        String payMessageTitle = "您在邂逅拾刻的发飙款项："+order.getItemTitle() ;
        String jsApiParams = WxPayUtils.userPayToCorp(code,payMessageTitle,order.getPrice());
        attr.addAttribute("jsApiParams",jsApiParams);
        attr.addAttribute("payTip","你确定要支付"+order.getPrice()+"元吗");
        attr.addAttribute("okUrl",request.getContextPath()+"/to-buyer-confirm/"+orderId);
        attr.addAttribute("backUrl",request.getContextPath()+"/modify-order-status?orderId="+orderId+"&bidStatus=SELLER_APPLY");

        return "redirect:/wxPay/to-pay/";
    }
    @ResponseBody
    @RequestMapping(value = "/save-async")
    public String saveAsync(ItemOrder order,Model model,@CookieValue(value="time_sid", defaultValue="") String userId) {
        String result = orderService.saveOrder(order);
        return result;
    }
    @RequestMapping(value = "/modify-order-status")
    public String modifyBidStatus(String orderId,String bidStatus,HttpServletRequest request) {
        ItemOrder order = orderService.findOrderByOrderId(orderId);
        order.setOrderStatus(bidStatus);
        orderService.saveOrder(order);
        return "redirect:"+request.getContextPath()+"/to-buyer-confirm/"+orderId;
    }

    @RequestMapping(value = "/to-seller-order-list")
    public String toSellOrderList() {

        return "appointment/sellerOrderlist";
    }

    @RequestMapping(value = "/to-buyer-order-list")
    public String toBuyerOrderList() {

        return "appointment/buyerOrderlist";
    }

    @RequestMapping(value = "/my-order-list")
    @ResponseBody
    public List<ItemOrder> findOrderList(@RequestParam String orderStatus,
                                         @RequestParam String optUserType,
                                         @RequestParam int startIndex, @RequestParam int loadSize
                                        ,@CookieValue(value="time_sid", defaultValue="") String userId) {
        List<ItemOrder> itemList = new ArrayList<>();
        ItemOrder parms = new ItemOrder();
        parms.setOrderUserId(userId);
        parms.setOrderStatus(orderStatus);
        parms.setOptUserType(optUserType);
        if(optUserType.equals(Contants.OPT_USER_TYPE.buyer.toString())){
            parms.setUserId(userId);
        }else if(optUserType.equals(Contants.OPT_USER_TYPE.seller.toString())){
            parms.setOrderUserId(userId);
        }
        itemList = orderService.findItemPage(parms,startIndex,loadSize);
        return itemList;
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public String save(ItemOrder order,Model model,@CookieValue(value="time_sid", defaultValue="") String userId) {

        SystemMessage message = new SystemMessage();
        if(order != null){
            Item item = itemService.findItemByItemId(order.getItemId());
            if(StringUtils.isNotBlank(userId) && order.getOrderStatus().equals(Contants.ORDER_STATUS.BEGIN.toString())){

                UserInfo seller = userService.findUserByUserId(item.getUserId());
                order.setOrderUserName(seller.getNickName());
                UserInfo user = getCurrentUser(userId);
                order.setUserId(user.getUserId());
                order.setCreateUserName(user.getNickName());
            }else{
                ItemOrder tempOrder = orderService.findOrderByOrderId(order.getOrderId());
                order.setUserId(tempOrder.getUserId());
                if(order.getOrderStatus().toString().equals("BUYLLER_FINISH")
                        || order.getOrderStatus().toString().equals("SELLER_FINISH")){


                    if(order.getOptUserType().equals("seller")){

                        if(StringUtils.isNotBlank(tempOrder.getBuyerFinish())//卖家
                                && tempOrder.getBuyerFinish().equals("1")){
                            order.setOrderStatus(Contants.ORDER_STATUS.FINISH.toString());
                        }else{
                            order.setOrderStatus(Contants.ORDER_STATUS.SELLER_FINISH.toString());
                        }

                        Feedback feedback = new Feedback();
                        feedback.setItemTitle(item.getTitle());
                        feedback.setItemId(item.getItemId());
                        feedback.setCreateUserName(tempOrder.getOrderUserName());
                        feedback.setContent(order.getOrderFeedBack());
                        feedback.setToUserId(order.getUserId());
                        feedback.setUserId(tempOrder.getOrderUserId());
                        feedback.setRating(order.getRating());
                        feedback.setOrderId(order.getOrderId());
                        feedbackService.saveFeedback(feedback);

                    }else{//买家

                        if(StringUtils.isNotBlank(tempOrder.getSellerFinish())
                                && tempOrder.getSellerFinish().equals("1")){
                            order.setOrderStatus(Contants.ORDER_STATUS.FINISH.toString());
                        }else{
                            order.setOrderStatus(Contants.ORDER_STATUS.BUYLLER_FINISH.toString());
                        }

                        Feedback feedback = new Feedback();
                        feedback.setItemTitle(item.getTitle());
                        feedback.setItemId(item.getItemId());
                        feedback.setCreateUserName(tempOrder.getCreateUserName());
                        feedback.setContent(order.getOrderFeedBack());
                        feedback.setToUserId(order.getOrderUserId());
                        feedback.setUserId(tempOrder.getUserId());
                        feedback.setRating(order.getRating());
                        feedback.setOrderId(order.getOrderId());
                        feedbackService.saveFeedback(feedback);
                        //更新数量
                        int useCount = item.getUseCount() + 1;
                        int totalScore = feedbackService.findItemTotalScore(item.getItemId(),item.getUserId());

                        float avgF = (float)totalScore/useCount;
                        BigDecimal avg = new BigDecimal(avgF);
                        avg.setScale(1,BigDecimal.ROUND_HALF_UP);
                        item.setScore(avg);
                        item.setUseCount(useCount);
                        itemService.modifyItem(item);
                        order.setBuyerPayed(true);

                    }
                }
            }



            String result = orderService.saveOrder(order);

            message.setMessageType(result);
            String toUserType = "";
            if(order.getOptUserType().equals("buyer")){
                toUserType = "卖家";
            }else{
                toUserType = "买家";
            }

            String messageContent = "操作成功！已经向"+toUserType+"发送短信通知！";

            if(result.equals(Contants.SUCCESS)){
                message.setContent(messageContent);
            }

        }
        model.addAttribute("message",message);
        model.addAttribute("jumpUrl","/order/to-"+order.getOptUserType()+"-order-list");
        return "info";
    }


}
