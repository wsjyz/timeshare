package com.timeshare.domain.crowdfunding;

import com.timeshare.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;


public class Enroll extends BaseDomain {
    //'报名主键'
    private String enrollId;
    //'报名项目'
    private String crowdfundingId;
    //'报名者ID'
    private String enrollUserId;
    //'姓名'
    private String userName;
    //'手机'
    private String phone;
    //'公司简称'
    private String corpName;
    //'发票抬头'
    private String invoiceTitle;
    //'发票类型'
    private String invoiceType;
    //'支付状态'
    private String payStatus;
    //'支付金额'
    private BigDecimal payAmount;
    //是否已经转移到了现金账户
    private String isTransferCashAccount;
    //'商户支付交易号'
    private String payTradeNo;
    //'商户退款交易号'
    private String refundTradeNo;

    //预约的众筹项目名称
    private String projectName;
    //预约的众筹项目详情
    private String detail;
    //预约的众筹项目图片路径
    private String imageUrl;

    //所属项目最小人数 用于判断是否成团
    private String minPeoples;
    //所属项目发起人拥有者
    private String ownerUserId;

    public String getPayTradeNo() {
        return payTradeNo;
    }

    public void setPayTradeNo(String payTradeNo) {
        this.payTradeNo = payTradeNo;
    }

    public String getRefundTradeNo() {
        return refundTradeNo;
    }

    public void setRefundTradeNo(String refundTradeNo) {
        this.refundTradeNo = refundTradeNo;
    }

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId;
    }

    public String getCrowdfundingId() {
        return crowdfundingId;
    }

    public void setCrowdfundingId(String crowdfundingId) {
        this.crowdfundingId = crowdfundingId;
    }

    public String getEnrollUserId() {
        return enrollUserId;
    }

    public void setEnrollUserId(String enrollUserId) {
        this.enrollUserId = enrollUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsTransferCashAccount() {
        return isTransferCashAccount;
    }

    public void setIsTransferCashAccount(String isTransferCashAccount) {
        this.isTransferCashAccount = isTransferCashAccount;
    }

    public String getMinPeoples() {
        return minPeoples;
    }

    public void setMinPeoples(String minPeoples) {
        this.minPeoples = minPeoples;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
}
