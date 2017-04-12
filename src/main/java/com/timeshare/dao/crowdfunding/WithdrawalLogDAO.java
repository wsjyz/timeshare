package com.timeshare.dao.crowdfunding;

import com.timeshare.domain.crowdfunding.WithdrawalLog;

import java.util.List;


public interface WithdrawalLogDAO {
    //保存 提现记录
    String saveWithdrawalLog(WithdrawalLog withdrawalLog);
    //我的提现记录
    List<WithdrawalLog> findWithdrawalLogByOwner(String userId, int startIndex, int loadSize);
    //我的提现记录-总计数
    int findWithdrawalLogByOwnerCount(String userId);
}
