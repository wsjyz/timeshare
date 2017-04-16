package com.timeshare.domain.assembly;

import com.timeshare.domain.BaseDomain;
import com.timeshare.domain.ImageObj;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by user on 2017/3/23.
 * 活动
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
    private String isOnApply;
    //联系人手机号
    private String phoneNumber;
    //参加人数
    private int attentCount;
    //评论人数
    private int commentCount;

    //是否出现在首页轮播
    private String isOnIndex;
    //显示哪些报名设置
    private String showApplyProblem;
    private int browseTimes;
    private String userId;

    private String userName;
    private String userImg;
    private String cost;
    private String titleImg;
    private List<Fee> feeList;
    private List<Comment> commentList;
    private List<String> contentImgList;
    private String createTime;
    private String consultationImg;
    private String resultContent;
    private String status;
    private String quota;

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getQuota() {
        return quota;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }
    public String getResultContent() {
        return resultContent;
    }
    public String getConsultationImg() {
        return consultationImg;
    }

    public void setConsultationImg(String consultationImg) {
        this.consultationImg = consultationImg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setContentImgList(List<String> contentImgList) {
        this.contentImgList = contentImgList;
    }

    public List<String> getContentImgList() {
        return contentImgList;
    }

    public void setBrowseTimes(int browseTimes) {
        this.browseTimes = browseTimes;
    }

    public int getBrowseTimes() {
        return browseTimes;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setFeeList(List<Fee> feeList) {
        this.feeList = feeList;
    }

    public List<Fee> getFeeList() {
        return feeList;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public String getShowApplyProblem() {
        return showApplyProblem;
    }

    public void setShowApplyProblem(String showApplyProblem) {
        this.showApplyProblem = showApplyProblem;
    }

    public void setIsOnApply(String isOnApply) {
        this.isOnApply = isOnApply;
    }

    public String getIsOnApply() {
        return isOnApply;
    }

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

    public String getStartTime()
    {

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
