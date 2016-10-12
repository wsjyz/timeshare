package com.timeshare.service.impl;

import com.timeshare.dao.AuditorDAO;
import com.timeshare.domain.Auditor;
import com.timeshare.service.AuditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/10/12.
 */
@Service("AuditorService")
public class AuditorServiceImpl implements AuditorService {

    @Autowired
    AuditorDAO auditorDAO;

    @Override
    public String saveAuditor(Auditor auditor) {
        return auditorDAO.saveAuditor(auditor);
    }

    @Override
    public Auditor findAuditorByBidIdAndUserId(String bidId, String userId) {
        return auditorDAO.findAuditorByBidIdAndUserId(bidId,userId);
    }

    @Override
    public List<Auditor> findAuditorList(Auditor auditor, int startIndex, int loadSize) {
        return auditorDAO.findAuditorList(auditor,startIndex,loadSize);
    }
}
