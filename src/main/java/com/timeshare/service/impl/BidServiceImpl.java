package com.timeshare.service.impl;

import com.timeshare.dao.BidDAO;
import com.timeshare.domain.Bid;
import com.timeshare.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("BidService")
public class BidServiceImpl implements BidService {

    @Autowired
    BidDAO bidDAO;

    @Override
    public String saveBid(Bid bid) {
        return bidDAO.saveBid(bid);
    }

    @Override
    public String modifyBid(Bid bid) {
        return bidDAO.modifyBid(bid);
    }

    @Override
    public Bid findBidById(String bidId) {
        return bidDAO.findBidById(bidId);
    }

    @Override
    public List<Bid> findBidList(Bid bid, int startIndex, int loadSize) {
        return bidDAO.findBidList(bid,startIndex,loadSize);
    }

    @Override
    public int findBidCount(Bid bid) {
        return bidDAO.findBidCount(bid);
    }

    @Override
    public List<Bid> searchBidList(Bid bid, int startIndex, int loadSize) {
        return bidDAO.searchBidList(bid,startIndex,loadSize);
    }

    @Override
    public List<Bid> findBidListForManage(Bid bid, int startIndex, int loadSize) {
        return bidDAO.findBidListForManage(bid,startIndex,loadSize);
    }
}
