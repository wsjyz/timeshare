package com.timeshare.service;

import com.timeshare.domain.Auditor;

import java.util.List;

/**
 * Created by user on 2016/10/12.
 */
public interface AuditorService {

    String saveAuditor(Auditor auditor);

    Auditor findAuditorByBidIdAndUserId(String bidId,String userId);

    List<Auditor> findAuditorList(Auditor auditor, int startIndex, int loadSize);

}
