package com.timeshare.domain.assembly;

import com.timeshare.domain.BaseDomain;

import java.math.BigDecimal;

/**
 * Created by user on 2017/3/24.
 * 费用项
 */
public class Fee extends BaseDomain{

    private String feeId;
    //报名费用
    private BigDecimal fee;
    private String feeTitle;
    //费用限额
    private int quota;
    //所属活动
    private String assemblyId;

    public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getFeeTitle() {
        return feeTitle;
    }

    public void setFeeTitle(String feeTitle) {
        this.feeTitle = feeTitle;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public String getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }
}
