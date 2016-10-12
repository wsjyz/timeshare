package com.timeshare.domain;

import java.math.BigDecimal;

/**
 * Created by user on 2016/9/22.
 */
public class Auditor extends BaseDomain {

    private String auditorId;
    private String bidId;
    private int fee;
    private String wxTradeNo;

    public String getWxTradeNo() {
        return wxTradeNo;
    }

    public void setWxTradeNo(String wxTradeNo) {
        this.wxTradeNo = wxTradeNo;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
