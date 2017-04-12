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
    private Date curriculumStartTime;
    //项目结束时间
    private Date curriculumEndTime;
    //主办城市
    private String sponsorCity;
    //详细描述
    private String detail;
    //费用类型 （费用均摊，一口价）
    private String costType;
    //总费用
    private BigDecimal costTotal;
    //最少人数
    private Integer minPeoples;
    //最多人数
    private Integer maxPeoples;
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
     private Date createTime;
}
