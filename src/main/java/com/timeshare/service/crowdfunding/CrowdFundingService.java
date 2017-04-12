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
}
