package com.timeshare.dao;

import com.timeshare.domain.BidSubmit;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface BidSubmitDAO {

    String saveBidSubmit(BidSubmit submit);

    String deleteBidSubmit(String submitId);

    List<BidSubmit> findSubmitList(BidSubmit submit,int startIndex, int loadSize);

    BidSubmit findPreviouSubmit(BidSubmit submit);
}
