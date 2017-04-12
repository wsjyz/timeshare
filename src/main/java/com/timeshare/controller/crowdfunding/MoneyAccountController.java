package com.timeshare.controller.crowdfunding;

import com.timeshare.controller.BaseController;
import com.timeshare.domain.crowdfunding.MoneyAccount;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.MoneyAccountService;
import com.timeshare.utils.Contants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

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

    protected Logger logger = LoggerFactory.getLogger(MoneyAccountController.class);

    @RequestMapping(value = "/save")
    public String save(MoneyAccount moneyAccount, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        userId="00359e8721c44d168aac7d501177e314";
        moneyAccount.setCashWithdrawalAmount(new BigDecimal(-100));
        moneyAccount.setCashRaisedAmount(new BigDecimal(100));
        moneyAccount.setMonthWithdrawalNumber(1);
        moneyAccount.setUserId("00359e8721c44d168aac7d501177e314");
        String pk= moneyAccountService.saveOrUpdateMoneyAccount(moneyAccount);
        System.out.println(pk);
        return "info";
    }

    @RequestMapping(value = "/listByOwner")
    public String listByOwner(MoneyAccount MoneyAccount, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        userId="00359e8721c44d168aac7d501177e314";
        MoneyAccount moneyAccountDB= moneyAccountService.findMoneyAccountByOwner(userId);

        System.out.println(moneyAccountDB.getUserId()+"---"+moneyAccountDB.getCashWithdrawalAmount()+"---"+moneyAccountDB.getCashRaisedAmount()+"---"+moneyAccountDB.getMonthWithdrawalNumber());

        return "info";
    }
}
