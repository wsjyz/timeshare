package com.timeshare.service.impl;

import com.timeshare.dao.BidUserDAO;
import com.timeshare.domain.BidUser;
import com.timeshare.service.BidUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("BidUserService")
public class BidUserServiceImpl implements BidUserService {

    @Autowired
    BidUserDAO bidUserDAO;

    @Override
    public String saveBidUser(BidUser bidUser) {
        return bidUserDAO.saveBidUser(bidUser);
    }

    @Override
    public BidUser findBidUserByBidIdAndUserId(String bidId, String userId) {
        return bidUserDAO.findBidUserByBidIdAndUserId(bidId,userId);
    }

    @Override
    public String modifyBidUser(BidUser bidUser) {
        return bidUserDAO.modifyBidUser(bidUser);
    }

    @Override
    public List<BidUser> findBidUserList(BidUser bidUser, int startIndex, int loadSize) {
        return bidUserDAO.findBidUserList(bidUser,startIndex,loadSize);
    }
}
