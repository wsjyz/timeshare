package com.timeshare.service.impl;

import com.timeshare.dao.BidUserDAO;
import com.timeshare.domain.BidUser;
import com.timeshare.service.BidUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
