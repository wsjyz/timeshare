package com.timeshare.service.impl;

import com.timeshare.dao.BidSubmitDAO;
import com.timeshare.domain.BidSubmit;
import com.timeshare.service.BidSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("BidSubmitService")
public class BidSubmitServiceImpl implements BidSubmitService {

    @Autowired
    BidSubmitDAO bidSubmitDAO;

    @Override
    public String saveBidSubmit(BidSubmit submit) {
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
