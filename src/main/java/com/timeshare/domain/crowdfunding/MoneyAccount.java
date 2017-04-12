package com.timeshare.domain.crowdfunding;

import com.timeshare.domain.BaseDomain;

import java.math.BigDecimal;


public class MoneyAccount extends BaseDomain {
    //'主键'
    private String moneyAccountId;
    //'可提现金额'
    private BigDecimal cashWithdrawalAmount;
    //'已提现金额'
    private BigDecimal cashRaisedAmount;
    //'月提现次数'
    private Integer monthWithdrawalNumber;


    public String getMoneyAccountId() {
        return moneyAccountId;
    }

    public void setMoneyAccountId(String moneyAccountId) {
        this.moneyAccountId = moneyAccountId;
    }

    public BigDecimal getCashWithdrawalAmount() {
        return cashWithdrawalAmount;
    }

    public void setCashWithdrawalAmount(BigDecimal cashWithdrawalAmount) {
        this.cashWithdrawalAmount = cashWithdrawalAmount;
    }

    public BigDecimal getCashRaisedAmount() {
        return cashRaisedAmount;
    }

    public void setCashRaisedAmount(BigDecimal cashRaisedAmount) {
        this.cashRaisedAmount = cashRaisedAmount;
    }

    public Integer getMonthWithdrawalNumber() {
        return monthWithdrawalNumber;
    }

    public void setMonthWithdrawalNumber(Integer monthWithdrawalNumber) {
        this.monthWithdrawalNumber = monthWithdrawalNumber;
    }

}
