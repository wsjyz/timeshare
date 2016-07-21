package com.timeshare.domain;

/**
 * Created by adam on 2016/6/11.
 */
public class Feedback extends BaseDomain {

    private String feedBackId;

    private String toUserId;

    private String title;

    private String content;

    private String itemId;

    private String itemTitle;

    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedBackId() {
        return feedBackId;
    }

    public void setFeedBackId(String feedBackId) {
        this.feedBackId = feedBackId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
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
}
