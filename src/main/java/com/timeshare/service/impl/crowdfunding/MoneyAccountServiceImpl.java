package com.timeshare.service.impl.crowdfunding;

import com.timeshare.dao.crowdfunding.MoneyAccountDAO;
import com.timeshare.domain.crowdfunding.MoneyAccount;
import com.timeshare.domain.crowdfunding.WithdrawalLog;
import com.timeshare.service.crowdfunding.MoneyAccountService;
import com.timeshare.service.crowdfunding.WithdrawalLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("MoneyAccountService")
public class MoneyAccountServiceImpl implements MoneyAccountService {

    @Autowired
    MoneyAccountDAO moneyAccountDAO;

    @Override
    public String saveOrUpdateMoneyAccount(MoneyAccount moneyAccount) {
        if(moneyAccount!=null && StringUtils.isNotBlank(moneyAccount.getUserId())){
            //判断用户是否已经有现金账户
           if(moneyAccountIsAlreadyExist(moneyAccount.getUserId())){
               return moneyAccountDAO.updateMoneyAccount(moneyAccount);
           }
           else{
               return moneyAccountDAO.saveMoneyAccount(moneyAccount);
           }
        }
        return null;
    }

    @Override
    public MoneyAccount findMoneyAccountByOwner(String userId) {
        List<MoneyAccount> moneyAccountList=moneyAccountDAO.findMoneyAccountByOwner(userId);
        if(moneyAccountList!=null && moneyAccountList.size()>0){
            return moneyAccountList.get(0);
        }
        else{
            return null;
        }
    }
    //该用户是否有已存在的现金账户
    private boolean moneyAccountIsAlreadyExist(String userId){
        if(StringUtils.isNotBlank(userId)){
            MoneyAccount moneyAccount=findMoneyAccountByOwner(userId);
            if(moneyAccount!=null){
                return true;
            }
        }
        return false;
    }
}
