package com.timeshare.controller.crowdfunding;

import com.timeshare.controller.BaseController;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.service.crowdfunding.EnrollService;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
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
    //预约成功后跳转关注页面
    @RequestMapping(value = "/toGz")
    public String toGz() {
        return "crowdfunding/gz";
    }
    //我预约的众筹页面
    @RequestMapping(value = "/toCrowdfundingByMyEnroll")
    public String toCrowdfundingByMyEnroll() {
        return "crowdfunding/wyydzc";
    }
    @RequestMapping(value = "/toCrowdfundingByMyEnrollToPaging")
    public String toCrowdfundingByMyEnrollToPaging(@RequestParam String crowdFundingId,Model model) {
        model.addAttribute("crowdFundingId",crowdFundingId);
        return "crowdfunding/bmmd";
    }

    //预约页面
    @RequestMapping(value = "/yy")
    public String yy(@RequestParam String crowdFundingId,@RequestParam(required = false) String enrollId, Model model,@CookieValue(value="time_sid", defaultValue="00359e8721c44d168aac7d501177e314") String userId)  {
        String crowdFundingIdTemp=crowdFundingId;
        //从支付返回回来的需要获取一次预约信息展示到页面
        if(StringUtils.isNotBlank(enrollId)){
            Enroll enroll=enrollService.findEnrollById(enrollId);
            model.addAttribute("enroll",enroll);
            crowdFundingIdTemp=enroll.getCrowdfundingId();
        }
        //根据购买用户ID判断用户是否已经购买过
        model.addAttribute("isAlreadyBuy",enrollService.enrollUserIdIsAlreadyBuy(userId,crowdFundingId)+"");

        CrowdFunding crowdFunding=crowdFundingService.findCrowdFundingDetailByCrowdfundingId(crowdFundingIdTemp);
        model.addAttribute("crowdFunding",crowdFunding);
        return "crowdfunding/yy";
    }

    @ResponseBody
    @RequestMapping(value = "/save")
    public String save(Enroll enroll, @CookieValue(value="time_sid", defaultValue="00359e8721c44d168aac7d501177e314") String userId, Model model) {
        try{

            if(enroll!=null && StringUtils.isNotBlank(enroll.getCrowdfundingId())){
                CrowdFunding crowdFunding=crowdFundingService.findCrowdFundingToPay(enroll.getCrowdfundingId());
                if(crowdFunding!=null){

                    String curriculumEndTime=crowdFunding.getCurriculumEndTime();
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH");
                    Date curriculumEndTimeDate=sdf.parse(curriculumEndTime);
                    if(curriculumEndTimeDate.compareTo(new Date())>=0){
                        //已报名数量+本次报名数量
                        int quantity=crowdFunding.getEnrollCount()+Integer.parseInt(enroll.getQuantity());
                        if(quantity<=crowdFunding.getMaxPeoples()){
                            if(Contants.CROWD_FUNDING_STATUS.RELEASED.name().equals(crowdFunding.getCrowdfundingStatus())){
                                enroll.setUserId(userId);
                                enroll.setEnrollUserId(userId);
                                enroll.setOptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                //保存报名信息
                                String pk= enrollService.saveEnroll(enroll);
                                return pk;
                            }
                            else{
                                //众筹项目未发布
                                return "NOT_RELEASED";
                            }
                        }
                        else{
                            //报名人数已满
                            return "MAX_PEOPLES_OUT";
                        }
                    }
                    else{
                        //众筹项目已到期
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
    //支付确认页面
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

        //应付预约费用
        BigDecimal payAmount=crowdFunding.getReservationCost().multiply(BigDecimal.valueOf(Long.parseLong(enroll.getQuantity())));
        String payMessageTitle = "金额："+payAmount;
        //封装支付参数
        String jsApiParams = WxPayUtils.userPayToCorp(code,payMessageTitle,payAmount,outTradeNo);

        attr.addAttribute("jsApiParams",jsApiParams);
        attr.addAttribute("payTip","你确定要支付"+payAmount+"元吗");
        attr.addAttribute("okUrl",request.getContextPath()+"/enroll/payComplete/"+enrollId);
        attr.addAttribute("backUrl",request.getContextPath()+"/enroll/yy?enrollId="+enrollId+"&crowdFundingId=");

        return "redirect:/wxPay/to-pay/";
    }
    //支付成功返回
    @RequestMapping(value = "/payComplete/{enrollId}")
    public String payComplete(@PathVariable String enrollId, Model model,
                                  @CookieValue(value="time_sid", defaultValue="00359e8721c44d168aac7d501177e314") String userId,HttpServletRequest request) {
        Enroll enroll=new Enroll();
        enroll.setEnrollId(enrollId);
        enroll.setPayStatus(Contants.ENROLL_PAY_STATUS.PAYED.name());
//        enroll.setUserId(userId);
//        enroll.setEnrollUserId(userId);
//        enroll.setOptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //更新支付状态为已支付
        enrollService.modifyEnroll(enroll);
        System.out.println("payComplete--------------------------------------------------------------userID:"+userId);
        //支付成功跳转至我预约的众筹
        return "crowdfunding/gz";
    }

    //导出报名名单
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
    public List<Enroll> findCrowdfundingByMyEnroll(@RequestParam int startIndex,@RequestParam int loadSize,@CookieValue(value="time_sid", defaultValue="00359e8721c44d168aac7d501177e314") String userId) {
        try{
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
