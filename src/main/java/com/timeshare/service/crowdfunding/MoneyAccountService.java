package com.timeshare.service.crowdfunding;

import com.timeshare.domain.crowdfunding.MoneyAccount;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
public interface MoneyAccountService {
    //保存或更改提现信息
    String saveOrUpdateMoneyAccount(MoneyAccount moneyAccount);
    //我的提现
    MoneyAccount findMoneyAccountByOwner(String userId);
    //重置可提现次数
    public String resetMonthWithdrawalNumber() ;
    //项目时间到 未成团的自动退款 成团的转移现金账户到可提现余额
    public String autoRefundAndMoneyAccount() ;
}
