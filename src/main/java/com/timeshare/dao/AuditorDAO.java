package com.timeshare.dao;

import com.timeshare.domain.Auditor;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface AuditorDAO {

    String saveAuditor(Auditor auditor);

    Auditor findAuditorByBidIdAndUserId(String bidId,String userId);

    List<Auditor> findAuditorList(Auditor auditor, int startIndex, int loadSize);

}
