package com.timeshare.service.impl;

import com.timeshare.dao.OrderDAO;
import com.timeshare.domain.*;
import com.timeshare.service.ItemService;
import com.timeshare.service.OrderService;
import com.timeshare.service.RemindService;
import com.timeshare.service.UserService;
import com.timeshare.utils.Contants;
import com.timeshare.utils.SmsContentBean;
import com.timeshare.utils.SmsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/7/1.
 */
@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    protected Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    OrderDAO orderDAO;
    @Autowired
    ItemService itemService;
    @Autowired
    RemindService remindService;
    @Autowired
    UserService userService;

    @Override
    public String saveOrder(ItemOrder order) {

        String result = "";

        switch (order.getOrderStatus()){
            case "BEGIN":
                Item item = itemService.findItemByItemId(order.getItemId());
                order.setPrice(item.getPrice());
                order.setItemTitle(item.getTitle());
                order.setOrderUserId(item.getUserId());

                Remind remind = new Remind();
                remind.setObjId(item.getItemId());
                remind.setRemindType(Contants.REMIND_TYPE.ORDER.toString());
                remind.setToUserId(item.getUserId());
                remind.setUserId(item.getUserId());

                remindService.saveRemind(remind);
                result = orderDAO.saveOrder(order);
            break;
            case "SELLER_APPLY":
                Item item1 = itemService.findItemByItemId(order.getItemId());
                Remind sellerApplyRemind = new Remind();
                sellerApplyRemind.setObjId(order.getOrderId());
                sellerApplyRemind.setRemindType(Contants.REMIND_TYPE.ORDER.toString());
                sellerApplyRemind.setToUserId(order.getUserId());
                sellerApplyRemind.setUserId(item1.getUserId());

                result = orderDAO.modifyOrder(order);
                break;
            case "BUYER_CONFIRM":
                Item item2 = itemService.findItemByItemId(order.getItemId());
                Remind buyConfirmRemind = new Remind();
                buyConfirmRemind.setObjId(order.getOrderId());
                buyConfirmRemind.setRemindType(Contants.REMIND_TYPE.ORDER.toString());
                buyConfirmRemind.setToUserId(item2.getUserId());
                buyConfirmRemind.setUserId(order.getUserId());

                result = orderDAO.modifyOrder(order);
                break;
            case "SELLER_FINISH":
                result = orderDAO.modifyOrder(order);
                break;
            case "BUYLLER_FINISH":
                result = orderDAO.modifyOrder(order);
                break;
            case "FINISH":
                result = orderDAO.modifyOrder(order);
                break;
        }
        if(!order.getOrderStatus().equals("FINISH")){
            sendMms(order);
        }



        return result;
    }

    @Override
    public String modifyOrder(ItemOrder itemOrder) {
        return null;
    }

    @Override
    public ItemOrder findOrderByOrderId(String orderId) {
        return orderDAO.findOrderByOrderId(orderId);
    }

    @Override
    public String deleteById(String OrderId) {
        return null;
    }

    @Override
    public OpenPage<ItemOrder> findOrderPage(String mobile, String nickName, OpenPage page) {
        return null;
    }

    @Override
    public List<ItemOrder> findItemPage(ItemOrder order, int startIndex, int loadSize) {
        return orderDAO.findItemPage(order,startIndex,loadSize);
    }

    @Override
    public BigDecimal findUsersMoneyByType(String userId, String type) {
        return orderDAO.findUsersMoneyByType(userId,type);
    }
    public void sendMms(ItemOrder order){
//        卖家收到：买家[李四]预约了您的项目，请进入服务号[邂逅时刻]查看
//        买家收到：卖家[张三]答复了您的邀请，请进入服务号[邂逅时刻]查看
//        卖家收到：买家[李四]确认了邂逅时间，具体沟通时间为[2016-12-10 12:00]，请进入服务号[邂逅时刻]查看
//        买家收到：卖家[张三]已经确认完成双方邀约，请进入服务号[邂逅时刻]查看
//        卖家收到：买家[李四]已经确认完成双方邀约，项目款项已入账，请进入服务号[邂逅时刻]查收
        UserInfo seller = userService.findUserByUserId(order.getOrderUserId());
        UserInfo buyer = userService.findUserByUserId(order.getUserId());
        SmsContentBean bean = new SmsContentBean();
        switch (order.getOrderStatus()){
            case "BEGIN":
                bean.setTemplateCode("SMS_12405376");
                bean.setToMobile(seller.getMobile());
                bean.setContent("{\"name\":\""+buyer.getNickName()+"\"}");
                System.out.println("向卖家"+seller.getMobile()+"发短信："+"买家["+buyer.getNickName()+"]预约了您的项目，请进入服务号[邂逅时刻]查看");
                break;
            case "SELLER_APPLY":
                bean.setTemplateCode("SMS_12350337");
                bean.setToMobile(buyer.getMobile());
                bean.setContent("{\"name\":\""+seller.getNickName()+"\"}");
                System.out.println("向买家"+buyer.getMobile()+"发短信："+"卖家["+seller.getNickName()+"]答复了您的邀请，请进入服务号[邂逅时刻]查看");
                break;
            case "BUYER_CONFIRM":
                bean.setTemplateCode("SMS_12410324");
                bean.setToMobile(seller.getMobile());
                SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日 HH点mm");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = sdf1.parse(order.getFinalAppointmentTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String finalAppointmentTime = sdf.format(date);
                bean.setContent("{\"name\":\""+buyer.getNickName()+"\",\"time\":\""+finalAppointmentTime+"\"}");
                System.out.println("向卖家"+seller.getMobile()+"发短信："+"买家["+buyer.getNickName()+"]确认了邂逅时间，具体沟通时间为["+finalAppointmentTime+"]，请进入服务号[邂逅时刻]查看");
                break;
            case "SELLER_FINISH":
                bean.setTemplateCode("SMS_12335356");
                bean.setToMobile(buyer.getMobile());
                bean.setContent("{\"name\":\""+seller.getNickName()+"\"}");
                System.out.println("向买家"+buyer.getMobile()+"发短信："+"卖家["+seller.getNickName()+"]已经确认完成双方邀约，请进入服务号[邂逅时刻]查看");
                break;
            case "BUYLLER_FINISH":
                bean.setTemplateCode("SMS_12360451");
                bean.setToMobile(seller.getMobile());
                bean.setContent("{\"name\":\""+buyer.getNickName()+"\"}");
                System.out.println("向卖家"+seller.getMobile()+"发短信："+"买家["+buyer.getNickName()+"]已经确认完成双方邀约，项目款项已入账，请进入服务号[邂逅时刻]查收");
                break;


        }
        String response = SmsUtils.senMessage(bean);
        if(response.indexOf("error_response") != -1){
            logger.error(response);
        }
    }
}

