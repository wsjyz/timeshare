package com.timeshare.service.crowdfunding;

import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;

import java.io.IOException;
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
    //发送报名名单
    public Boolean exportEnrollListToEmail(String crowdfundingId,String toEmailAddress) throws IOException;
    //某众筹的报名名单-分页
    public List<Enroll> findEnrollByCrowdfundingId(String crowdfundingId, int startIndex, int loadSize);
    //根据主键获取对象
    public Enroll findEnrollById(String enrollId) ;
    //修改报名对象
    public String modifyEnroll(Enroll enroll);
}
