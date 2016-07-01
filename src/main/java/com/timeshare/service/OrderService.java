package com.timeshare.service;

import com.timeshare.domain.ItemOrder;
import com.timeshare.domain.OpenPage;

import java.util.List;

/**
 * Created by adam on 2016/6/11.
 */
public interface OrderService {

    String saveOrder(ItemOrder info);

    String modifyOrder(ItemOrder itemOrder);

    ItemOrder findOrderByOrderId(String OrderId);

    String deleteById(String OrderId);

    OpenPage<ItemOrder> findOrderPage(String mobile, String nickName, OpenPage page);

    List<ItemOrder> findItemPage(ItemOrder order, int startIndex, int loadSize);

}
