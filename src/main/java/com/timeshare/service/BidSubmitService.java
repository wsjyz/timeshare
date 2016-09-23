package com.timeshare.service;

import com.timeshare.domain.BidSubmit;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface BidSubmitService {

    String saveBidSubmit(BidSubmit submit);

    String deleteBidSubmit(String submitId);

    List<BidSubmit> findSubmitList(BidSubmit submit, int startIndex, int loadSize);

}
