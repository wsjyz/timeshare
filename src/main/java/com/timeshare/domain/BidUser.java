package com.timeshare.domain;


import java.math.BigDecimal;

/**
 * Created by user on 2016/9/22.
 */
public class BidUser extends BaseDomain {

    private String bidUserId;
    private String bidId;
    private String winTheBid;
    private BigDecimal incomeFee;
    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getBidUserId() {
        return bidUserId;
    }

    public void setBidUserId(String bidUserId) {
        this.bidUserId = bidUserId;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getWinTheBid() {
        return winTheBid;
    }

    public void setWinTheBid(String winTheBid) {
        this.winTheBid = winTheBid;
    }

    public BigDecimal getIncomeFee() {
        return incomeFee;
    }

    public void setIncomeFee(BigDecimal incomeFee) {
        this.incomeFee = incomeFee;
    }
}
