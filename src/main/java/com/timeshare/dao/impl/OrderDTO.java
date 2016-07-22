package com.timeshare.dao.impl;

import com.timeshare.domain.ItemOrder;

/**
 * Created by user on 2016/7/13.
 */
public class OrderDTO {

    private ItemOrder order;
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public ItemOrder getOrder() {
        return order;
    }

    public void setOrder(ItemOrder order) {
        this.order = order;
    }
}
