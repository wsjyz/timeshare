package com.timeshare.controller.crowdfunding;

import com.timeshare.controller.BaseController;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.service.crowdfunding.EnrollService;
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
@RequestMapping(value = "/enroll")
public class EnrollController extends  BaseController{


    @Autowired
    UserService userService;
    @Autowired
    private EnrollService enrollService;

    protected Logger logger = LoggerFactory.getLogger(EnrollController.class);

    @RequestMapping(value = "/save")
    public String save(Enroll enroll, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        userId="00359e8721c44d168aac7d501177e314";
        enroll.setCrowdfundingId("dbbc8b43a3764dd4afae8d5b16228124");
        enroll.setEnrollUserId(userId);
        enroll.setUserName("张三");
        enroll.setPhone("13400001111");
        enroll.setCorpName("启东教育");
        enroll.setInvoiceTitle("启东教育有限公司");
        enroll.setInvoiceType(Contants.ENROLL_INVOICE_TYPE.VAT_INVOICE.name());
        enroll.setPayStatus(Contants.ENROLL_PAY_STATUS.PAYED.name());
        enroll.setPayAmount(new BigDecimal(10.23));
        enroll.setUserId(userId);
        enroll.setOptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String pk= enrollService.saveEnroll(enroll);
        System.out.println(pk);
        return "info";
    }

    @RequestMapping(value = "/listByOwner")
    public String listByOwner(Enroll enroll, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        userId="00359e8721c44d168aac7d501177e314";
        List<Enroll> enrollList= enrollService.findEnrollByOwner(userId,0,10);
        for (Enroll enrollItem: enrollList) {
            System.out.println(enrollItem.getUserName()+"---"+enrollItem.getPhone()+"---"+enrollItem.getCorpName());
        }
        return "info";
    }
}
