package com.timeshare.controller;

import com.timeshare.domain.Bid;
import com.timeshare.domain.BidUser;
import com.timeshare.domain.SystemMessage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.BidService;
import com.timeshare.service.BidUserService;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import com.timeshare.utils.WxPayUtils;
import org.apache.commons.lang3.StringUtils;
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
    public List<BidUser> findList(@RequestParam int startIndex, @RequestParam int loadSize,String bidId) {

        BidUser bidUser = new BidUser();
        //bidUser.setUserId(userId);
        bidUser.setBidId(bidId);
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
                bidService.modifyBid(bid);
                bidUser.setIncomeFee(bid.getPrice());
            }
            String outTradeNo = CommonStringUtils.gen18RandomNumber();
            bidUser.setWxTradeNo(outTradeNo);
            result = bidUserService.modifyBidUser(bidUser);
            String bidUserId = bidUser.getUserId();
            UserInfo bidUserInfo = getCurrentUser(bidUserId);
            WxPayUtils.payToSeller(outTradeNo,bid.getPrice(),bidUserInfo.getOpenId());
        }
        return getSystemMessage(result);
    }
}
