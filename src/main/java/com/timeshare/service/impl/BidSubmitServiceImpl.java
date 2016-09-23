package com.timeshare.service.impl;

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
    BidSubmitService bidSubmitService;

    @Override
    public String saveBidSubmit(BidSubmit submit) {
        return bidSubmitService.saveBidSubmit(submit);
    }

    @Override
    public String deleteBidSubmit(String submitId) {
        return bidSubmitService.deleteBidSubmit(submitId);
    }

    @Override
    public List<BidSubmit> findSubmitList(BidSubmit submit, int startIndex, int loadSize) {
        return bidSubmitService.findSubmitList(submit,startIndex,loadSize);
    }
}
