package com.timeshare.domain.assembly;

import com.timeshare.domain.BaseDomain;

import java.math.BigDecimal;

/**
 * Created by user on 2017/3/23.
 * 报名
 */
public class Assembly extends BaseDomain{

    private String assemblyId;
    private String title;
    private String startTime;
    private String endTime;
    private String rendezvous;
    private String description;
    private String type;
    //封面，存在ImageObj对象中

    //联系人手机号
    private String phoneNumber;
    //参加人数
    private int attentCount;
    //评论人数
    private int commentCount;

    //是否出现在首页轮播
    private String isOnIndex;

    public String getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRendezvous() {
        return rendezvous;
    }

    public void setRendezvous(String rendezvous) {
        this.rendezvous = rendezvous;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAttentCount() {
        return attentCount;
    }

    public void setAttentCount(int attentCount) {
        this.attentCount = attentCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getIsOnIndex() {
        return isOnIndex;
    }

    public void setIsOnIndex(String isOnIndex) {
        this.isOnIndex = isOnIndex;
    }
}
