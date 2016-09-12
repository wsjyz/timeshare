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
    @RequestMapping(value = "/to-buyer-confirm/")
    public String toByerConfirm(HttpServletRequest request,Model model) {

        String code = request.getParameter("code");
        String orderId = request.getParameter("state");
        WeixinOauth weixinOauth = new WeixinOauth();
        String openId = weixinOauth.obtainOpenId(code);
        ItemOrder order = orderService.findOrderByOrderId(orderId);
        remindService.deleteRemindByObjIdAndUserId(orderId,order.getUserId());
        request.setAttribute("order",order);
        request.setAttribute("openId",openId);
        WxPayConfigBean config = new WxPayConfigBean();
        SortedMap parameters = new TreeMap<>();

        config.setAppid(Contants.APPID);
        parameters.put("appid",Contants.APPID);

        config.setMch_id(Contants.MCHID);
        parameters.put("mch_id",Contants.MCHID);

        String noceStr = CommonStringUtils.genPK();
        config.setNonce_str(noceStr);
        parameters.put("nonce_str",noceStr);

        String bodyStr = order.getItemTitle() + "|" + order.getOrderUserName();
        config.setBody(bodyStr);
        parameters.put("body",bodyStr);

        String outTradeNo = CommonStringUtils.gen18RandomNumber();
        config.setOut_trade_no(outTradeNo);
        parameters.put("out_trade_no",outTradeNo);
        order.setWxTradeNo(outTradeNo);

        int fenPrice = (order.getPrice().multiply(new BigDecimal(100))).intValue();
        System.out.println(" 价格为 "+fenPrice);
        config.setTotal_fee(fenPrice);
        parameters.put("total_fee",fenPrice);

        //ip sbwx
        config.setSpbill_create_ip("123.0.1.2");
        parameters.put("spbill_create_ip","123.0.1.2");

        config.setTrade_type("JSAPI");
        parameters.put("trade_type","JSAPI");

        config.setNotify_url("http://jk.zhangqidong.cn/time/wxPay/notify-url");
        parameters.put("notify_url","http://jk.zhangqidong.cn/time/wxPay/notify-url");

        config.setOpenid(openId);
        parameters.put("openid",openId);

        parameters.put("key",Contants.KEY);

        String signStr = WxUtils.createSign(parameters,Contants.KEY);
        config.setSign(signStr);

        XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xs.processAnnotations(config.getClass());
        String xml = xs.toXML(config);
        System.out.println("xml is "+xml);

        HTTPSClient client = new HTTPSClient();
        client.setSERVER_HOST_URL(UNIFIEDORDER_URL);
        client.setServiceUri("unifiedorder");
        try {
            client.setBodyParams(new String(xml.getBytes("UTF-8"),"ISO-8859-1"));//狗日的微信，神经病
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String response = client.request();
        System.out.println("response is "+response);


        WxResponseBean responseBean = new WxResponseBean();
        xs.processAnnotations(responseBean.getClass());
        responseBean = (WxResponseBean)xs.fromXML(response);
        String prepayId = "";
        String payStatus = "NOTPAY";
        String jsApiParams = "\'\'";
        if(responseBean.getReturn_code() != null && responseBean.getReturn_code().equals("SUCCESS")){

            if(responseBean.getResult_code() != null && responseBean.getResult_code().equals("FAIL")){
                if(responseBean.getErr_code().equals("ORDERPAID")){//已支付过了
                    payStatus = "ORDERPAID";
                }

            }else if(responseBean.getResult_code() != null && responseBean.getResult_code().equals("SUCCESS")){
                prepayId = responseBean.getPrepay_id();
                SortedMap signMap = new TreeMap<>();
                signMap.put("appId",Contants.APPID);
                String timestampStr = System.currentTimeMillis()+"";
                signMap.put("timeStamp",timestampStr);
                String randomStr = CommonStringUtils.genPK();
                signMap.put("nonceStr",randomStr);
                signMap.put("package","prepay_id="+prepayId);
                signMap.put("signType","MD5");

                String paySign = WxUtils.createSign(signMap,Contants.KEY);
                signMap.put("paySign",paySign);
                jsApiParams = JSON.toJSONString(signMap);
                System.out.println("jsApiParams "+jsApiParams);

            }

        }
        model.addAttribute("jsApiParams",jsApiParams);
        model.addAttribute("payStatus",payStatus);
        model.addAttribute("order",order);
        return "appointment/buyerConfirm";

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
