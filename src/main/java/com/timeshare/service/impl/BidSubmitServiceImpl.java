package com.timeshare.service.impl;

import com.timeshare.dao.BidSubmitDAO;
import com.timeshare.dao.BidUserDAO;
import com.timeshare.domain.BidSubmit;
import com.timeshare.domain.BidUser;
import com.timeshare.service.BidSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("BidSubmitService")
public class BidSubmitServiceImpl implements BidSubmitService {

    @Autowired
    BidSubmitDAO bidSubmitDAO;
    @Autowired
    BidUserDAO bidUserDAO;

    @Override
    public String saveBidSubmit(BidSubmit submit) {
        BidUser bidUser = bidUserDAO.findBidUserByBidIdAndUserId(submit.getBidId(),submit.getUserId());
        if(bidUser == null){
            bidUser = new BidUser();
            bidUser.setBidId(submit.getBidId());
            bidUser.setCreateUserName(submit.getCreateUserName());
            bidUser.setBidUserId(submit.getUserId());
            bidUser.setUserId(submit.getUserId());
            bidUserDAO.saveBidUser(bidUser);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bidUser.setLastModifyTime(sdf.format(new Date()));
            bidUserDAO.modifyBidUser(bidUser);
        }
        return bidSubmitDAO.saveBidSubmit(submit);
    }

    @Override
    public String deleteBidSubmit(String submitId) {
        return bidSubmitDAO.deleteBidSubmit(submitId);
    }

    @Override
    public List<BidSubmit> findSubmitList(BidSubmit submit, int startIndex, int loadSize) {
        return bidSubmitDAO.findSubmitList(submit,startIndex,loadSize);
    }
}
