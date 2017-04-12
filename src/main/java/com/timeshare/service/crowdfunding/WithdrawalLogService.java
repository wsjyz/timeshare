package com.timeshare.service.crowdfunding;

import com.timeshare.domain.crowdfunding.WithdrawalLog;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface WithdrawalLogService {
    //保存报名信息
    String saveWithdrawalLog(WithdrawalLog withdrawalLog);
    //我预约的众筹
    List<WithdrawalLog> findWithdrawalLogByOwner(String userId, int startIndex, int loadSize);
    //我预约的众筹-总计数
    int findWithdrawalLogByOwnerCount(String userId);
}
