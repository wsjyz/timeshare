package com.timeshare.domain;

import java.math.BigDecimal;

/**
 * Created by user on 2016/9/22.
 */
public class Bid extends BaseDomain{

    private String bidId;
    private String title;
    private String content;
    private BigDecimal price;
    private String endTime;
    private String bidStatus;
    private String canAudit;
    private int clickRate;
    private int submitCount;
    private BigDecimal score;
    private String bidCatalog;
    private String stopReason;

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

    public String getBidCatalog() {
        return bidCatalog;
    }

    public void setBidCatalog(String bidCatalog) {
        this.bidCatalog = bidCatalog;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getCanAudit() {
        return canAudit;
    }

    public void setCanAudit(String canAudit) {
        this.canAudit = canAudit;
    }

    public int getClickRate() {
        return clickRate;
    }

    public void setClickRate(int clickRate) {
        this.clickRate = clickRate;
    }

    public int getSubmitCount() {
        return submitCount;
    }

    public void setSubmitCount(int submitCount) {
        this.submitCount = submitCount;
    }
}
