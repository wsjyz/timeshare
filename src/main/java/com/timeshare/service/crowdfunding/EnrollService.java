package com.timeshare.service.crowdfunding;

import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface EnrollService {
    //保存报名信息
    String saveEnroll(Enroll enroll);
    //我预约的众筹
    List<Enroll> findEnrollByOwner(String userId, int startIndex, int loadSize);
    //我预约的众筹-总计数
    int findEnrollByOwnerCount(String userId);
}
