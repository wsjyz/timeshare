package com.timeshare.service.crowdfunding;

import com.timeshare.domain.crowdfunding.WithdrawalLog;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface WithdrawalLogService {
    //保存提现信息并更新现金账户
    String saveWithdrawalLogAndUpdateMoneyAccount(WithdrawalLog withdrawalLog);
    //根据用户ID获取提现记录
    List<WithdrawalLog> findWithdrawalLogByOwner(String userId, int startIndex, int loadSize);
    //根据用户ID获取提现记录-总条数
    int findWithdrawalLogByOwnerCount(String userId);
    //保存提现记录
    public String saveWithdrawalLog(WithdrawalLog withdrawalLog) ;
}
