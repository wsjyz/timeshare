package com.timeshare.controller;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.timeshare.domain.*;
import com.timeshare.service.AuditorService;
import com.timeshare.service.BidService;
import com.timeshare.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by user on 2016/9/26.
 */
@Controller
@RequestMapping(value = "/bid")
public class BidController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(BidController.class);

    @Autowired
    BidService bidService;
    @Autowired
    AuditorService auditorService;

    public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/";

    @RequestMapping(value = "/to-add")
    public String toAdd(){
        return "bid/addbid";
    }

    @RequestMapping(value = "/to-pay-for-bid")
    public String toPayForBid(HttpServletRequest request,RedirectAttributes attr,
                        @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {

        String bidId = request.getParameter("state");
        Bid bid = new Bid();
        if(StringUtils.isNotBlank(bidId)){
            bid = bidService.findBidById(bidId);
        }
        String code = request.getParameter("code");
        String payMessageTitle = "您在邂逅时刻的发飙款项："+bid.getTitle() ;

        String jsApiParams = WxPayUtils.userPayToCorp(code,payMessageTitle,bid.getPrice());
        attr.addAttribute("jsApiParams",jsApiParams);
        attr.addAttribute("payTip","你确定要支付"+bid.getPrice()+"元吗");
        attr.addAttribute("backUrl",request.getContextPath()+"/bid/modify-bid-status?bidId="+bidId+"&bidStatus=ongoing");
        return "redirect:/wxPay/to-pay/";
    }
    @RequestMapping(value = "/modify-bid-status")
    public String save(String bidId,String bidStatus,
                       @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId,Model model) {
        Bid bid = bidService.findBidById(bidId);
        bid.setBidStatus(bidStatus);
        saveBid(bid,userId);
        return "redirect:/bid/to-list?pageContentType=mybid";
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
            params.setBidStatus(Contants.BID_STATUS.finish.toString());
        }
        List<Bid> bidList = bidService.findBidList(params,startIndex,loadSize);
        return bidList;
    }

    @RequestMapping(value = "/to-audit")
    public String toAudit(Model model,HttpServletRequest request,
                          @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {
        String bidId = request.getParameter("state");
        //判断是否支付过
        Auditor auditor = auditorService.findAuditorByBidIdAndUserId(bidId,userId);
        if(auditor != null){
            return "redirect:/bidsubmit/to-submit/"+bidId+"?audit=audit";
        }

        Bid bid = bidService.findBidById(bidId);
        UserInfo userInfo = getCurrentUser(userId);

        String code = request.getParameter("code");
        WeixinOauth weixinOauth = new WeixinOauth();
        String openId = weixinOauth.obtainOpenId(code);

        request.setAttribute("openId",openId);
        WxPayConfigBean config = new WxPayConfigBean();
        SortedMap parameters = new TreeMap<>();

        config.setAppid(Contants.APPID);
        parameters.put("appid",Contants.APPID);

        config.setMch_id(Contants.MCHID);
        parameters.put("mch_id",Contants.MCHID);

        String noceStr = CommonStringUtils.genPK();
        config.setNonce_str(noceStr);
        parameters.put("nonce_str",noceStr);

        String bodyStr = bid.getTitle() + "|" + userInfo.getNickName();
        config.setBody(bodyStr);
        parameters.put("body",bodyStr);

        String outTradeNo = CommonStringUtils.gen18RandomNumber();
        config.setOut_trade_no(outTradeNo);
        parameters.put("out_trade_no",outTradeNo);


        int fenPrice = (bid.getPrice().multiply(new BigDecimal(100)).multiply(new BigDecimal("0.3"))).intValue();
        System.out.println(" 价格为 "+fenPrice);
        config.setTotal_fee(fenPrice);
        parameters.put("total_fee",fenPrice);

        //ip sbwx
        config.setSpbill_create_ip("123.0.1.2");
        parameters.put("spbill_create_ip","123.0.1.2");

        config.setTrade_type("JSAPI");
        parameters.put("trade_type","JSAPI");

        config.setNotify_url("http://jk.zhangqidong.cn/time/wxPay/notify-url");
        parameters.put("notify_url","http://jk.zhangqidong.cn/time/wxPay/notify-url");

        config.setOpenid(openId);
        parameters.put("openid",openId);

        parameters.put("key",Contants.KEY);

        String signStr = WxUtils.createSign(parameters,Contants.KEY);
        config.setSign(signStr);

        XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xs.processAnnotations(config.getClass());
        String xml = xs.toXML(config);
        System.out.println("xml is "+xml);

        HTTPSClient client = new HTTPSClient();
        client.setSERVER_HOST_URL(UNIFIEDORDER_URL);
        client.setServiceUri("unifiedorder");
        try {
            client.setBodyParams(new String(xml.getBytes("UTF-8"),"ISO-8859-1"));//狗日的微信，神经病
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String response = client.request();
        System.out.println("response is "+response);


        WxResponseBean responseBean = new WxResponseBean();
        xs.processAnnotations(responseBean.getClass());
        responseBean = (WxResponseBean)xs.fromXML(response);
        String prepayId = "";
        String payStatus = "NOTPAY";
        String jsApiParams = "\'\'";
        if(responseBean.getReturn_code() != null && responseBean.getReturn_code().equals("SUCCESS")){

            if(responseBean.getResult_code() != null && responseBean.getResult_code().equals("FAIL")){
                if(responseBean.getErr_code().equals("ORDERPAID")){//已支付过了
                    payStatus = "ORDERPAID";
                }

            }else if(responseBean.getResult_code() != null && responseBean.getResult_code().equals("SUCCESS")){
                prepayId = responseBean.getPrepay_id();
                SortedMap signMap = new TreeMap<>();
                signMap.put("appId",Contants.APPID);
                String timestampStr = System.currentTimeMillis()+"";
                signMap.put("timeStamp",timestampStr);
                String randomStr = CommonStringUtils.genPK();
                signMap.put("nonceStr",randomStr);
                signMap.put("package","prepay_id="+prepayId);
                signMap.put("signType","MD5");

                String paySign = WxUtils.createSign(signMap,Contants.KEY);
                signMap.put("paySign",paySign);
                jsApiParams = JSON.toJSONString(signMap);
                System.out.println("jsApiParams "+jsApiParams);

            }

        }
        model.addAttribute("jsApiParams",jsApiParams);
        model.addAttribute("payStatus",payStatus);
        model.addAttribute("tradeNo",outTradeNo);


        model.addAttribute("bidId",bidId);
        model.addAttribute("auditPrice",bid.getPrice().multiply(new BigDecimal("0.3")));
        return "bid/addauditor";
    }

    @ResponseBody
    @RequestMapping(value = "/save-auditor")
    public SystemMessage saveAuditor(String bidId,String wxTradeNo,@CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {
        Bid bid = bidService.findBidById(bidId);
        UserInfo seller = getCurrentUser(bid.getUserId());
        UserInfo buyer = getCurrentUser(userId);
        Auditor auditor = new Auditor();
        auditor.setBidId(bidId);
        auditor.setCreateUserName(buyer.getNickName());
        auditor.setUserId(userId);
        auditor.setWxTradeNo(wxTradeNo);
        int fenFee = FeeUtils.payAmount(bid.getPrice(),new BigDecimal("0.7"));
        auditor.setFee(fenFee);
        String result = auditorService.saveAuditor(auditor);
        SystemMessage message = getSystemMessage(result);
        //付款到相应的人
        logger.info("付款给"+seller.getNickName());
        WxPayUtils.payToSeller(wxTradeNo,bid.getPrice().multiply(new BigDecimal("0.3")),seller.getOpenId());
        return message;
    }

}
