package com.timeshare.domain;

/**
 * Created by adam on 2016/6/11.
 */
public class Feedback extends BaseDomain {

    private String title;

    private String content;

    private String itemId;

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
