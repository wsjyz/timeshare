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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    //众筹详情页
    public CrowdFunding findCrowdFundingDetailByCrowdfundingId(String crowdfundingId){
        List<CrowdFunding> crowdFundingList=crowdFundingDAO.findCrowdFundingToIndex(0,1,crowdfundingId);
        if(crowdFundingList!=null && crowdFundingList.size()>0){
            return crowdFundingList.get(0);
        }
        return null;
    }
    //众筹首页集合
    public List<CrowdFunding> findCrowdFundingToIndex(int startIndex, int loadSize){
        return crowdFundingDAO.findCrowdFundingToIndex(startIndex,loadSize,null);
    }
    //我发起的众筹
    public List<CrowdFunding> findCrowdFundingToMyCrowdFunding(int startIndex, int loadSize,String userId){
        return crowdFundingDAO.findCrowdFundingToMyCrowdFunding(startIndex,loadSize,userId);
    }

    public String modifyEnroll(CrowdFunding crowdFunding) {
        return crowdFundingDAO.modifyEnroll(crowdFunding);
    }
    //用户下架
    public String crowdFundingToShelveByCrowdfundingId(String crowdfundingId,String offShelveReason) {
        if(StringUtils.isNotBlank(crowdfundingId)){
            CrowdFunding crowdfunding=findCrowdFundingById(crowdfundingId);
            crowdfunding.setCrowdfundingStatus(Contants.CROWD_FUNDING_STATUS.OFF_SHELVE.name());
            crowdfunding.setOffShelveReason(offShelveReason);
            crowdfunding.setOptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            return crowdFundingDAO.modifyEnroll(crowdfunding);
        }
        return Contants.FAILED;
    }

    public CrowdFunding findCrowdFundingToPay(String crowdfundingId) {
        List<CrowdFunding> crowdFundingList=crowdFundingDAO.findCrowdFundingToPay(crowdfundingId);
        if(crowdFundingList!=null && crowdFundingList.size()>0){
            return crowdFundingList.get(0);
        }
        return null;
    }
    public Boolean crowdFundingPrjectNameIsExisting(String crowdFundingPrjectName) {
        List<CrowdFunding> crowdFundingList=findCrowdFundingByPrjectName(crowdFundingPrjectName);
        if(crowdFundingList!=null && crowdFundingList.size()>0){
            return true;
        }
        else{
            return false;
        }
    }
    public List<CrowdFunding> findCrowdFundingByPrjectName(String crowdFundingPrjectName) {
        return crowdFundingDAO.findCrowdFundingByPrjectName(crowdFundingPrjectName);
    }

}
