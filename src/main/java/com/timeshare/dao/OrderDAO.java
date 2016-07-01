package com.timeshare.dao;

import com.timeshare.domain.OpenPage;
import com.timeshare.domain.ItemOrder;

import java.util.List;

/**
 * Created by adam on 2016/6/11.
 */
public interface OrderDAO {

    String saveOrder(ItemOrder info);

    String modifyOrder(ItemOrder itemOrder);

    ItemOrder findOrderByOrderId(String OrderId);

    String deleteById(String OrderId);

    OpenPage<ItemOrder> findOrderPage(String mobile, String nickName, OpenPage page);

    List<ItemOrder> findItemPage(ItemOrder order, int startIndex, int loadSize);
}
