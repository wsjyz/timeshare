package com.timeshare.domain;

import java.math.BigDecimal;

/**
 * Created by adam on 2016/6/11.
 */
public class Item extends BaseDomain{

    private String itemId;
    private String title;
    private BigDecimal price;
    private BigDecimal score;
    private String description;
    private String itemType;
    private String itemStatus;
    private int useCount;
    private int duration;
    private boolean recommend;

    //传值用
    private int remindCount;

    public int getRemindCount() {
        return remindCount;
    }

    public void setRemindCount(int remindCount) {
        this.remindCount = remindCount;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getScore() {
        if(score == null){
            return new BigDecimal(0);
        }
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }
}
