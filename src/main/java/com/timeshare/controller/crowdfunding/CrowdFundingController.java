package com.timeshare.controller.crowdfunding;

import com.alibaba.fastjson.JSON;
import com.timeshare.controller.BaseController;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.Item;
import com.timeshare.domain.SystemMessage;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Assembly;
import com.timeshare.domain.assembly.Attender;
import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.assembly.Fee;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.utils.Contants;
import com.timeshare.utils.WxUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/9/26.
 */
@Controller
@RequestMapping(value = "/crowdFunding")
public class CrowdFundingController extends  BaseController{


    @Autowired
    UserService userService;
    @Autowired
    private CrowdFundingService crowdFundingService;

    protected Logger logger = LoggerFactory.getLogger(CrowdFundingController.class);

    @RequestMapping(value = "/save")
    public String save(CrowdFunding crowdFunding, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        userId="00359e8721c44d168aac7d501177e314";
        crowdFunding.setProjectName("众筹3");
        crowdFunding.setCurriculumStartTime("2017-04-12 21:34:00");
        crowdFunding.setCurriculumEndTime("2017-04-13 21:34:00");

        crowdFunding.setSponsorCity("广州");
        crowdFunding.setDetail("这个课程非常的好！");
        crowdFunding.setCostType(Contants.COST_TYPE.AA.name());
        crowdFunding.setCostTotal(new BigDecimal(678.90));
        crowdFunding.setMinPeoples(3);
        crowdFunding.setMaxPeoples(10);
        crowdFunding.setReservationCost(new BigDecimal(20));
        crowdFunding.setCrowdfundingIndexImgPath("D:/Github/xiaopengyou.jpg");
        crowdFunding.setIsShow(Contants.CROWD_FUNDING_IS_SHOW.YES.name());
        crowdFunding.setCrowdfundingStatus(Contants.CROWD_FUNDING_STATUS.SKETCH.name());
        crowdFunding.setOffShelveReason("课程太好，人太多了，不下架就把系统挤爆了...");
        crowdFunding.setUserId(userId);
        UserInfo userinfo=getCurrentUser(userId);
        crowdFunding.setCreateUserName(userinfo.getNickName());
        crowdFunding.setCreateTime("2017-04-13 21:34:01");
        crowdFunding.setOptTime("2017-04-13 21:34:02");




        String pk= crowdFundingService.saveCrowdFunding(crowdFunding);
        System.out.println(pk);
        return "info";
    }
}
