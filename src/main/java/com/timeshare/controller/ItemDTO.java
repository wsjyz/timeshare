package com.timeshare.controller;

import com.timeshare.domain.Item;
import com.timeshare.domain.UserInfo;

/**
 * Created by user on 2016/6/23.
 */
public class ItemDTO {

    private Item item;
    private UserInfo userInfo;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
