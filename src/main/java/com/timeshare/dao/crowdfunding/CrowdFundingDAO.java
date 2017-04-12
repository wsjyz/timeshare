package com.timeshare.dao.crowdfunding;

import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.crowdfunding.CrowdFunding;

import java.util.List;


public interface CrowdFundingDAO {

    String saveCrowdFunding(CrowdFunding crowdFunding);
    List<CrowdFunding> findCrowdFundingByOwner(String userId,int startIndex, int loadSize);
    int findCrowdFundingByOwnerCount(String userId);
}
