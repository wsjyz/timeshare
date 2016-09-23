package com.timeshare.domain;

import java.math.BigDecimal;

/**
 * Created by user on 2016/9/22.
 */
public class Auditor extends BaseDomain {

    private String auditorId;
    private String bidId;
    private BigDecimal fee;

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
