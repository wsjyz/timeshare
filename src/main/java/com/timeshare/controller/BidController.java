package com.timeshare.controller;

import com.timeshare.domain.Bid;
import com.timeshare.domain.SystemMessage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.BidService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/9/26.
 */
@Controller
@RequestMapping(value = "/bid")
public class BidController extends BaseController{

    @Autowired
    BidService bidService;

    @RequestMapping(value = "/to-add")
    public String toAdd() {

        return "bid/addbid";
    }
    @RequestMapping(value = "/save")
    public String save(Bid bid,@CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId,Model model) {
        SystemMessage message = saveBid(bid,userId);
        model.addAttribute("message",message);
        model.addAttribute("jumpUrl","/bid/to-list");
        return "info";
    }
    @ResponseBody
    @RequestMapping(value = "/save-async")
    public SystemMessage save(Bid bid, @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {
        SystemMessage message = saveBid(bid,userId);
        return message;
    }

    private SystemMessage saveBid(Bid bid, String userId){
        if(bid != null){
            UserInfo user = getCurrentUser(userId);
            bid.setCreateUserName(user.getNickName());
            bid.setUserId(userId);
            String result = "";
            //TODO 测试时为1分
            //item.setPrice(new BigDecimal("0.01"));
            if(StringUtils.isNotBlank(bid.getBidId())){
                result = bidService.modifyBid(bid);
            }else{
                if(StringUtils.isNotBlank(bid.getTitle()) && bid.getPrice() != null){
                    result = bidService.saveBid(bid);
                }

            }

           return getSystemMessage(result);
        }
        return null;
    }
    @RequestMapping(value = "/to-list")
    public String bidList() {
        return "bid/myBidList";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Bid> findItemList(@RequestParam int startIndex, @RequestParam int loadSize,
                                   @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {
        List<Bid> bidList = new ArrayList<Bid>();
        Bid parms = new Bid();
        parms.setUserId(userId);
        bidList = bidService.findBidList(parms,startIndex,loadSize);
        return bidList;
    }
}
