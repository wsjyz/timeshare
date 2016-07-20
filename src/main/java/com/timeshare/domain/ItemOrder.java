package com.timeshare.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adam on 2016/6/11.
 */
public class ItemOrder extends BaseDomain {

    private String orderId;
    private String wxTradeNo;
    private String itemId;
    private String orderUserId;
    private String orderUserName;
    private String orderProblem;
    private String orderUserDescription;
    private String suggestAppointmentTime;
    private String finalAppointmentTime;
    private String sellerPhone;
    private BigDecimal price;
    private String payType;
    private BigDecimal paidMoney;
    private String orderStatus;
    private String itemTitle;
    private String sellerFinish;
    private String buyerFinish;

    //传值用
    private String optUserType;//seller,buyer
    private String orderFeedBack;

    public String getWxTradeNo() {
        return wxTradeNo;
    }

    public void setWxTradeNo(String wxTradeNo) {
        this.wxTradeNo = wxTradeNo;
    }

    public String getOrderFeedBack() {
        return orderFeedBack;
    }

    public void setOrderFeedBack(String orderFeedBack) {
        this.orderFeedBack = orderFeedBack;
    }

    public String getSellerFinish() {
        return sellerFinish;
    }

    public void setSellerFinish(String sellerFinish) {
        this.sellerFinish = sellerFinish;
    }

    public String getBuyerFinish() {
        return buyerFinish;
    }

    public void setBuyerFinish(String buyerFinish) {
        this.buyerFinish = buyerFinish;
    }

    public String getOptUserType() {
        return optUserType;
    }

    public void setOptUserType(String optUserType) {
        this.optUserType = optUserType;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(BigDecimal paidMoney) {
        this.paidMoney = paidMoney;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(String orderUserId) {
        this.orderUserId = orderUserId;
    }

    public String getOrderUserName() {
        return orderUserName;
    }

    public void setOrderUserName(String orderUserName) {
        this.orderUserName = orderUserName;
    }

    public String getOrderProblem() {
        return orderProblem;
    }

    public void setOrderProblem(String orderProblem) {
        this.orderProblem = orderProblem;
    }

    public String getOrderUserDescription() {
        return orderUserDescription;
    }

    public void setOrderUserDescription(String orderUserDescription) {
        this.orderUserDescription = orderUserDescription;
    }

    public String getSuggestAppointmentTime() {
        return suggestAppointmentTime;
    }

    public void setSuggestAppointmentTime(String suggestAppointmentTime) {
        this.suggestAppointmentTime = suggestAppointmentTime;
    }

    public String getFinalAppointmentTime() {
        return finalAppointmentTime;
    }

    public void setFinalAppointmentTime(String finalAppointmentTime) {
        this.finalAppointmentTime = finalAppointmentTime;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
