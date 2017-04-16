package com.timeshare.service.crowdfunding;

import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.crowdfunding.CrowdFunding;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface CrowdFundingService {
    //保存众筹项目
    String saveCrowdFunding(CrowdFunding crowdFunding);
    //我发起的众筹
    List<CrowdFunding> findCrowdFundingByOwner(String userId,int startIndex, int loadSize);
    //我发起的众筹-总计数
    int findCrowdFundingByOwnerCount(String userId);
    //获取某个众筹项目
    CrowdFunding findCrowdFundingById(String crowdFundingById);
    //众筹首页集合
    public List<CrowdFunding> findCrowdFundingToIndex(int startIndex, int loadSize) ;
    //众筹详情页
    public CrowdFunding findCrowdFundingDetailByCrowdfundingId(String crowdfundingId);
    //我发起的众筹
    public List<CrowdFunding> findCrowdFundingToMyCrowdFunding(int startIndex, int loadSize,String userId) ;
    //修改
    public String modifyEnroll(CrowdFunding crowdFunding) ;
    //强行下架
    public String crowdFundingToShelveByCrowdfundingId(String crowdfundingId,String offShelveReason) ;
    //获取众筹对象用于执行支付
    public CrowdFunding findCrowdFundingToPay(String crowdfundingId);
    //项目名称是否存在
    public Boolean crowdFundingPrjectNameIsExisting(String crowdFundingPrjectName) ;
}
