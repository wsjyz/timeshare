package com.timeshare.controller.crowdfunding;

import com.timeshare.controller.BaseController;
import com.timeshare.domain.crowdfunding.MoneyAccount;
import com.timeshare.domain.crowdfunding.WithdrawalLog;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.MoneyAccountService;
import com.timeshare.service.crowdfunding.WithdrawalLogService;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import com.timeshare.utils.WeixinOauth;
import com.timeshare.utils.WxPayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/9/26.
 */
@Controller
@RequestMapping(value = "/moneyAccount")
public class MoneyAccountController extends  BaseController{

    @Autowired
    private MoneyAccountService moneyAccountService;
    @Autowired
    private WithdrawalLogService withdrawalLogService;

    protected Logger logger = LoggerFactory.getLogger(MoneyAccountController.class);


    @RequestMapping(value = "/listByOwner")
    public String listByOwner(@CookieValue(value="time_sid", defaultValue="admin") String userId, Model model) {
        userId="00359e8721c44d168aac7d501177e314";
        MoneyAccount moneyAccountDB= moneyAccountService.findMoneyAccountByOwner(userId);
        model.addAttribute("moneyAccount",moneyAccountDB);
        return "crowdfunding/tx";
    }
    //goto提现申请页面
    @RequestMapping(value = "/gotoWithdrawal")
    public String gotoWithdrawal(@CookieValue(value="time_sid", defaultValue="admin") String userId, Model model) {
        userId="00359e8721c44d168aac7d501177e314";
        MoneyAccount moneyAccountDB= moneyAccountService.findMoneyAccountByOwner(userId);
        model.addAttribute("moneyAccount",moneyAccountDB);
        model.addAttribute("monthWithdrawalMaxNumber",Contants.MONTH_WITHDRAWAL_MAX_NUMBER);
        return "crowdfunding/txsq";
    }
    //提现检查
    @ResponseBody
    @RequestMapping(value = "/payToSellerCheck")
    public String payToSellerCheck(@RequestParam BigDecimal cashWithdrawalAmountInput,@CookieValue(value="time_sid", defaultValue="admin") String userId) {
        //MOCK
        userId="00359e8721c44d168aac7d501177e314";
        long cashWithdrawalAmount=(cashWithdrawalAmountInput.multiply(new BigDecimal(100))).longValueExact();
        if(cashWithdrawalAmount>=101 && cashWithdrawalAmount<=200000){
            MoneyAccount moneyAccount=moneyAccountService.findMoneyAccountByOwner(userId);
            if(moneyAccount!=null){
                if(moneyAccount.getMonthWithdrawalNumber()<Contants.MONTH_WITHDRAWAL_MAX_NUMBER){
                    long cashWithdrawalAmountDB=(moneyAccount.getCashWithdrawalAmount().multiply(new BigDecimal(100))).longValueExact();
                    if(cashWithdrawalAmountDB>=cashWithdrawalAmount){
                        return Contants.SUCCESS;
                    }
                    else{
                        //可提现金额已超
                        return "CASH_WITHDRAWAL_AMOUNT_INPUT_EXCESS_ERROR";
                    }
                }
                else{
                    //提现次数已超
                    return "MONTH_WITHDRAWAL_MAX_NUMBER_ERROR";
                }
            }
        }
        else{
            //提现金额输入非法
            return "MONEY_ACCOUNT_AMOUNT_ERROR";
        }
        return Contants.FAILED;
    }

    //提现
    @RequestMapping(value = "/payToSeller")
    public String payToSeller(HttpServletRequest request,@CookieValue(value="time_sid", defaultValue="admin") String userId,Model model) {
        //MOCK
        userId="00359e8721c44d168aac7d501177e314";

        String code = request.getParameter("code");
        String cashWithdrawalAmountInput = request.getParameter("state");

        WeixinOauth weixinOauth = new WeixinOauth();
        String openId = weixinOauth.obtainOpenId(code);
        String cashWithdrawalTradeNo = CommonStringUtils.gen18RandomNumber();
        //发起提现操作
        String result= WxPayUtils.payToSeller(cashWithdrawalTradeNo,new BigDecimal(cashWithdrawalAmountInput),openId);
        if(Contants.SUCCESS.equals(result)){
            //提现成功
            WithdrawalLog withdrawalLog=new WithdrawalLog();
            withdrawalLog.setWithdrawalCash(new BigDecimal(cashWithdrawalAmountInput));
            withdrawalLog.setWithdrawalStatus(Contants.WITHDRAWAL_STATUS.SUCCESS.name());
            withdrawalLog.setWithdrawalTradeNo(cashWithdrawalTradeNo);
            withdrawalLog.setWithdrawalTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            withdrawalLog.setUserId(userId);
            withdrawalLogService.saveWithdrawalLogAndUpdateMoneyAccount(withdrawalLog);
        }
        else{
            //提现失败
            WithdrawalLog withdrawalLog=new WithdrawalLog();
            withdrawalLog.setWithdrawalCash(new BigDecimal(cashWithdrawalAmountInput));
            withdrawalLog.setWithdrawalStatus(Contants.WITHDRAWAL_STATUS.REJECT.name());
            withdrawalLog.setWithdrawalTradeNo(cashWithdrawalTradeNo);
            withdrawalLog.setWithdrawalTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            //记录失败原因
            withdrawalLog.setReplyMsg(result);
            withdrawalLog.setUserId(userId);
            withdrawalLogService.saveWithdrawalLog(withdrawalLog);
        }
        return "crowdfunding/txjl";
    }
}
