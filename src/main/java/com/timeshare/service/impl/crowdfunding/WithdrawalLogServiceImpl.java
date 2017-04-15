package com.timeshare.service.impl.crowdfunding;

import com.timeshare.dao.crowdfunding.MoneyAccountDAO;
import com.timeshare.dao.crowdfunding.WithdrawalLogDAO;
import com.timeshare.domain.crowdfunding.MoneyAccount;
import com.timeshare.domain.crowdfunding.WithdrawalLog;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.MoneyAccountService;
import com.timeshare.service.crowdfunding.WithdrawalLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("WithdrawalLogService")
public class WithdrawalLogServiceImpl implements WithdrawalLogService {

    @Autowired
    WithdrawalLogDAO withdrawalLogDAO;
    @Autowired
    MoneyAccountService moneyAccountService;

    @Override
    public String saveWithdrawalLogAndUpdateMoneyAccount(WithdrawalLog withdrawalLog) {
        //保存提现记录
        String pk=withdrawalLogDAO.saveWithdrawalLog(withdrawalLog);
        MoneyAccount moneyAccount=new MoneyAccount();
        //提现成功后 现金账户可提现金额需要相对减少 (计算出结果为 当前提现金额的对应负数)
        moneyAccount.setCashWithdrawalAmount(withdrawalLog.getWithdrawalCash().subtract(withdrawalLog.getWithdrawalCash().multiply(new BigDecimal(2))));
        moneyAccount.setCashRaisedAmount(withdrawalLog.getWithdrawalCash());
        moneyAccount.setMonthWithdrawalNumber(1);
        moneyAccount.setUserId(withdrawalLog.getUserId());
        //更新现金账户
        moneyAccountService.saveOrUpdateMoneyAccount(moneyAccount);
        return pk;
    }

    public String saveWithdrawalLog(WithdrawalLog withdrawalLog) {
        //保存提现记录
        return withdrawalLogDAO.saveWithdrawalLog(withdrawalLog);
    }

    @Override
    public List<WithdrawalLog> findWithdrawalLogByOwner(String userId, int startIndex, int loadSize) {
        return withdrawalLogDAO.findWithdrawalLogByOwner(userId,startIndex,loadSize);
    }

    @Override
    public int findWithdrawalLogByOwnerCount(String userId) {
        return withdrawalLogDAO.findWithdrawalLogByOwnerCount(userId);
    }
}
