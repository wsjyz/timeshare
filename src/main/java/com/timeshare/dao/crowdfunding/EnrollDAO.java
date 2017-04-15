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
    //某个众筹项目的报名人集合
    List<Enroll> findEnrollByCrowdfundingId(String crowdfundingId,int startIndex, int loadSize) ;
    //根据主键获取对象
    List<Enroll> findEnrollById(String enrollId) ;
    //修改报名对象
    public String modifyEnroll(Enroll enroll);
    //我预约的众筹
    public List<Enroll> findCrowdfundingByMyEnroll(int startIndex, int loadSize,String userId) ;
    //获取已报名对象
    public List<Enroll> findCrowdfundingEnrollList(String crowdfundingId,int startIndex, int loadSize);

    //查询出需要自动退款的报名记录
    public List<Enroll> findNeedAotuRefundEnroll() ;
    //自动退款完成后更新支付状态及退款交易号
    public String autoRefundAfterUpdate(String enrollId,String refundTradeNo) ;
    //自动更新现金账户可提现金额后更新已转移标志
    public String autoMoneyTransferAfterUpdate(String enrollId) ;
}
