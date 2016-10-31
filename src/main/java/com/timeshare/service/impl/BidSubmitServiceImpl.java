package com.timeshare.service.impl;

import com.alibaba.fastjson.JSON;
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

import java.text.ParseException;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(bidUser == null){
            bidUser = new BidUser();
            bidUser.setBidId(submit.getBidId());
            bidUser.setCreateUserName(submit.getCreateUserName());
            bidUser.setBidUserId(submit.getUserId());
            bidUser.setUserId(submit.getUserId());
            //增加人数
            if(!bid.getUserId().equals(submit.getUserId())){
                bidUserDAO.saveBidUser(bidUser);
                bid.setSubmitCount(bid.getSubmitCount() + 1);
            }
            bidDAO.modifyBid(bid);
        }else{
            bidUser.setLastModifyTime(sdf.format(new Date()));
            bidUserDAO.modifyBidUser(bidUser);
        }
        //发短信
        SmsContentBean bean;
        UserInfo bidCreator = userService.findUserByUserId(bid.getUserId());
        if(bidCreator.getUserId().equals(submit.getUserId())){//回答别人的应飚
            BidSubmit params = new BidSubmit();
            params.setBidId(bid.getBidId());
            params.setUserId(bidCreator.getUserId());
            params.setOtherUserId(submit.getOtherUserId());
            BidSubmit previousSubmit = bidSubmitDAO.findPreviouSubmit(params);

            UserInfo otherUser = userService.findUserByUserId(submit.getOtherUserId());
            if(previousSubmit == null){
                sendmmsToBidUser(bid.getTitle(),otherUser.getMobile());
            }else{
                long previousTime = 0;
                try {
                    previousTime = sdf.parse(previousSubmit.getOptTime()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date nowDate = new Date();
                if ((nowDate.getTime() - previousTime) > (1000 * 60 * 3)){
                    sendmmsToBidUser(bid.getTitle(),otherUser.getMobile());
                }
            }


        }else{
            BidSubmit params = new BidSubmit();
            params.setBidId(bid.getBidId());
            params.setUserId(submit.getUserId());
            BidSubmit previousSubmit = bidSubmitDAO.findPreviouSubmit(params);
            //System.out.println(JSON.toJSONString(previousSubmit));
            if(previousSubmit == null){
                sendmmsToBidCreator(bid.getTitle(),bidCreator.getMobile());
            }else{
                long previousTime = 0;
                try {
                    previousTime = sdf.parse(previousSubmit.getOptTime()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date nowDate = new Date();
                if ((nowDate.getTime() - previousTime) > (1000 * 60 * 10)){
                    sendmmsToBidCreator(bid.getTitle(),bidCreator.getMobile());
                }
            }


        }

        return bidSubmitDAO.saveBidSubmit(submit);
    }
    private void sendmmsToBidUser(String bidTitle,String mobile){
        SmsContentBean bean = new SmsContentBean();
        bean.setTemplateCode("SMS_22275127");
        bean.setToMobile(mobile);//这里有问题，需要知道回复谁的
        bean.setContent("{\"bidName\":\""+bidTitle+"\"}");
        System.out.println("向"+mobile+"发短信："+"飚主回答了您的“"+bidTitle+"”应飚，请进入微信服务号“邂逅时刻 ”查看");
        String response = SmsUtils.senMessage(bean);
        if(response.indexOf("error_response") != -1){
            logger.error(response);
        }
    }

    private void sendmmsToBidCreator(String bidTitle,String mobile){
        SmsContentBean bean = new SmsContentBean();
        bean.setTemplateCode("SMS_21385035");
        bean.setToMobile(mobile);
        bean.setContent("{\"bidName\":\""+bidTitle+"\"}");
        System.out.println("向"+mobile+"发短信："+"您的“"+bidTitle+"”有人应飚了，请进入微信服务号“邂逅时刻 ”查看");
        String response = SmsUtils.senMessage(bean);
        if(response.indexOf("error_response") != -1){
            logger.error(response);
        }
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
