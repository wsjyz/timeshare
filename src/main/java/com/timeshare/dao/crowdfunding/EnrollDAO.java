package com.timeshare.dao.crowdfunding;

import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;

import java.util.List;


public interface EnrollDAO {
    //保存 报名信息
    String saveEnroll(Enroll enroll);
    //我预约的众筹
    List<Enroll> findEnrollByOwner(String userId, int startIndex, int loadSize);
    //我预约的众筹-总计数
    int findEnrollByOwnerCount(String userId);
}
