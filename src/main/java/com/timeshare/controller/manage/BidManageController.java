package com.timeshare.controller.manage;


import com.timeshare.domain.Bid;
import com.timeshare.service.BidService;
import com.timeshare.service.BidService;
import com.timeshare.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

/**
 * Created by user on 2016/7/12.
 */
@Controller
@RequestMapping(value = "/manager/bid")
public class BidManageController {

    @Autowired
    BidService bidService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Bid> findBidList(@RequestParam String bidStatus, @RequestParam int startIndex, @RequestParam int loadSize) {


        Bid parms = new Bid();
        if(StringUtils.isNotBlank(bidStatus) && !bidStatus.equals("all")){
            parms.setBidStatus(bidStatus);
        }
        //parms.setBidStatus(Contants.ITEM_STATUS.apply_for_online.toString());
        List<Bid> bidList = bidService.findBidListForManage(parms,startIndex,loadSize);
        return bidList;
    }
    @RequestMapping(value = "/modify")
    @ResponseBody
    public String modify(Bid bid) {
        String result = "failed";
        if(StringUtils.isNotBlank(bid.getBidStatus()) && StringUtils.isNotBlank(bid.getBidId())){
            bid.setOptTime(bid.getLastModifyTime());
            result = bidService.modifyBid(bid);

        }
        return result;
    }


    @RequestMapping(value = "/to-bid-manage")
    public String toBidList(Model model){
        return "manager/bid";
    }


    @RequestMapping(value = "/get-bid")
    @ResponseBody
    public Bid getBidById(@RequestParam String bidId) {
        Bid bid = bidService.findBidById(bidId);
        if(bid == null){
            bid = new Bid();
        }
        return bid;
    }
}
