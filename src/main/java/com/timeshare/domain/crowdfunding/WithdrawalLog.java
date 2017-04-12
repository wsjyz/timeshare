package com.timeshare.domain.crowdfunding;

import com.timeshare.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;


public class WithdrawalLog extends BaseDomain {
    //'主键'
    private String withdrawal_log_id;
    //'提现金额'
    private BigDecimal withdrawal_cash;
    //'提现状态'
    private String withdrawal_status;
    //'提现时间'
    private String withdrawal_time;
    //'失败原因'
    private String fail_msg;


    public String getWithdrawal_log_id() {
        return withdrawal_log_id;
    }

    public void setWithdrawal_log_id(String withdrawal_log_id) {
        this.withdrawal_log_id = withdrawal_log_id;
    }

    public BigDecimal getWithdrawal_cash() {
        return withdrawal_cash;
    }

    public void setWithdrawal_cash(BigDecimal withdrawal_cash) {
        this.withdrawal_cash = withdrawal_cash;
    }

    public String getWithdrawal_status() {
        return withdrawal_status;
    }

    public void setWithdrawal_status(String withdrawal_status) {
        this.withdrawal_status = withdrawal_status;
    }

    public String getWithdrawal_time() {
        return withdrawal_time;
    }

    public void setWithdrawal_time(String withdrawal_time) {
        this.withdrawal_time = withdrawal_time;
    }

    public String getFail_msg() {
        return fail_msg;
    }

    public void setFail_msg(String fail_msg) {
        this.fail_msg = fail_msg;
    }

}
