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
        Item item = itemService.findItemByItemId(order.getItemId());
        switch (order.getOrderStatus()){
            case "BEGIN":

                order.setPrice(item.getPrice());
                order.setItemTitle(item.getTitle());
                order.setOrderUserId(item.getUserId());

                Remind remind = new Remind();
                remind.setObjId(item.getItemId());
                remind.setRemindType(Contants.REMIND_TYPE.ORDER.toString());
                remind.setToUserId(item.getUserId());
                remind.setUserId(item.getUserId());

                remindService.saveRemind(remind);
            break;
            case "SELLER_APPLY":

                Remind sellerApplyRemind = new Remind();
                sellerApplyRemind.setObjId(item.getItemId());
                sellerApplyRemind.setRemindType(Contants.REMIND_TYPE.ORDER.toString());
                sellerApplyRemind.setToUserId(order.getUserId());
                sellerApplyRemind.setUserId(item.getUserId());
                break;
        }



        return orderDAO.saveOrder(order);
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

