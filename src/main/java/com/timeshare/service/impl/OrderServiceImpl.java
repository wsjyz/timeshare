package com.timeshare.service.impl;

import com.timeshare.dao.OrderDAO;
import com.timeshare.domain.Item;
import com.timeshare.domain.ItemOrder;
import com.timeshare.domain.OpenPage;
import com.timeshare.domain.Remind;
import com.timeshare.service.ItemService;
import com.timeshare.service.OrderService;
import com.timeshare.service.RemindService;
import com.timeshare.utils.Contants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/7/1.
 */
@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDAO orderDAO;
    @Autowired
    ItemService itemService;
    @Autowired
    RemindService remindService;

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
}

