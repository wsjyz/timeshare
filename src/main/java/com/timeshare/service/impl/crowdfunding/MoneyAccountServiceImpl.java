package com.timeshare.service.impl.crowdfunding;

import com.timeshare.dao.crowdfunding.MoneyAccountDAO;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.domain.crowdfunding.MoneyAccount;
import com.timeshare.domain.crowdfunding.WithdrawalLog;
import com.timeshare.service.crowdfunding.EnrollService;
import com.timeshare.service.crowdfunding.MoneyAccountService;
import com.timeshare.service.crowdfunding.WithdrawalLogService;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import com.timeshare.utils.WeixinOauth;
import com.timeshare.utils.WxPayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/9/23.
 */
@Service("MoneyAccountService")
public class MoneyAccountServiceImpl implements MoneyAccountService {

    @Autowired
    MoneyAccountDAO moneyAccountDAO;

    @Autowired
    EnrollService enrollService;

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
    //重置可提现次数
    public String resetMonthWithdrawalNumber() {
        return moneyAccountDAO.resetMonthWithdrawalNumber();
    }

    //自动将项目时间到 未成团的报名进行退款
    //自动将项目时间到 成团的项目进行可提现金额的累加
    public String autoRefundAndMoneyAccount(){
        try{
            Map<String,Integer> crowdFundingEnrollNum=new HashMap<String,Integer>();
            List<Enroll> enrollList=enrollService.findNeedAotuRefundEnroll();
            for (Enroll enroll: enrollList) {
                Integer num=crowdFundingEnrollNum.get(enroll.getCrowdfundingId());
                if(num!=null){
                    crowdFundingEnrollNum.put(enroll.getCrowdfundingId(),num+1);
                }
                else{
                    crowdFundingEnrollNum.put(enroll.getCrowdfundingId(),1);
                }
            }

            for (Enroll enroll: enrollList) {
                Integer enrollNum=crowdFundingEnrollNum.get(enroll.getCrowdfundingId());
                //报名人数小于 项目最小人数 进行自动退款
                if(enrollNum!=null && enrollNum<Integer.parseInt(enroll.getMinPeoples())){
                    //交易号不为空 并且资金没有被转移过
                    if(StringUtils.isNotBlank(enroll.getPayTradeNo()) && !Contants.IS_TRANSFER_CASH_ACCOUNT.YES.name().equals(enroll.getIsTransferCashAccount())){
                        String outRefundNo = CommonStringUtils.genPK();
                        int payAmountFee=(enroll.getPayAmount().multiply(new BigDecimal(100))).intValueExact();
                        //发起退款
                        String result= WxPayUtils.payRefund(outRefundNo,enroll.getPayTradeNo(),payAmountFee,payAmountFee);
                        if(result=="success"){
                            //退款成功后将支付状态改为已退款 将退款交易号保存
                            enrollService.autoRefundAfterUpdate(enroll.getEnrollId(),outRefundNo);
                        }
                    }
                }
                else{
                    //没有被转移过的资金
                    if(!Contants.IS_TRANSFER_CASH_ACCOUNT.YES.name().equals(enroll.getIsTransferCashAccount())){
                        //项目时间到 报名人数大于等于 项目最小人数 进行可提现金额转移
                        MoneyAccount moneyAccount=new MoneyAccount();
                        moneyAccount.setUserId(enroll.getOwnerUserId());
                        moneyAccount.setCashWithdrawalAmount(enroll.getPayAmount());
                        saveOrUpdateMoneyAccount(moneyAccount);
                        //更新资金转移标志位
                        enrollService.autoMoneyTransferAfterUpdate(enroll.getEnrollId());
                    }
                }
            }
            return Contants.SUCCESS;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Contants.FAILED;
    }
}
