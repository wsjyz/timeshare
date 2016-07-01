package com.timeshare.service.impl;

import com.timeshare.dao.OrderDAO;
import com.timeshare.domain.ItemOrder;
import com.timeshare.domain.OpenPage;
import com.timeshare.service.OrderService;
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

    @Override
    public String saveOrder(ItemOrder info) {
        return orderDAO.saveOrder(info);
    }

    @Override
    public String modifyOrder(ItemOrder itemOrder) {
        return null;
    }

    @Override
    public ItemOrder findOrderByOrderId(String OrderId) {
        return null;
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
