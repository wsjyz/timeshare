package com.timeshare.controller.crowdfunding;

import com.sun.tools.internal.jxc.ap.Const;
import com.timeshare.controller.BaseController;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.service.crowdfunding.EnrollService;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import com.timeshare.utils.WeixinOauth;
import com.timeshare.utils.WxPayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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


    @RequestMapping(value = "/toCrowdfundingByMyEnroll")
    public String toCrowdfundingByMyEnroll() {
        return "crowdfunding/wyydzc";
    }
    @RequestMapping(value = "/toCrowdfundingByMyEnrollToPaging")
    public String toCrowdfundingByMyEnrollToPaging(@RequestParam String crowdFundingId,Model model) {
        model.addAttribute("crowdFundingId",crowdFundingId);
        return "crowdfunding/bmmd";
    }


    @RequestMapping(value = "/yy")
    public String yy(@RequestParam String crowdFundingId,@RequestParam String enrollId, Model model)  {
        String crowdFundingIdTemp=crowdFundingId;
        //从支付返回 回来的需要获取一次预约信息展示到页面
        if(StringUtils.isNotBlank(enrollId)){
            Enroll enroll=enrollService.findEnrollById(enrollId);
            model.addAttribute("enroll",enroll);
            crowdFundingIdTemp=enroll.getCrowdfundingId();
        }
        CrowdFunding crowdFunding=crowdFundingService.findCrowdFundingDetailByCrowdfundingId(crowdFundingIdTemp);
        model.addAttribute("crowdFunding",crowdFunding);
        return "crowdfunding/yy";
    }

    @ResponseBody
    @RequestMapping(value = "/save")
    public String save(Enroll enroll, @CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        try{

            if(enroll!=null && StringUtils.isNotBlank(enroll.getCrowdfundingId())){
                CrowdFunding crowdFunding=crowdFundingService.findCrowdFundingToPay(enroll.getCrowdfundingId());
                if(crowdFunding!=null){

                    String curriculumEndTime=crowdFunding.getCurriculumEndTime();
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date curriculumEndTimeDate=sdf.parse(curriculumEndTime);
                    if(curriculumEndTimeDate.compareTo(new Date())>=0){
                        if(crowdFunding.getEnrollCount()<crowdFunding.getMaxPeoples()){
                            if(Contants.CROWD_FUNDING_STATUS.RELEASED.name().equals(crowdFunding.getCrowdfundingStatus())){
                                //MOCK
                                userId="00359e8721c44d168aac7d501177e314";
                                enroll.setUserId(userId);
                                enroll.setEnrollUserId(userId);
                                enroll.setOptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                String pk= enrollService.saveEnroll(enroll);
                                model.addAttribute("enrollId",pk);
                                return Contants.SUCCESS;
                            }
                            else{
                                return "NOT_RELEASED";
                            }
                        }
                        else{
                            return "MAX_PEOPLES_OUT";
                        }
                    }
                    else{
                        return "TIME_OUT";
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Contants.FAILED;
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
        attr.addAttribute("backUrl",request.getContextPath()+"/enroll/yy?enrollId="+enrollId+"&crowdFundingId=");

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

    @ResponseBody
    @RequestMapping(value = "/exportEnrollListToEmail")
    public String exportEnrollListToEmail(@RequestParam String crowdfundingId,@RequestParam String toEmailAddress) throws IOException {
        if(StringUtils.isNotBlank(toEmailAddress) && StringUtils.isNotBlank(crowdfundingId)){
            Boolean result= enrollService.exportEnrollListToEmail(crowdfundingId,toEmailAddress);
            return result?Contants.SUCCESS:Contants.FAILED;
        }
        else{
            return Contants.FAILED;
        }

    }

    //我预约的众筹
    @ResponseBody
    @RequestMapping(value = "/crowdfundingByMyEnroll")
    public List<Enroll> findCrowdfundingByMyEnroll(@RequestParam int startIndex,@RequestParam int loadSize,@CookieValue(value="time_sid", defaultValue="") String userId) {
        try{
            //MOCK
            userId="00359e8721c44d168aac7d501177e314";
            //下架
            return enrollService.findCrowdfundingByMyEnroll(startIndex,loadSize,userId);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //获取已报名名单
    @ResponseBody
    @RequestMapping(value = "/findCrowdfundingEnrollListToPaging")
    public List<Enroll> findCrowdfundingEnrollListToPaging(@RequestParam String crowdfundingId,@RequestParam int startIndex,@RequestParam int loadSize){
        try{
            return enrollService.findCrowdfundingEnrollListToPaging(crowdfundingId,startIndex,loadSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
