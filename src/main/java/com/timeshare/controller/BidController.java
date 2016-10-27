package com.timeshare.controller;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.timeshare.domain.*;
import com.timeshare.service.AuditorService;
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
    protected Logger payLogger = LoggerFactory.getLogger("payLogger");

    @Autowired
    BidService bidService;
    @Autowired
    AuditorService auditorService;
    @Autowired
    BidUserService bidUserService;

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
        //TODO 这里有一个漏洞，可以通过http工具查看到url，借此可以控制bidStatus
        attr.addAttribute("okUrl",request.getContextPath()+"/bid/modify-bid-status?bidId="+bidId+"&bidStatus=ongoing");
        attr.addAttribute("backUrl",request.getContextPath()+"/bid/modify-bid-status?bidId="+bidId+"&bidStatus=draft");
        return "redirect:/wxPay/to-pay/";
    }
    @RequestMapping(value = "/modify-bid-status")
    public String modifyBidStatus(String bidId,String bidStatus,
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
    @RequestMapping(value = "/save")
    public String save(Bid bid,@CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId,Model model) {
        SystemMessage message = saveBid(bid,userId);
        model.addAttribute("message",message);
        model.addAttribute("jumpUrl","/bid/to-list?pageContentType=mybid");
        return "info";
    }

    private SystemMessage saveBid(Bid bid, String bidCreatUserId){
        if(bid != null){
            UserInfo seller = getCurrentUser(bidCreatUserId);
            bid.setCreateUserName(seller.getNickName());
            bid.setUserId(bidCreatUserId);
            if(bid.getCanAudit() == null){
                bid.setCanAudit("0");
            }
            String result = "";

            if(StringUtils.isNotBlank(bid.getBidId())){
                result = bidService.modifyBid(bid);
            }else{
                if(StringUtils.isNotBlank(bid.getTitle()) && bid.getPrice() != null){
                    result = bidService.saveBid(bid);
                }

            }
            if(bid.getBidStatus().equals(Contants.BID_STATUS.ongoing.toString())){
                //修改支出
                seller.setSumCost(seller.getSumCost().add(bid.getPrice()));
                userService.modifyUser(seller);
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
        if(pageContentType.equals(Contants.PAGE_CONTENT_TYPE.mybid.toString())){
            parms.setUserId(userId);
        }

        parms.setPageContentType(pageContentType);
        if(pageContentType.equals(Contants.PAGE_CONTENT_TYPE.mysubmit.toString())){
            parms.setBidUserId(userId);
        }
        if(pageContentType.equals(Contants.PAGE_CONTENT_TYPE.myaudit.toString())){

            parms.setAuditUserId(userId);
            parms.setBidStatus(Contants.BID_STATUS.finish.toString());
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
    @ResponseBody
    @RequestMapping(value = "/find-bid/{bidId}")
    public Bid findBidById(@PathVariable String bidId) {

        Bid bid = bidService.findBidById(bidId);

        return bid;
    }

    @RequestMapping(value = "/to-share-view/{bidId}")
    public String toShare(@PathVariable String bidId,Model model,
                          @CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId,
                          HttpServletRequest request) {
        model.addAttribute("currentUserId", userId);
        Bid bid = bidService.findBidById(bidId);
        String bidCreatorHeadImg = getCurrentUser(bid.getUserId()).getHeadImgPath();
        model.addAttribute("bid",bid);
        model.addAttribute("bidCreatorHeadImg",bidCreatorHeadImg);

        //微信jssdk相关代码
        String url = WxUtils.getUrl(request);
        Map<String,String> parmsMap = WxUtils.sign(url);
        model.addAttribute("parmsMap",parmsMap);
        return "bid/sharebid";
    }

    @RequestMapping(value = "/to-index")
    public String toBidIndex(Model model,@CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {
        model.addAttribute("currentUserId", userId);
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
            params.setBidStatus(Contants.BID_STATUS.finish.toString());
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


        int fenPrice = 300;
        if(bid.getPrice().compareTo(new BigDecimal("10")) == 1){
           fenPrice = (bid.getPrice().multiply(new BigDecimal(100)).multiply(new BigDecimal("0.3"))).intValue();
        }
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
        if(bid.getPrice().compareTo(new BigDecimal("10")) == 1){
            model.addAttribute("auditPrice",bid.getPrice().multiply(new BigDecimal("0.3")));
        }else{
            model.addAttribute("auditPrice",3);
        }

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

        //计算旁听需要支付的费用
        BigDecimal payPrice = new BigDecimal("3");
        if(bid.getPrice().compareTo(new BigDecimal("10")) == 1){//大于10元
            payPrice = bid.getPrice().multiply(new BigDecimal("0.3"));
        }
        auditor.setFee(payPrice.multiply(new BigDecimal("100")).intValue());

        String result = auditorService.saveAuditor(auditor);
        SystemMessage message = getSystemMessage(result);
        //付款到相应的人
        payLogger.info("旁听费付款给发飙人"+seller.getNickName());
        BigDecimal percentageFee = payPrice.multiply(new BigDecimal("0.5"));
        WxPayUtils.payToSeller(wxTradeNo,percentageFee,seller.getOpenId());
        //修改发飙人收入
        seller.setIncome(seller.getIncome().add(percentageFee));
        userService.modifyUser(seller);
        //发短信给发飙人
        SmsContentBean bean = new SmsContentBean();
        bean.setTemplateCode("SMS_21390019");
        bean.setToMobile(seller.getMobile());
        bean.setContent("{\"bidName\":\""+bid.getTitle()+"\",\"bidPrice\":\""+percentageFee+"\"}");
        System.out.println("您发的飚“"+bid.getTitle()+"”有人旁听了，项目款项"+percentageFee+"元已入账，请进入微信服务号“邂逅时刻”查看");
        String response = SmsUtils.senMessage(bean);
        if(response.indexOf("error_response") != -1){
            logger.error(response);
        }

        //查找成功应飚人
        UserInfo winUser = new UserInfo();
        BidUser bidUser = new BidUser();
        bidUser.setWinTheBid("1");
        bidUser.setBidId(bidId);
        List<BidUser> winUserList = bidUserService.findBidUserList(bidUser,0,1);
        if(winUserList != null){
            String winUserId = winUserList.get(0).getUserId();
            winUser = getCurrentUser(winUserId);
        }
        payLogger.info("旁听费付款给中标人"+winUser.getNickName());
        wxTradeNo = CommonStringUtils.gen18RandomNumber();
        WxPayUtils.payToSeller(wxTradeNo,percentageFee,winUser.getOpenId());
        //修改收入
        winUser.setIncome(winUser.getIncome().add(percentageFee));
        userService.modifyUser(winUser);
        //发短信给中标人
        bean = new SmsContentBean();
        bean.setTemplateCode("SMS_21700251");
        bean.setToMobile(winUser.getMobile());
        bean.setContent("{\"bidName\":\""+bid.getTitle()+"\",\"bidPrice\":\""+percentageFee+"\"}");
        System.out.println("您的应飚“"+bid.getTitle()+"”有人旁听了，项目款项"+percentageFee+"元已入账，请进入微信服务号“邂逅时刻”查看");
        response = SmsUtils.senMessage(bean);
        if(response.indexOf("error_response") != -1){
            logger.error(response);
        }


        //修改旁听者支出
        buyer.setSumCost(buyer.getSumCost().add(payPrice));
        userService.modifyUser(buyer);
        return message;
    }

    @RequestMapping(value = "/to-search")
    public String toSearch(@RequestParam String keyword,Model model,@CookieValue(value="time_sid", defaultValue="c9f7da60747f4cf49505123d15d29ac4") String userId) {
        model.addAttribute("currentUserId", userId);
        model.addAttribute("keyword",keyword);
        return "bid/searchbid";
    }

    @RequestMapping(value = "/search")
    @ResponseBody
    public List<Bid> search(@RequestParam String keyword ,@RequestParam int startIndex, @RequestParam int loadSize) {
        Bid bid = new Bid();
        bid.setTitle(keyword);
        bid.setCreateUserName(keyword);
        List<Bid> bidList = bidService.searchBidList(bid,startIndex,loadSize);
        return bidList;
    }


}
