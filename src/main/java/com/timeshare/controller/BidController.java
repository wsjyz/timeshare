package com.timeshare.controller;

import com.timeshare.domain.Bid;
import com.timeshare.domain.SystemMessage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.BidService;
import com.timeshare.utils.Contants;
import com.timeshare.utils.WxUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public String bidList(@RequestParam String pageContentType,Model model) {
        model.addAttribute("pageContentType",pageContentType);
        return "bid/myBidList";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Bid> findItemList(@RequestParam String pageContentType,@RequestParam int startIndex, @RequestParam int loadSize,
                                   @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {
        List<Bid> bidList = new ArrayList<Bid>();
        Bid parms = new Bid();
        parms.setUserId(userId);
        parms.setPageContentType(pageContentType);
        if(pageContentType.equals("mysubmit")){

            parms.setBidUserId(userId);
        }
        bidList = bidService.findBidList(parms,startIndex,loadSize);
        return bidList;
    }

    @RequestMapping(value = "/to-view/{bidId}")
    public String toView(@PathVariable String bidId, Model model, @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {
        String returnStr = "";
        Bid bid = bidService.findBidById(bidId);
        if(bid != null){
            model.addAttribute("bid", bid);

            if(StringUtils.isNotBlank(userId) && userId.equals(bid.getUserId())){
                returnStr = "bid/addbid";
            }

        }

        return returnStr;
    }

    @RequestMapping(value = "/to-index")
    public String toBidIndex() {
        return "bid/bidindex";
    }

    @RequestMapping(value = "/list-by-condition")
    @ResponseBody
    public List<Bid> listByCondition(@RequestParam String condition ,@RequestParam int startIndex, @RequestParam int loadSize) {
        Bid params = new Bid();
        if(condition.equals("all")){
            params.setBidStatus(Contants.BID_STATUS.ongoing.toString());
        }else if(condition.equals("audit")){
            params.setCanAudit("1");
            params.setBidStatus(Contants.BID_STATUS.ongoing.toString());
        }else if(condition.equals("complete")){
            params.setBidStatus(Contants.BID_STATUS.ongoing.toString());
        }
        List<Bid> bidList = bidService.findBidList(params,startIndex,loadSize);
        return bidList;
    }
}
