package com.timeshare.dao;

import com.timeshare.domain.Bid;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface BidDAO {

    String saveBid(Bid bid);

    String modifyBid(Bid bid);

    Bid findBidById(String bidId);

    List<Bid> findBidList(Bid bid,int startIndex, int loadSize);

    int findBidCount(Bid bid);

    List<Bid> searchBidList(Bid bid, int startIndex, int loadSize);

    List<Bid> findBidListForManage(Bid bid, int startIndex, int loadSize);

}
