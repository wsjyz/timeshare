package com.timeshare.controller.crowdfunding;

import com.timeshare.controller.BaseController;
import com.timeshare.domain.crowdfunding.WithdrawalLog;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.WithdrawalLogService;
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
@RequestMapping(value = "/withdrawalLog")
public class WithdrawalLogController extends  BaseController{


    @Autowired
    UserService userService;
    @Autowired
    private WithdrawalLogService withdrawalLogService;

    protected Logger logger = LoggerFactory.getLogger(WithdrawalLogController.class);

    @RequestMapping(value = "/save")
    public String save(WithdrawalLog withdrawalLog, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        userId="00359e8721c44d168aac7d501177e314";
        withdrawalLog.setWithdrawalCash(new BigDecimal(10));
        withdrawalLog.setWithdrawalStatus(Contants.WITHDRAWAL_STATUS.PENDING.name());
        withdrawalLog.setWithdrawalTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        withdrawalLog.setUserId(userId);
        String pk= withdrawalLogService.saveWithdrawalLog(withdrawalLog);
        System.out.println(pk);
        return "info";
    }

    @RequestMapping(value = "/listByOwner")
    public String listByOwner(WithdrawalLog WithdrawalLog, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        userId="00359e8721c44d168aac7d501177e314";
        List<WithdrawalLog> withdrawalLogList= withdrawalLogService.findWithdrawalLogByOwner(userId,0,10);
        for (WithdrawalLog withdrawalLogItem: withdrawalLogList) {
            System.out.println(withdrawalLogItem.getWithdrawalCash()+"---"+withdrawalLogItem.getWithdrawalStatus()+"----"+withdrawalLogItem.getWithdrawalTime()+"----"+withdrawalLogItem.getReplyMsg());
        }
        return "info";
    }
}
