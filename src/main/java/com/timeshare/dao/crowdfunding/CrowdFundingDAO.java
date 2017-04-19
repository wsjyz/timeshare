package com.timeshare.dao.crowdfunding;

import com.timeshare.domain.crowdfunding.CrowdFunding;

import java.util.List;


public interface CrowdFundingDAO {

    String saveCrowdFunding(CrowdFunding crowdFunding);
    List<CrowdFunding> findCrowdFundingByOwner(String userId,int startIndex, int loadSize);
    List<CrowdFunding> findCrowdFundingById(String crowdFundingById);
    int findCrowdFundingByOwnerCount(String userId);
    public List<CrowdFunding> findCrowdFundingToIndex(int startIndex, int loadSize,String crowdfundingId,String isShowFlag) ;
    public List<CrowdFunding> findCrowdFundingToMyCrowdFunding(int startIndex, int loadSize,String userId) ;
    public String modifyEnroll(CrowdFunding crowdFunding) ;
    public List<CrowdFunding> findCrowdFundingToPay(String crowdfundingId) ;
    public List<CrowdFunding> findCrowdFundingByPrjectName(String crowdFundingPrjectName) ;
    public List<CrowdFunding> editCrowdFundingByCrowdFundingId(String crowdFundingId) ;
    public String deleteCrowdFunding(String crowdFundingId) ;
    public List<CrowdFunding> findCrowdFundingDetailByCrowdfundingId(String crowdfundingId) ;
}
