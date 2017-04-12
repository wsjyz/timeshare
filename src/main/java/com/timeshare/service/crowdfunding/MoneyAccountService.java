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
}
