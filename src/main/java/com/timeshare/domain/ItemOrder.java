package com.timeshare.domain;

import java.math.BigDecimal;

/**
 * Created by adam on 2016/6/11.
 */
public class ItemOrder extends BaseDomain {

    private String orderId;
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
