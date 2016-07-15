package com.timeshare.controller;

import com.timeshare.domain.Item;
import com.timeshare.domain.UserInfo;

/**
 * Created by user on 2016/6/23.
 */
public class ItemDTO {

    private Item item;
    private UserInfo userInfo;
    private String imgPath;//ITEM_SHOW_IMG
    private String headImgPath;
    private String imgType;

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getHeadImgPath() {
        return headImgPath;
    }

    public void setHeadImgPath(String headImgPath) {
        this.headImgPath = headImgPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

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
