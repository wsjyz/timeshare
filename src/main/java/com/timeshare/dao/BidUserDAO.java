package com.timeshare.dao;

import com.timeshare.dao.impl.BidUserInfo;
import com.timeshare.domain.BidUser;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface BidUserDAO {

    String saveBidUser(BidUser bidUser);

    BidUser findBidUserByBidIdAndUserId(String bidId,String userId);

    String modifyBidUser(BidUser bidUser);

    List<BidUser> findBidUserList(BidUser bidUser, int startIndex, int loadSize);

    List<BidUserInfo> findNotWinBidUserList(BidUser bidUser);
}
