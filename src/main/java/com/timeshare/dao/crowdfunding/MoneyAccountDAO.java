package com.timeshare.dao.crowdfunding;

import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.crowdfunding.MoneyAccount;

import java.util.List;


public interface MoneyAccountDAO {
    //保存 现金账户信息
    String saveMoneyAccount(MoneyAccount moneyAccount);
    //更改 现金账户信息
    String updateMoneyAccount(MoneyAccount moneyAccount);
    //我的提现
    List<MoneyAccount> findMoneyAccountByOwner(String userId);
    //重置可提现次数
    public String resetMonthWithdrawalNumber() ;
}
