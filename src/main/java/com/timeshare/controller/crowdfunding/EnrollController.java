package com.timeshare.controller.crowdfunding;

import com.timeshare.controller.BaseController;
import com.timeshare.domain.Feedback;
import com.timeshare.domain.ItemOrder;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.service.crowdfunding.EnrollService;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import com.timeshare.utils.WeixinOauth;
import com.timeshare.utils.WxPayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/9/26.
 */
@Controller
@RequestMapping(value = "/enroll")
public class EnrollController extends  BaseController{


    @Autowired
    UserService userService;
    @Autowired
    private EnrollService enrollService;
    @Autowired
    private CrowdFundingService crowdFundingService;

    protected Logger logger = LoggerFactory.getLogger(EnrollController.class);


    @RequestMapping(value = "/goToEnroll")
    public String goToEnroll(@CookieValue(value="time_sid", defaultValue="") String userId, Model model)  {
        return "crowdfunding/crowdfundingindex";
    }

    @ResponseBody
    @RequestMapping(value = "/save")
    public String save(Enroll enroll, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        try{
            enroll.setOptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String pk= enrollService.saveEnroll(enroll);
            return pk;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/toEnroolPayConfirm")
    public String toPayForConfirm(HttpServletRequest request, RedirectAttributes attr) {

        String code = request.getParameter("code");
        String enrollId = request.getParameter("state");
        Enroll enroll=enrollService.findEnrollById(enrollId);
        CrowdFunding crowdFunding = crowdFundingService.findCrowdFundingById(enroll.getCrowdfundingId());

        //生成支付交易号
        String outTradeNo = CommonStringUtils.gen18RandomNumber();
        enroll.setPayTradeNo(outTradeNo);
        enrollService.modifyEnroll(enroll);


        String payMessageTitle = "金额："+crowdFunding.getReservationCost();
        String jsApiParams = WxPayUtils.userPayToCorp(code,payMessageTitle,crowdFunding.getReservationCost(),outTradeNo);
        System.out.println("crowdFunding.getReservationCost():"+crowdFunding.getReservationCost());

        attr.addAttribute("jsApiParams",jsApiParams);
        attr.addAttribute("payTip","你确定要支付"+crowdFunding.getReservationCost()+"元吗");
        attr.addAttribute("okUrl",request.getContextPath()+"/enroll/payComplete/"+enrollId);
        attr.addAttribute("backUrl",request.getContextPath()+"/enroll/payCallback?enrollId="+enrollId);

        return "redirect:/wxPay/to-pay/";
    }
    @RequestMapping(value = "/payComplete/{enrollId}")
    public String payComplete(@PathVariable String enrollId, Model model,
                                  @CookieValue(value="time_sid", defaultValue="admin") String userId,HttpServletRequest request) {
        String bidId = request.getParameter("bidId");
        String wxTradeNo = request.getParameter("wxTradeNo");
        System.out.println("payComplete ....   enrollId:"+enrollId+"           wxTradeNo:"+wxTradeNo);

        Enroll enroll=new Enroll();
        enroll.setEnrollId(enrollId);
        enroll.setPayStatus(Contants.ENROLL_PAY_STATUS.PAYED.name());
        //更新支付状态为已支付
        enrollService.modifyEnroll(enroll);

        return "info";
    }
    @RequestMapping(value = "/payCallback")
    public String payCallback(String crowdfundingId,HttpServletRequest request) {
        System.out.println("payCallback....   crowdfundingId:"+crowdfundingId);
        return "error";

//        ItemOrder order = orderService.findOrderByOrderId(orderId);
//        order.setOrderStatus(bidStatus);
//        orderService.saveOrder(order);
        //return "redirect:"+request.getContextPath()+"/to-buyer-confirm/"+orderId;
    }
    @RequestMapping(value = "/payToSeller")
    public String payToSeller(String wxTradeNo,HttpServletRequest request) {
        String code = request.getParameter("code");
        String enrollId = request.getParameter("state");
        System.out.println("payToSeller... code:"+code);
        WeixinOauth weixinOauth = new WeixinOauth();
        String openId = weixinOauth.obtainOpenId(code);
        System.out.println("payToSeller... openId:"+openId);
        String result=WxPayUtils.payToSeller("H20170413224728844",new BigDecimal(1.01),openId);

        System.out.println("payToSeller....   result:"+result);
        return "info";

//        ItemOrder order = orderService.findOrderByOrderId(orderId);
//        order.setOrderStatus(bidStatus);
//        orderService.saveOrder(order);
        //return "redirect:"+request.getContextPath()+"/to-buyer-confirm/"+orderId;
    }

    @RequestMapping(value = "/refund")
    public String refund(String wxTradeNo,HttpServletRequest request) {
        String code = request.getParameter("code");
        String enrollId = request.getParameter("state");
        System.out.println("payToSeller... code:"+code);
        WeixinOauth weixinOauth = new WeixinOauth();
        String openId = weixinOauth.obtainOpenId(code);
        System.out.println("payToSeller... openId:"+openId);


        String outRefundNo = CommonStringUtils.genPK();
        String result=WxPayUtils.payRefund(outRefundNo,"S20170413231658734",1,1);

        System.out.println("refund....   result:"+result);
        return "info";

//        ItemOrder order = orderService.findOrderByOrderId(orderId);
//        order.setOrderStatus(bidStatus);
//        orderService.saveOrder(order);
        //return "redirect:"+request.getContextPath()+"/to-buyer-confirm/"+orderId;
    }



    @RequestMapping(value = "/listByOwner")
    public String listByOwner(Enroll enroll, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        userId="00359e8721c44d168aac7d501177e314";
        List<Enroll> enrollList= enrollService.findEnrollByOwner(userId,0,10);
        for (Enroll enrollItem: enrollList) {
            System.out.println(enrollItem.getUserName()+"---"+enrollItem.getPhone()+"---"+enrollItem.getCorpName());
        }
        return "info";
    }
    @RequestMapping(value = "/exportEnrollListToEmail")
    public String exportEnrollListToEmail(Enroll enroll, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) throws IOException {
        String crowdfundingId="dbbc8b43a3764dd4afae8d5b16228124";
        String toEmailAddress="254105316@qq.com";
        Boolean result= enrollService.exportEnrollListToEmail(crowdfundingId,toEmailAddress);
        System.out.println(result);
        return "info";
    }

}
