package com.timeshare.service.impl.crowdfunding;

import com.timeshare.dao.ImageObjDAO;
import com.timeshare.dao.assembly.CommentDAO;
import com.timeshare.dao.crowdfunding.CrowdFundingDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.CommentService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("CrowdFundingService")
public class CrowdFundingServiceImpl implements CrowdFundingService {

    @Autowired
    CrowdFundingDAO crowdFundingDAO;
    @Autowired
    UserService userService;

    @Override
    public String saveCrowdFunding(CrowdFunding crowdFunding) {
        return crowdFundingDAO.saveCrowdFunding(crowdFunding);
    }

    @Override
    public List<CrowdFunding> findCrowdFundingByOwner(String userId, int startIndex, int loadSize) {
        return crowdFundingDAO.findCrowdFundingByOwner(userId,startIndex,loadSize);
    }

    @Override
    public int findCrowdFundingByOwnerCount(String userId) {
        return crowdFundingDAO.findCrowdFundingByOwnerCount(userId);
    }
}
