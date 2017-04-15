package com.timeshare.domain.assembly;

import com.timeshare.domain.BaseDomain;

/**
 * Created by user on 2017/3/24.
 * 报名的人
 */
public class Attender extends BaseDomain {

    private String attenderId;
    //用户id
    private String userId;
    //所属费用项
    private String feedId;
    private String assemblyId;
    private String createTime;
    private String userImg;
    private String userName;
    private String questionAnswer;
    private String userNameTitle;
    private String phone;
    private String email;
    private String wx;
    private String company;

    public String getUserNameTitle() {
        return userNameTitle;
    }

    public void setUserNameTitle(String userNameTitle) {
        this.userNameTitle = userNameTitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setAttenderId(String attenderId) {
        this.attenderId = attenderId;
    }

    public String getAttenderId() {
        return attenderId;
    }

    public String getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }
}
