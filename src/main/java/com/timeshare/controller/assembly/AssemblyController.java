package com.timeshare.controller.assembly;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.timeshare.controller.BaseController;
import com.timeshare.domain.*;
import com.timeshare.domain.assembly.*;
import com.timeshare.domain.assembly.Collection;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.service.AuditorService;
import com.timeshare.service.BidService;
import com.timeshare.service.BidUserService;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.*;
import com.timeshare.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.SchemaOutputResolver;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.timeshare.utils.WxPayUtils.*;

/**
 * Created by user on 2016/9/26.
 */
@Controller
@RequestMapping(value = "/assembly")
public class AssemblyController extends  BaseController {

    @Autowired
    FeeService feeService;
    @Autowired
    UserService userService;
    @Autowired
    AssemblyService assemblyService;
    @Autowired
    AttenderService attenderService;
    @Autowired
    CommentService commentService;
    @Autowired
    CollectionService collectionService;
    protected Logger logger = LoggerFactory.getLogger(AssemblyController.class);

    @RequestMapping(value = "/to-index")
    public String index(@RequestParam(value = "searchName", defaultValue = "") String searchName,
                        @RequestParam(value = "type", defaultValue = "") String type, Model model, @CookieValue(value = "time_sid", defaultValue = "admin") String userId, HttpServletRequest request) {
        model.addAttribute("type", type);
        Assembly assembly = new Assembly();
        assembly.setStatus("PUBLISHED");
        assembly.setShowOldTime("true");
        assembly.setCarousel("Y");
        UserInfo userInfo = getCurrentUser(userId);
        model.addAttribute("userInfo",userInfo);

        List<Assembly> assemblyList = assemblyService.findAssemblyList(assembly, 0, 10);
        model.addAttribute("assemblyList", assemblyList);
        //微信jssdk相关代码
        String url = WxUtils.getUrl(request);
        Map<String, String> parmsMap = WxUtils.sign(url);
        model.addAttribute("parmsMap", parmsMap);
        return "assembly/index";
    }

