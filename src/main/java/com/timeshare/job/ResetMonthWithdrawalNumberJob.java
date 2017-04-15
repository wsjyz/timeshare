package com.timeshare.job;

import com.timeshare.service.crowdfunding.MoneyAccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Properties;


public class ResetMonthWithdrawalNumberJob implements Runnable {
    protected static Logger log = LoggerFactory.getLogger("resetMonthWithdrawalNumberJobLogger");

    @Autowired
    MoneyAccountService moneyAccountService;

    @Override
    public void run() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/quartz.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String enabled=properties.getProperty("RESET_MONTH_WITHDRAWAL_NUMBER_JOB.ENABLED");
        if (!"true".equals(enabled)) {
            return;
        }
        try {
            //重置可提现次数
            String result=moneyAccountService.resetMonthWithdrawalNumber();
            log.info("ResetMonthWithdrawalNumberJob complete... Result:"+result);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}