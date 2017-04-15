package com.timeshare.job;

import com.timeshare.service.crowdfunding.MoneyAccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Properties;


public class AutoRefundAndMoneyAccountJob implements Runnable {

	protected static Logger log = LoggerFactory.getLogger("autoRefundAndMoneyAccountJobLogger");

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
        String enabled=properties.getProperty("AUTO_REFUND_AND_MONEY_ACCOUNT_JOB.ENABLED");
		if (!"true".equals(enabled)) {
			return;
		}
		try {
			String result=moneyAccountService.autoRefundAndMoneyAccount();
			log.info("AutoRefundAndMoneyAccountJob complete... Result:"+result);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}



}
