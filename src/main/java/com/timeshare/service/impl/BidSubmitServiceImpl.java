package com.timeshare.service.impl;

import com.timeshare.dao.BidDAO;
import com.timeshare.dao.BidSubmitDAO;
import com.timeshare.dao.BidUserDAO;
import com.timeshare.domain.Bid;
import com.timeshare.domain.BidSubmit;
import com.timeshare.domain.BidUser;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.BidSubmitService;
import com.timeshare.service.UserService;
import com.timeshare.utils.SmsContentBean;
import com.timeshare.utils.SmsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("BidSubmitService")
public class BidSubmitServiceImpl implements BidSubmitService {

    protected Logger logger = LoggerFactory.getLogger(BidSubmitServiceImpl.class);
    @Autowired
    BidSubmitDAO bidSubmitDAO;
    @Autowired
    BidUserDAO bidUserDAO;
    @Autowired
    BidDAO bidDAO;
    @Autowired
    UserService userService;

    @Override
    public String saveBidSubmit(BidSubmit submit) {
        BidUser bidUser = bidUserDAO.findBidUserByBidIdAndUserId(submit.getBidId(),submit.getUserId());
        Bid bid = bidDAO.findBidById(submit.getBidId());
        if(bidUser == null){
            bidUser = new BidUser();
            bidUser.setBidId(submit.getBidId());
            bidUser.setCreateUserName(submit.getCreateUserName());
            bidUser.setBidUserId(submit.getUserId());
            bidUser.setUserId(submit.getUserId());
            bidUserDAO.saveBidUser(bidUser);
            //增加人数
            bid.setSubmitCount(bid.getSubmitCount() + 1);
            bidDAO.modifyBid(bid);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bidUser.setLastModifyTime(sdf.format(new Date()));
            bidUserDAO.modifyBidUser(bidUser);
        }
        //发短信
        UserInfo bidCreator = userService.findUserByUserId(bid.getUserId());
        SmsContentBean bean = new SmsContentBean();
        bean.setTemplateCode("SMS_21385035");
        bean.setToMobile(bidCreator.getMobile());
        bean.setContent("{\"bidName\":\""+bid.getTitle()+"\"}");
        System.out.println("向卖家"+bidCreator.getMobile()+"发短信："+"您的“"+bid.getTitle()+"”有人应飚了，请进入微信服务号“邂逅时刻 ”查看");
        String response = SmsUtils.senMessage(bean);
        if(response.indexOf("error_response") != -1){
            logger.error(response);
        }
        return bidSubmitDAO.saveBidSubmit(submit);
    }

    @Override
    public String deleteBidSubmit(String submitId) {
        return bidSubmitDAO.deleteBidSubmit(submitId);
    }

    @Override
    public List<BidSubmit> findSubmitList(BidSubmit submit, int startIndex, int loadSize) {
        return bidSubmitDAO.findSubmitList(submit,startIndex,loadSize);
    }
}
