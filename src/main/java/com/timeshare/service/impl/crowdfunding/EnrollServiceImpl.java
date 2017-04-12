package com.timeshare.service.impl.crowdfunding;

import com.timeshare.dao.crowdfunding.CrowdFundingDAO;
import com.timeshare.dao.crowdfunding.EnrollDAO;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.service.crowdfunding.EnrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("EnrollService")
public class EnrollServiceImpl implements EnrollService {

    @Autowired
    EnrollDAO enrollDAO;

    @Override
    public String saveEnroll(Enroll enroll) {
        return enrollDAO.saveEnroll(enroll);
    }

    @Override
    public List<Enroll> findEnrollByOwner(String userId, int startIndex, int loadSize) {
        return enrollDAO.findEnrollByOwner(userId,startIndex,loadSize);
    }

    @Override
    public int findEnrollByOwnerCount(String userId) {
        return enrollDAO.findEnrollByOwnerCount(userId);
    }
}
