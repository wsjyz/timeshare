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
    private String enroll_userId;
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

    public String getEnroll_userId() {
        return enroll_userId;
    }

    public void setEnroll_userId(String enroll_userId) {
        this.enroll_userId = enroll_userId;
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
}
