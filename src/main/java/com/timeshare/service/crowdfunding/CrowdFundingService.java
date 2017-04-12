package com.timeshare.service.crowdfunding;

import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.crowdfunding.CrowdFunding;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface CrowdFundingService {
    String saveCrowdFunding(CrowdFunding crowdFunding);
    List<CrowdFunding> findCrowdFundingByOwner(String userId,int startIndex, int loadSize);
    int findCrowdFundingByOwnerCount(String userId);
}