    @RequestMapping(value = "/searchAssembly")
    public String searchAssembly(@RequestParam(value = "searchName", defaultValue = "") String searchName, @RequestParam(value = "citySelect", defaultValue = "") String citySelect, @RequestParam(value = "type", defaultValue = "") String type, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("citySelect", citySelect);
        model.addAttribute("searchName", searchName);

        return "assembly/tab-index";
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<Assembly> findList(@RequestParam(value = "searchName", defaultValue = "") String searchName, @RequestParam(value = "citySelect", defaultValue = "") String citySelect, @RequestParam(value = "type", defaultValue = "") String type,
                                   @RequestParam(value = "startIndex", defaultValue = "0") int startIndex, @RequestParam(value = "loadSize", defaultValue = "20") int loadSize, Model model) {
        Assembly assembly = new Assembly();
        assembly.setType(type);
        assembly.setTitle(searchName);
        assembly.setRendezvous(citySelect);
        assembly.setStatus("PUBLISHED");
        assembly.setShowOldTime("true");
        List<Assembly> assemblyList = assemblyService.findAssemblyList(assembly, startIndex, loadSize);
        return assemblyList;
    }

    @RequestMapping(value = "/to-add")
    public String add(@CookieValue(value = "time_sid", defaultValue = "admin") String userId,Model model,@RequestParam(value = "sendTo", defaultValue = "") String sendTo,@RequestParam(value = "userAccount", defaultValue = "") String userAccount) {
        UserInfo userInfo = getCurrentUser(userId);
        model.addAttribute("sendTo",sendTo);
        model.addAttribute("userAccount",userAccount);
        model.addAttribute("userInfo",userInfo);
        return "assembly/activityPublish";
    }

    @RequestMapping(value = "/saveFee")
    @ResponseBody
    public String saveFee(@RequestParam(value = "feeIds", defaultValue = "") String feeIds,@RequestParam String feeTitle, @RequestParam String feeStr, @RequestParam String quota) {
        String resultId = "";
        try {
            String[] feeIdStr = feeIds.split(",");
            String[] feeTitles = feeTitle.split(",");
            String[] fees = feeStr.split(",");
            String[] quotas = quota.split(",");
            for (int i = 0; i < feeTitles.length; i++) {
                Fee fee = new Fee();
                fee.setFeeTitle(feeTitles[i]);
                fee.setFee(new BigDecimal(fees[i]));
                fee.setQuota(Integer.parseInt(quotas[i]));
                String feeId="";
                if(feeIdStr.length>i && StringUtils.isNotEmpty(feeIdStr[i])){
                    feeId=feeIdStr[i];
                    fee.setFeeId(feeId);
                    feeService.modifyFee(fee);
                }else{
                    feeId = feeService.saveFee(fee);
                }
                resultId += feeId + ",";
            }
            List<Fee> feeList = feeService.findFeeByAssemblyId(null);
            if (!CollectionUtils.isEmpty(feeList)) {
                for (Fee fee : feeList) {
                    feeService.delete(fee.getFeeId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultId = "ERROR";
        }

        return resultId;
    }

    @RequestMapping(value = "/saveAssembly")
    @ResponseBody
    public String saveAssembly(@RequestParam(value = "assemblyId", defaultValue = "") String assemblyId,@RequestParam String assemblyTitle, @RequestParam String imageIdStr, @RequestParam String startTime, @RequestParam String endTime, @RequestParam String rendezvous,
                               @RequestParam String description,@RequestParam String assemblyType,@RequestParam String feeId,@RequestParam String onApply,@RequestParam String phoneNumber,
                               @RequestParam String applyId,@RequestParam String imageIdStrCon, @CookieValue(value = "time_sid", defaultValue = "admin") String userId,HttpServletRequest request,@RequestParam(value = "sendTo", defaultValue = "") String sendTo
    ,@RequestParam(value = "userAccount", defaultValue = "") String userAccount) {
        String resultId = "";
        try {
            Assembly assembly = new Assembly();

            assembly.setTitle(assemblyTitle);
            assembly.setStartTime(startTime);
            assembly.setEndTime(endTime);
            assembly.setRendezvous(rendezvous);
            assembly.setDescription(description);
            assembly.setType(assemblyType);
            assembly.setShowApplyProblem(applyId);
            assembly.setUserId(userId);
            if (onApply.equals("true")){
                assembly.setIsOnApply(onApply);
            }else{
                assembly.setIsOnApply("false");
            }
            assembly.setPhoneNumber(phoneNumber);
            assembly.setStatus("PUBLISHED");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            assembly.setCreateTime(sdf.format(cal.getTime()));
            if (StringUtils.isNotEmpty(assemblyId)){
                assembly.setAssemblyId(assemblyId);
                assemblyService.modifyAssembly(assembly);
            }else{
                assemblyId = assemblyService.saveAssembly(assembly);
            }
            String imageTitleUrl="";
            if (StringUtils.isNotEmpty(imageIdStr)) {
                ImageObj imageObj = userService.findById(imageIdStr);
                imageObj.setObjId(assemblyId);
                imageTitleUrl=imageObj.getImageUrl();
                userService.saveOrUpdateImg(imageObj);
            }
            if (StringUtils.isNotEmpty(imageIdStrCon)) {
                ImageObj imageObj = userService.findById(imageIdStrCon);
                imageObj.setObjId(assemblyId);
                userService.saveOrUpdateImg(imageObj);
            }
            if (StringUtils.isNotEmpty(feeId)) {
                String[] feeIds = feeId.split(",");
                for (String feeTdTemp : feeIds) {
                    if (StringUtils.isNotEmpty(feeTdTemp)) {
                        Fee fee = feeService.findFeeById(feeTdTemp);
                        if (fee != null) {
                            fee.setAssemblyId(assemblyId);
                            feeService.modifyFee(fee);
                        }
                    }
                }
            }
           if(StringUtils.isNotEmpty(sendTo)){
               try {
                   System.out.println("推送" + assemblyId + "group.getRobotWxId()" + userAccount);
                   JSONObject pushJson = new JSONObject();
                   pushJson.put("sendTo", sendTo);
                   pushJson.put("title", assembly.getTitle());
                   pushJson.put("description", assembly.getDescription());
                   pushJson.put("thumbUrl",imageTitleUrl);
                   pushJson.put("url", "http://www.xiehoushike.com"+request.getContextPath() + "/assembly/to-detail?assemblyId=" + assemblyId);
                   CommonStringUtils.sendMessage("sendShare", pushJson.toJSONString(), userAccount);
               }catch (Exception e){
                   e.printStackTrace();
                   resultId = "ERROR";

               }
           }
            resultId = assemblyId;
        } catch (Exception e) {
            e.printStackTrace();
            resultId = "ERROR";
        }

        return resultId;
    }

    @ResponseBody
    @RequestMapping(value = "/save-img")
    public String saveUserImg(@RequestParam String imageId, @RequestParam String imgUrl, @RequestParam String imageType) {
        try {
            ImageObj obj = new ImageObj();
            obj.setImageId(imageId);
            if (imgUrl.indexOf("images") != -1) {
                imgUrl = imgUrl.substring(imgUrl.indexOf("images") - 1, imgUrl.indexOf("_"));
            }
            obj.setImageUrl(imgUrl);
            obj.setImageType(imageType);
            return userService.saveOrUpdateImg(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping(value = "/to-detail")
    public String detail(@RequestParam(value = "assemblyId", defaultValue = "") String assemblyId,String type, Model model, HttpServletRequest request, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        Assembly assembly = assemblyService.findAssemblyById(assemblyId);
        UserInfo userInfo = getCurrentUser(userId);
        //浏览次数加1
        Assembly assemblyBrowers = new Assembly();
        assemblyBrowers.setAssemblyId(assemblyId);
        assemblyBrowers.setBrowseTimes(assembly.getBrowseTimes() + 1);
        assemblyService.modifyAssembly(assemblyBrowers);
        assembly.setBrowseTimes(assemblyBrowers.getBrowseTimes());
        String attenderTouser = "farse";
        if(assembly.getCreateTime()!=null){
            try {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日");
                assembly.setCreateTime(format2.format(format1.parse(assembly.getCreateTime())));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(assembly.getStartTime()!=null){
            try {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日 HH:mm");
                assembly.setStartTime(format2.format(format1.parse(assembly.getStartTime())));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(assembly.getEndTime()!=null){
            try {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日 HH:mm");
                Date endTime=format1.parse(assembly.getEndTime());
                assembly.setEndTime(format2.format(endTime));
                Calendar cal=Calendar.getInstance();
                if (cal.getTime().after(endTime)){
                    attenderTouser="guoqi";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        List<Attender> attenderList = attenderService.getListByAssemblyId(assemblyId);
        BigDecimal minMoney = new BigDecimal(0);
        BigDecimal maxMoney = new BigDecimal(0);
        List<Collection> list = collectionService.getCollectionByAssemblyId(assemblyId);
        String collectionStr="false";
        if (!CollectionUtils.isEmpty(list)) {
            for (Collection collection : list) {
                if (collection.getUserId().equals(userId) && collection.getAssemblyId().equals(assemblyId)){
                    collectionStr="true";
                    break;
                }
            }
        }
        int UserSumCount=0;
        if (!CollectionUtils.isEmpty(assembly.getFeeList())) {
            for (Fee fee : assembly.getFeeList()) {
                if (minMoney.compareTo(new BigDecimal(0)) == 0) {
                    minMoney = fee.getFee();
                }
                if (maxMoney.compareTo(new BigDecimal(0)) == 0) {
                    maxMoney = fee.getFee();
                }
                if (fee.getFee().compareTo(minMoney) < 0) {
                    minMoney = fee.getFee();
                }
                if (fee.getFee().compareTo(maxMoney) > 0) {
                    maxMoney = fee.getFee();
                }
                int count = 0;
                for (Attender attender : attenderList) {
                    UserSumCount+=Integer.parseInt(attender.getUserCount());
                    if (attender.getFeedId().equals(fee.getFeeId())) {
                        count++;
                    }

                    if (attender.getUserId().equals(userInfo.getUserId())) {
                        attenderTouser = "true";
                    }
                }
                if (fee.getQuota() == 0) {
                    fee.setQuotaTitle("不限制人数");
                } else {
                    int userCount = fee.getQuota() - count;
                    if (userCount <= 0) {
                        fee.setQuota(-1);
                    }
                    fee.setQuotaTitle("剩余：" + userCount);
                }
            }
        }
        List<Comment> commentList = commentService.findCommentByObjId(assemblyId);
        model.addAttribute("attenderTouser", attenderTouser);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("assembly", assembly);
        model.addAttribute("minMoney", minMoney);
        model.addAttribute("maxMoney", maxMoney);
        model.addAttribute("userCount", UserSumCount);
        model.addAttribute("attenderList", attenderList);
        model.addAttribute("commentList", commentList);
        model.addAttribute("collectionCount", list.size());
        model.addAttribute("collectionStr", collectionStr);
        model.addAttribute("type",type);
        //微信jssdk相关代码
        String url = WxUtils.getUrl(request);
        Map<String, String> parmsMap = WxUtils.sign(url);
        model.addAttribute("parmsMap", parmsMap);
        return "assembly/detail";
    }

    @RequestMapping(value = "/to-attender")
    public String attender(@RequestParam(value = "assemblyId", defaultValue = "") String assemblyId,String type, Model model, HttpServletRequest request, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        Assembly assembly = assemblyService.findAssemblyById(assemblyId);
        UserInfo userInfo = getCurrentUser(userId);
        //浏览次数加1
        Assembly assemblyBrowers = new Assembly();
        assemblyBrowers.setAssemblyId(assemblyId);
        assemblyBrowers.setBrowseTimes(assembly.getBrowseTimes() + 1);
        assemblyService.modifyAssembly(assemblyBrowers);
        assembly.setBrowseTimes(assemblyBrowers.getBrowseTimes());
        String attenderTouser = "farse";
        if(assembly.getCreateTime()!=null){
            try {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日");
                assembly.setCreateTime(format2.format(format1.parse(assembly.getCreateTime())));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(assembly.getStartTime()!=null){
            try {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日 HH:mm");
                assembly.setStartTime(format2.format(format1.parse(assembly.getStartTime())));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(assembly.getEndTime()!=null){
            try {
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat format2 = new SimpleDateFormat("MM月dd日 HH:mm");
                Date endTime=format1.parse(assembly.getEndTime());
                assembly.setEndTime(format2.format(endTime));
                Calendar cal=Calendar.getInstance();
                if (cal.getTime().after(endTime)){
                    attenderTouser="guoqi";
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        List<Attender> attenderList = attenderService.getListByAssemblyId(assemblyId);
        BigDecimal minMoney = new BigDecimal(0);
        BigDecimal maxMoney = new BigDecimal(0);
        List<Collection> list = collectionService.getCollectionByAssemblyId(assemblyId);
        String collectionStr="false";
        if (!CollectionUtils.isEmpty(list)) {
            for (Collection collection : list) {
                if (collection.getUserId().equals(userId) && collection.getAssemblyId().equals(assemblyId)){
                    collectionStr="true";
                    break;
                }
            }
        }
        int UserSumCount=0;
        if (!CollectionUtils.isEmpty(assembly.getFeeList())) {
            for (Fee fee : assembly.getFeeList()) {
                if (minMoney.compareTo(new BigDecimal(0)) == 0) {
                    minMoney = fee.getFee();
                }
                if (maxMoney.compareTo(new BigDecimal(0)) == 0) {
                    maxMoney = fee.getFee();
                }
                if (fee.getFee().compareTo(minMoney) < 0) {
                    minMoney = fee.getFee();
                }
                if (fee.getFee().compareTo(maxMoney) > 0) {
                    maxMoney = fee.getFee();
                }
                int count = 0;
                for (Attender attender : attenderList) {
                    UserSumCount+=Integer.parseInt(attender.getUserCount());
                    if (attender.getFeedId().equals(fee.getFeeId())) {
                        count++;
                    }

                    if (attender.getUserId().equals(userInfo.getUserId())) {
                        attenderTouser = "true";
                    }
                }
                if (fee.getQuota() == 0) {
                    fee.setQuotaTitle("不限制人数");
                } else {
                    int userCount = fee.getQuota() - count;
                    if (userCount <= 0) {
                        fee.setQuota(-1);
                    }
                    fee.setQuotaTitle("剩余：" + userCount);
                }
            }
        }
        List<Comment> commentList = commentService.findCommentByObjId(assemblyId);
        model.addAttribute("attenderTouser", attenderTouser);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("assembly", assembly);
        model.addAttribute("minMoney", minMoney);
        model.addAttribute("maxMoney", maxMoney);
        model.addAttribute("userCount", UserSumCount);
        model.addAttribute("attenderList", attenderList);
        model.addAttribute("commentList", commentList);
        model.addAttribute("collectionCount", list.size());
        model.addAttribute("collectionStr", collectionStr);
        model.addAttribute("type",type);
        //微信jssdk相关代码
        String url = WxUtils.getUrl(request);
        Map<String, String> parmsMap = WxUtils.sign(url);
        model.addAttribute("parmsMap", parmsMap);
        return "assembly/detail";
    }
    @RequestMapping(value = "/showAttender")
    public String showAttender(@RequestParam(value = "assemblyId", defaultValue = "") String assemblyId, Model model) {
        List<Attender> attenderList = attenderService.getListByAssemblyId(assemblyId);
        model.addAttribute("assemblyId", assemblyId);
        model.addAttribute("userCount", attenderList.size());
        model.addAttribute("attenderList", attenderList);
        return "assembly/attendUser";
    }

    @RequestMapping(value = "/to-success")
    public String success(@RequestParam(value = "assemblyId", defaultValue = "") String assemblyId, Model model) {
        Assembly assembly = assemblyService.findAssemblyById(assemblyId);
        model.addAttribute("assembly", assembly);

        return "assembly/publishSuccess";
    }

    @RequestMapping(value = "/saveAttender")
    public String saveAttender(@RequestParam String assemblyId, @RequestParam String feeId, @RequestParam String questionAnswer, @RequestParam String userId,@RequestParam String userCount, Model model) {
        String resultId = "";
        try {
            Attender attender = new Attender();
            attender.setFeedId(feeId);
            attender.setAssemblyId(assemblyId);
            attender.setQuestionAnswer(questionAnswer);
            attender.setUserId(userId);
            attender.setUserCount(userCount);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            attender.setCreateTime(sdf.format(cal.getTime()));
            Assembly assembly = assemblyService.findAssemblyById(assemblyId);
            String[] questions = assembly.getShowApplyProblem().split("\\,");
            String[] questionAnswers = questionAnswer.split("\\^");
            for (int i=1;i<=questions.length;i++){
                if (!"undefined".equals(questionAnswers[i])) {
                    if (questions[i-1].equals("userName")){
                        attender.setUserName(questionAnswers[i]);
                    }
                    if (questions[i-1].equals("phone")){
                        attender.setPhone(questionAnswers[i]);
                    }
                    if (questions[i-1].equals("wx")){
                        attender.setWx(questionAnswers[i]);
                    }
                    if (questions[i-1].equals("email")){
                        attender.setEmail(questionAnswers[i]);
                    }
                    if (questions[i-1].equals("company")){
                        attender.setCompany(questionAnswers[i]);
                    }
                }
            }
            List<Attender> attenders = attenderService.getListByAssemblyId(assemblyId);
            boolean save=true;
            if (!CollectionUtils.isEmpty(attenders)){
                for (Attender attender1:attenders){
                    if (attender1.getUserId().equals(userId)){
                        save=false;
                        break;
                    }
                }
            }
            if (save) {
                attenderService.saveAttender(attender);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assembly assembly = assemblyService.findAssemblyById(assemblyId);
        Fee fee = feeService.findFeeById(feeId);
        model.addAttribute("fee", fee);
        model.addAttribute("assembly", assembly);
        return "assembly/attendSuccess";
    }

    @RequestMapping(value = "/to-attendersuccess")
    public String attendersuccess(@RequestParam(value = "assemblyId", defaultValue = "") String assemblyId, @RequestParam(value = "feeId", defaultValue = "") String feeId, Model model, HttpServletRequest request) {
        return "";
    }

    @RequestMapping(value = "/to-comment")
    public String commentAdd(@RequestParam(value = "assemblyId", defaultValue = "") String assemblyId, Model model) {
        Assembly assembly = assemblyService.findAssemblyById(assemblyId);
        model.addAttribute("assembly", assembly);
        return "assembly/commentPublish";
    }

    @RequestMapping(value = "/saveComment")
    @ResponseBody
    public String saveComment(@RequestParam String assemblyId, @RequestParam String content, @RequestParam String imageIdStrComment, @RequestParam int rating, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        String resultId = "";
        try {
            Comment comment = new Comment();
            comment.setUserId(userId);
            comment.setContent(content);
            comment.setObjId(assemblyId);
            comment.setRating(rating);
            comment.setObjType("ASSEMBLY");
            resultId = commentService.saveComment(comment);
            if (StringUtils.isNotEmpty(imageIdStrComment)) {
                String[] imageIdStrComments = imageIdStrComment.split(",");
                for (String imageIdStrTemp : imageIdStrComments) {
                    if (StringUtils.isNotEmpty(imageIdStrTemp)) {
                        ImageObj imageObj = userService.findById(imageIdStrTemp);
                        if (imageObj != null) {
                            imageObj.setObjId(resultId);
                            userService.saveOrUpdateImg(imageObj);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultId = "ERROR";
        }

        return resultId;
    }

    @RequestMapping(value = "/saveZan")
    @ResponseBody
    public String saveZan(@RequestParam String commentId, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        String resultId = "";
        int count=0;
        try {
            UserInfo userInfo = getCurrentUser(userId);
            Comment comment = commentService.findCommentById(commentId);
            if (StringUtils.isEmpty(comment.getZanContent())) {
                comment.setZanContent(userInfo.getNickName());
                count=1;
            } else {
                String[] zanContent = comment.getZanContent().split("&");
                count = zanContent.length;
                if (!comment.getZanContent().contains(userInfo.getNickName())) {
                    comment.setZanContent(comment.getZanContent() + "&" + userInfo.getNickName());
                    count = count+1;
                }
            }
            commentService.modifyComment(comment);
        } catch (Exception e) {
            e.printStackTrace();
            resultId = "ERROR";
        }

        return count+"";
    }

    @RequestMapping(value = "/saveReply")
    @ResponseBody
    public String saveReply(@RequestParam String commentId, @RequestParam String replyContent, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        String resultId = "";
        try {
            UserInfo userInfo = getCurrentUser(userId);
            Comment comment = commentService.findCommentById(commentId);
            resultId=userInfo.getNickName() + ":" + replyContent;
            if (StringUtils.isEmpty(comment.getReplyContent())) {
                comment.setReplyContent(resultId);
            } else {
                comment.setReplyContent(comment.getReplyContent() + "&#" +resultId);
            }
            commentService.modifyComment(comment);
        } catch (Exception e) {
            e.printStackTrace();
            resultId = "ERROR";
        }

        return resultId;
    }

    @RequestMapping(value = "/to-pay-for-confirm")
    public String toPayForConfirm(HttpServletRequest request, RedirectAttributes attr) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String[] states = state.split("_");
        String feeId = states[0];
        String assemblyId = states[1];
        String questionAnswer = states[2];
        String userCount = states[3];
        Fee fee = feeService.findFeeById(feeId);
        WeixinOauth weixinOauth=new WeixinOauth();
        AccessTokenBean accessTokenBean = weixinOauth.obtainOauthAccessToken(code);
        WeixinUser weixinUser=weixinOauth.getUserInfo(accessTokenBean.getAccess_token(),accessTokenBean.getOpenid());
        UserInfo user = new UserInfo();
        String userId = CommonStringUtils.genPK();
        if(weixinUser != null && StringUtils.isNotBlank(weixinUser.getOpenId())){
            UserInfo userInfo = userService.findUserByOpenId(weixinUser.getOpenId());
            if(userInfo == null){
                user.setUserId(userId);
                user.setOpenId(weixinUser.getOpenId());
                user.setNickName(weixinUser.getNickname());
                user.setSex(weixinUser.getSex());
                user.setCity(weixinUser.getCity());
                ImageObj imageObj = new ImageObj();
                imageObj.setImageUrl(weixinUser.getHeadimgurl());
                user.setImageObj(imageObj);
                String result = userService.saveUser(user);
            }else{
                userId=userInfo.getUserId();
            }

        }
        System.out.println("shuliang :::::::::::::::::::::::::::::::"+userCount);
        System.out.println("jine:::::::::::::::::::::::::::::"+fee.getFee().multiply(new BigDecimal(userCount)));
        String payMessageTitle = "您在邂逅活动的报名款项：" + fee.getFeeTitle();
        String jsApiParams = userPayToCorpByHuodong(code, payMessageTitle, fee.getFee().multiply(new BigDecimal(userCount)),weixinOauth,accessTokenBean.getOpenid());
        attr.addAttribute("jsApiParams", jsApiParams);
        attr.addAttribute("payTip", "你确定要支付" + fee.getFee().multiply(new BigDecimal(userCount)) + "元吗");
        attr.addAttribute("okUrl", request.getContextPath() + "/assembly/saveAttender?assemblyId=" + assemblyId + "&feeId=" + feeId + "&questionAnswer=" + questionAnswer+"&userId="+userId+"&userCount="+userCount);
        attr.addAttribute("backUrl", request.getContextPath() + "/assembly/to-detail?assemblyId=" + assemblyId);

        return "redirect:/wxPay/to-pay/";
    }

    @RequestMapping(value = "/saveCollection")
    @ResponseBody
    public String saveCollection(@RequestParam String assemblyId, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        String resultId = "";
        try {
            UserInfo userInfo = getCurrentUser(userId);
            Collection Collection = new Collection();
            Collection.setAssemblyId(assemblyId);
            Collection.setUserId(userId);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Collection.setCreateTime(sdf.format(cal.getTime()));
            resultId = collectionService.saveCollection(Collection);
        } catch (Exception e) {
            e.printStackTrace();
            resultId = "ERROR";
        }
        return resultId;
    }
    @RequestMapping(value = "/myAssembly")
    public String myAssembly( Model model) {
        return "assembly/myAssembly";
    }

    @RequestMapping("/myAssemblylist")
    @ResponseBody
    public List<Assembly> myAssemblylist( @RequestParam(value = "startIndex", defaultValue = "0") int startIndex, @RequestParam(value = "loadSize", defaultValue = "20") int loadSize, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        Assembly assembly = new Assembly();
        assembly.setUserId(userId);
        List<Assembly> assemblyList = assemblyService.findAssemblyList(assembly, startIndex, loadSize);
        return assemblyList;
    }
    @RequestMapping("/updateAssembly")
    @ResponseBody
    public String  updateAssembly(@RequestParam String assemblyId,
            @RequestParam String resultContent,Model model, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        String type="";
        try{
            Assembly assembly = assemblyService.findAssemblyById(assemblyId);
            if (assembly!=null){
                assembly=new Assembly();
                assembly.setAssemblyId(assemblyId);
                assembly.setStatus("DISABLED");
                assembly.setResultContent(resultContent);
                assemblyService.modifyAssembly(assembly);
                type="SUCCESS";
            }
        }catch (Exception e){
            e.printStackTrace();
            type="ERROR";
        }
        return type;
    }
    @RequestMapping("/updateAssemblyStatus")
    @ResponseBody
    public String  updateAssemblyStatus(@RequestParam String assemblyId,Model model, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        String type="";
        try{
            Assembly assembly = assemblyService.findAssemblyById(assemblyId);
            if (assembly!=null){
                assembly=new Assembly();
                assembly.setAssemblyId(assemblyId);
                assembly.setStatus("PUBLISHED");
                assemblyService.modifyAssembly(assembly);
                type="SUCCESS";
            }
        }catch (Exception e){
            e.printStackTrace();
            type="ERROR";
        }
        return type;
    }
    @RequestMapping("/updateAssemblyCarousel")
    @ResponseBody
    public String  updateAssemblyCarousel(@RequestParam String assemblyId,@RequestParam String carousel,Model model, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        String type="";
        try{
            Assembly assembly = assemblyService.findAssemblyById(assemblyId);
            if (assembly!=null){
                assembly=new Assembly();
                assembly.setAssemblyId(assemblyId);
                assembly.setCarousel(carousel);
                assemblyService.modifyAssembly(assembly);
                type="SUCCESS";
            }
        }catch (Exception e){
            e.printStackTrace();
            type="ERROR";
        }
        return type;
    }
    @RequestMapping(value = "/to-result")
    public String result(@RequestParam(value = "assemblyId", defaultValue = "") String assemblyId,@RequestParam String type, Model model) {
        Assembly assembly = assemblyService.findAssemblyById(assemblyId);
        model.addAttribute("assembly", assembly);
        model.addAttribute("type",type);
        return "assembly/resultContent";
    }

    @RequestMapping(value = "/mySignAssembly")
    public String mySignAssembly( Model model) {
        return "assembly/mySignAssembly";
    }

    @RequestMapping("/mySignAssemblylist")
    @ResponseBody
    public List<Assembly> mySignAssemblylist(
            @RequestParam(value = "startIndex", defaultValue = "0") int startIndex, @RequestParam(value = "loadSize", defaultValue = "20") int loadSize, Model model, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        Assembly assembly = new Assembly();
        List<Assembly> assemblyList = assemblyService.findSignAssemblyList(assembly, userId,startIndex, loadSize);
        return assemblyList;
    }
    @RequestMapping(value = "/assemblyList")
    public String assemblyListlyList( Model model) {
        return "assembly/assemblyList";
    }

    @RequestMapping("/assemblyAllList")
    @ResponseBody
    public List<Assembly> assemblyAllList(
            @RequestParam(value = "startIndex", defaultValue = "0") int startIndex, @RequestParam(value = "loadSize", defaultValue = "20") int loadSize, Model model, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        Assembly assembly = new Assembly();
        List<Assembly> assemblyList = assemblyService.findAssemblyList(assembly, startIndex, loadSize);
        return assemblyList;
    }
    @RequestMapping("/deleteAssembly")
    @ResponseBody
    public String  deleteAssembly(@RequestParam String assemblyId, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        String type="";
        try{
            Assembly assembly = assemblyService.findAssemblyById(assemblyId);
            if (assembly!=null){
                assemblyService.deleteAssembly(assemblyId);
                type="SUCCESS";
            }
        }catch (Exception e){
            e.printStackTrace();
            type="ERROR";
        }
        return type;
    }
    @RequestMapping(value = "/to-update")
    public String updateAssembly(@RequestParam(value = "assemblyId", defaultValue = "") String assemblyId, Model model, HttpServletRequest request, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        Assembly assembly = assemblyService.findAssemblyById(assemblyId);
        BigDecimal minMoney = new BigDecimal(0);
        BigDecimal maxMoney = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(assembly.getFeeList())) {
            for (Fee fee : assembly.getFeeList()) {
                if (minMoney.compareTo(new BigDecimal(0)) == 0) {
                    minMoney = fee.getFee();
                }
                if (maxMoney.compareTo(new BigDecimal(0)) == 0) {
                    maxMoney = fee.getFee();
                }
                if (fee.getFee().compareTo(minMoney) < 0) {
                    minMoney = fee.getFee();
                }
                if (fee.getFee().compareTo(maxMoney) > 0) {
                    maxMoney = fee.getFee();
                }
            }
        }
        UserInfo userInfo = getCurrentUser(userId);
        model.addAttribute("userInfo",userInfo);
        model.addAttribute("minMoney", minMoney);
        model.addAttribute("maxMoney", maxMoney);
        model.addAttribute("assembly", assembly);
        return "assembly/updateActivity";
    }
    @RequestMapping("/deleteImg")
    @ResponseBody
    public String  deleteImg(@RequestParam String imageId, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        String type="";
        try{
                userService.deleteImageObjByImageId(imageId);
                type="SUCCESS";
        }catch (Exception e){
            e.printStackTrace();
            type="ERROR";
        }
        return type;
    }
    @RequestMapping(value = "/myCollectionAssembly")
    public String myCollectionAssembly( Model model) {
        return "assembly/myCollectionAssembly";
    }

    @RequestMapping("/myCollectionAssemblyList")
    @ResponseBody
    public List<Assembly> myCollectionAssemblyList(
            @RequestParam(value = "startIndex", defaultValue = "0") int startIndex, @RequestParam(value = "loadSize", defaultValue = "20") int loadSize, Model model, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
        Assembly assembly = new Assembly();
        List<Assembly> assemblyList = assemblyService.findCollectionAssemblyList(assembly, userId,startIndex, loadSize);
        return assemblyList;
    }
    @RequestMapping(value = "/toAttendermd")
    public String toAttendermd(@RequestParam String assemblyId,Model model) {
        model.addAttribute("assemblyId",assemblyId);
        return "assembly/assemblybmmd";
    }

    //导出报名名单
    @ResponseBody
    @RequestMapping(value = "/exportEnrollListToEmail")
    public String exportEnrollListToEmail(@RequestParam String assemblyId,@RequestParam String toEmailAddress) throws IOException {
        if(StringUtils.isNotBlank(toEmailAddress) && StringUtils.isNotBlank(assemblyId)){
            Boolean result= attenderService.exportAttenderListToEmail(assemblyId,toEmailAddress);
            return result?Contants.SUCCESS:Contants.FAILED;
        }
        else{
            return Contants.FAILED;
        }

    }
    //获取已报名名单
    @ResponseBody
    @RequestMapping(value = "/findAttender")
    public List<Attender> findAttender(@RequestParam String assemblyId, @RequestParam int startIndex, @RequestParam int loadSize){
        try{
            Attender Attender=new Attender();
            Attender.setAssemblyId(assemblyId);
            return attenderService.findAttenderList(Attender,startIndex,loadSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}