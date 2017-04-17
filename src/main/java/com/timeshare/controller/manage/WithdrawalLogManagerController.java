package com.timeshare.controller.manage;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/9/26.
 */
@Controller
@RequestMapping(value = "/manager/withdrawalLog")
public class WithdrawalLogManagerController extends  BaseController{


    @Autowired
    UserService userService;
    @Autowired
    private WithdrawalLogService withdrawalLogService;

    protected Logger logger = LoggerFactory.getLogger(WithdrawalLogManagerController.class);


    @RequestMapping(value = "/gotoWithdrawalLog")
    public String gotoWithdrawalLog() {
        return "/manager/txgl";
    }

    @ResponseBody
    @RequestMapping(value = "/listByOwner")
    public List<WithdrawalLog> listByOwner(@RequestParam int startIndex, @RequestParam int loadSize, @CookieValue(value="time_sid", defaultValue="") String userId) {
        return withdrawalLogService.findWithdrawalLogByOwner("",startIndex,loadSize);
    }
}
