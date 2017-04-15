package com.timeshare.service.impl.crowdfunding;

import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.timeshare.dao.ImageObjDAO;
import com.timeshare.dao.assembly.CommentDAO;
import com.timeshare.dao.crowdfunding.CrowdFundingDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.CommentService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.service.crowdfunding.EnrollService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
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
    EnrollService enrollService;

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

    @Override
    public CrowdFunding findCrowdFundingById(String crowdFundingById) {
        if(StringUtils.isNotBlank(crowdFundingById)){
            List<CrowdFunding> crowdFundingList=crowdFundingDAO.findCrowdFundingById(crowdFundingById);
            if (crowdFundingList!=null && crowdFundingList.size()>0){
                return crowdFundingList.get(0);
            }
        }
        return null;
    }
    //众筹首页集合
    public List<CrowdFunding> findCrowdFundingToIndex(int startIndex, int loadSize){
        return crowdFundingDAO.findCrowdFundingToIndex(startIndex,loadSize);
    }
}
