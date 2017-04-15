package com.timeshare.service.crowdfunding;

import com.timeshare.domain.crowdfunding.WithdrawalLog;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface WithdrawalLogService {
    //保存提现信息并更新现金账户
    String saveWithdrawalLogAndUpdateMoneyAccount(WithdrawalLog withdrawalLog);

    List<WithdrawalLog> findWithdrawalLogByOwner(String userId, int startIndex, int loadSize);

    int findWithdrawalLogByOwnerCount(String userId);
    public String saveWithdrawalLog(WithdrawalLog withdrawalLog) ;
}
