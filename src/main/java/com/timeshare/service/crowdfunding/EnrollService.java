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
    //我预约的众筹
    public List<Enroll> findCrowdfundingByMyEnroll(int startIndex, int loadSize,String userId) ;
    //获取已报名对象
    public List<Enroll> findCrowdfundingEnrollList(String crowdfundingId);
    //获取已报名名单
    public List<Enroll> findCrowdfundingEnrollListToPaging(String crowdfundingId,int startIndex, int loadSize);

    //查询出需要自动退款的报名记录
    public List<Enroll> findNeedAotuRefundEnroll();
    //自动退款完成后更新支付状态及退款交易号
    public String autoRefundAfterUpdate(String enrollId,String refundTradeNo) ;
    //自动更新现金账户可提现金额后更新已转移标志
    public String autoMoneyTransferAfterUpdate(String enrollId) ;
    //根据购买用户ID及项目ID 获取预约信息
    public List<Enroll> findEnrollByEnrollUserIdAndCrowdFundingId(String enrollUserId,String crowdFundingId) ;
    //根据购买用户ID判断用户是否已经购买过
    public Boolean enrollUserIdIsAlreadyBuy(String enrollUserId,String crowdFundingId) ;
}
