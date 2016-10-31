package com.timeshare.controller;

import com.timeshare.dao.impl.BidUserInfo;
import com.timeshare.domain.Bid;
import com.timeshare.domain.BidUser;
import com.timeshare.domain.SystemMessage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.BidService;
import com.timeshare.service.BidUserService;
import com.timeshare.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by user on 2016/10/10.
 */
@Controller
@RequestMapping(value = "/biduser")
public class BidUserController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(BidUserController.class);

    @Autowired
    BidUserService bidUserService;
    @Autowired
    BidService bidService;

    @RequestMapping(value = "/to-list/{bidId}")
    public String toList(@PathVariable String bidId,Model model) {

        if(StringUtils.isNotBlank(bidId)){
            Bid bid = bidService.findBidById(bidId);
            model.addAttribute("bid",bid);
        }

        return "bid/biduserlist";
    }

    @ResponseBody
    @RequestMapping(value = "/find-list")
    public List<BidUser> findList(@RequestParam int startIndex, @RequestParam int loadSize,String bidId,
                                  @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {

        BidUser bidUser = new BidUser();
        //bidUser.setUserId(userId);
        bidUser.setBidId(bidId);
        bidUser.setCurrentUserId(userId);
        List<BidUser> bidUserList = bidUserService.findBidUserList(bidUser,startIndex,loadSize);
        return bidUserList;
    }

    @ResponseBody
    @RequestMapping(value = "/modify")
    public SystemMessage modify(BidUser bidUser, @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {

        String result = "";

        if(bidUser != null){
            Bid bid = bidService.findBidById(bidUser.getBidId());
            if(StringUtils.isNotBlank(bidUser.getWinTheBid()) && bidUser.getWinTheBid().equals("1")){

                bid.setBidStatus(Contants.BID_STATUS.finish.toString());
                //修改飚的得分
                bid.setScore(bidUser.getRating());
                bidService.modifyBid(bid);
                bidUser.setIncomeFee(bid.getPrice());

            }
            String outTradeNo = CommonStringUtils.gen18RandomNumber();
            bidUser.setWxTradeNo(outTradeNo);
            result = bidUserService.modifyBidUser(bidUser);
            String bidUserId = bidUser.getUserId();
            UserInfo bidUserInfo = getCurrentUser(bidUserId);
            WxPayUtils.payToSeller(outTradeNo,bid.getPrice(),bidUserInfo.getOpenId());
            //修改收入
            bidUserInfo.setIncome(bidUserInfo.getIncome().add(bid.getPrice()));
            userService.modifyUser(bidUserInfo);


            //发短信通知中标者
            SmsContentBean bean = null;
            String response = "";
            bean = new SmsContentBean();
            bean.setTemplateCode("SMS_21235044");
            bean.setToMobile(bidUserInfo.getMobile());
            bean.setContent("{\"bidName\":\""+bid.getTitle()+"\",\"bidPrice\":\""+bid.getPrice()+"\"}");
            System.out.println("您的应飚“"+bid.getTitle()+"”已被飚主接受，项目款项"+bid.getPrice()+"元已入账，请进入微信服务号“邂逅时刻”查看");
            response = SmsUtils.senMessage(bean);
            if(response.indexOf("error_response") != -1){
                logger.error(response);
            }

            //通知其他应飚者
            List<BidUserInfo> bidUserInfos = bidUserService.findNotWinBidUserList(bidUser);
            for(BidUserInfo user:bidUserInfos){
                bean = new SmsContentBean();
                bean.setTemplateCode("SMS_24880304");
                bean.setToMobile(user.getMobile());
                bean.setContent("{\"bidName\":\""+bid.getTitle()+"\"}");
                System.out.println("发短信给"+user.getMobile()+"感谢您参与"+bid.getTitle()+"的接飚，很遗憾未能中标，期待下次精彩合作！邂逅时刻");
                response = SmsUtils.senMessage(bean);
                if(response.indexOf("error_response") != -1){
                    logger.error(response);
                }
            }

        }
        return getSystemMessage(result);
    }
}
