package com.timeshare.domain.crowdfunding;

import com.timeshare.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;


public class WithdrawalLog extends BaseDomain {
    //'主键'
    private String withdrawalLogId;
    //'提现金额'
    private BigDecimal withdrawalCash;
    //'提现状态'
    private String withdrawalStatus;
    //'提现时间'
    private String withdrawalTime;
    //'支付平台返回原因'
    private String replyMsg;
    //商户提现交易号
    private String withdrawalTradeNo;




    public String getWithdrawalTradeNo() {
        return withdrawalTradeNo;
    }

    public void setWithdrawalTradeNo(String withdrawalTradeNo) {
        this.withdrawalTradeNo = withdrawalTradeNo;
    }

    public String getWithdrawalLogId() {
        return withdrawalLogId;
    }

    public void setWithdrawalLogId(String withdrawalLogId) {
        this.withdrawalLogId = withdrawalLogId;
    }

    public BigDecimal getWithdrawalCash() {
        return withdrawalCash;
    }

    public void setWithdrawalCash(BigDecimal withdrawalCash) {
        this.withdrawalCash = withdrawalCash;
    }

    public String getWithdrawalStatus() {
        return withdrawalStatus;
    }

    public void setWithdrawalStatus(String withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }

    public String getWithdrawalTime() {
        return withdrawalTime;
    }

    public void setWithdrawalTime(String withdrawalTime) {
        this.withdrawalTime = withdrawalTime;
    }

    public String getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(String replyMsg) {
        this.replyMsg = replyMsg;
    }
}
