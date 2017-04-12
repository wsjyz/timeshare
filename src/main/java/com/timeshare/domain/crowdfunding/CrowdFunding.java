package com.timeshare.domain.crowdfunding;

import com.timeshare.domain.BaseDomain;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.Item;
import com.timeshare.domain.annotation.Column;
import com.timeshare.domain.annotation.Table;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class CrowdFunding extends BaseDomain {
    //主键
    private String  crowdfundingId;
    //项目名称
    private String projectName;
    //项目开始时间
    private String curriculumStartTime;
    //项目结束时间
    private String curriculumEndTime;
    //主办城市
    private String sponsorCity;
    //详细描述
    private String detail;
    //费用类型 （费用均摊，一口价）
    private String costType;
    //总费用
    private BigDecimal costTotal;
    //最少人数
    private int minPeoples;
    //最多人数
    private int maxPeoples;
    //预约费用
    private BigDecimal reservationCost;
    //众筹封面
    private String crowdfundingIndexImgPath;
    //是否在轮播图展示
    private String isShow;
    //众筹状态 （草稿、已发布、已下架）
    private String crowdfundingStatus;
    //下架原因
    private String offShelveReason;
    //创建时间
    private String createTime;

    public String getCrowdfundingId() {
        return crowdfundingId;
    }

    public void setCrowdfundingId(String crowdfundingId) {
        this.crowdfundingId = crowdfundingId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCurriculumStartTime() {
        return curriculumStartTime;
    }

    public void setCurriculumStartTime(String curriculumStartTime) {
        this.curriculumStartTime = curriculumStartTime;
    }

    public String getCurriculumEndTime() {
        return curriculumEndTime;
    }

    public void setCurriculumEndTime(String curriculumEndTime) {
        this.curriculumEndTime = curriculumEndTime;
    }

    public String getSponsorCity() {
        return sponsorCity;
    }

    public void setSponsorCity(String sponsorCity) {
        this.sponsorCity = sponsorCity;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public BigDecimal getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(BigDecimal costTotal) {
        this.costTotal = costTotal;
    }

    public int getMinPeoples() {
        return minPeoples;
    }

    public void setMinPeoples(int minPeoples) {
        this.minPeoples = minPeoples;
    }

    public int getMaxPeoples() {
        return maxPeoples;
    }

    public void setMaxPeoples(int maxPeoples) {
        this.maxPeoples = maxPeoples;
    }

    public BigDecimal getReservationCost() {
        return reservationCost;
    }

    public void setReservationCost(BigDecimal reservationCost) {
        this.reservationCost = reservationCost;
    }

    public String getCrowdfundingIndexImgPath() {
        return crowdfundingIndexImgPath;
    }

    public void setCrowdfundingIndexImgPath(String crowdfundingIndexImgPath) {
        this.crowdfundingIndexImgPath = crowdfundingIndexImgPath;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getCrowdfundingStatus() {
        return crowdfundingStatus;
    }

    public void setCrowdfundingStatus(String crowdfundingStatus) {
        this.crowdfundingStatus = crowdfundingStatus;
    }

    public String getOffShelveReason() {
        return offShelveReason;
    }

    public void setOffShelveReason(String offShelveReason) {
        this.offShelveReason = offShelveReason;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
