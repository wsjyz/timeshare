package com.timeshare.controller.assembly;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.timeshare.controller.BaseController;
import com.timeshare.domain.*;
import com.timeshare.domain.assembly.*;
import com.timeshare.domain.assembly.Collection;
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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
                        @RequestParam(value = "type", defaultValue = "online") String type, Model model) {
        model.addAttribute("type", type);
        Assembly assembly = new Assembly();
        assembly.setStatus("PUBLISHED");
        assembly.setShowOldTime("true");
        List<Assembly> assemblyList = assemblyService.findAssemblyList(assembly, 0, 10);
        model.addAttribute("assemblyList", assemblyList);
        return "assembly/index";
    }

    @RequestMapping(value = "/searchAssembly")
    public String searchAssembly(@RequestParam(value = "searchName", defaultValue = "") String searchName, @RequestParam(value = "citySelect", defaultValue = "") String citySelect, @RequestParam(value = "type", defaultValue = "online") String type, Model model) {
        model.addAttribute("type", type);
        model.addAttribute("citySelect", citySelect);
        model.addAttribute("searchName", searchName);

        return "assembly/tab-index";
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<Assembly> findList(@RequestParam(value = "searchName", defaultValue = "") String searchName, @RequestParam(value = "citySelect", defaultValue = "") String citySelect, @RequestParam(value = "type", defaultValue = "online") String type,
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
    public String add() {
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
                if (StringUtils.isNotEmpty(feeIdStr[i])){
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
                               @RequestParam String applyId,@RequestParam String imageIdStrDesc,@RequestParam String imageIdStrCon, @CookieValue(value = "time_sid", defaultValue = "admin") String userId) {
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
            if (StringUtils.isNotEmpty(imageIdStr)) {
                ImageObj imageObj = userService.findById(imageIdStr);
                imageObj.setObjId(assemblyId);
                userService.saveOrUpdateImg(imageObj);
            }
            if (StringUtils.isNotEmpty(imageIdStrCon)) {
                ImageObj imageObj = userService.findById(imageIdStrCon);
                imageObj.setObjId(assemblyId);
                userService.saveOrUpdateImg(imageObj);
            }
            if (StringUtils.isNotEmpty(imageIdStrDesc)) {
                String[] imageIdStrDescs = imageIdStrDesc.split(",");
                for (String imageIdStrTemp : imageIdStrDescs) {
                    if (StringUtils.isNotEmpty(imageIdStrTemp)) {
                        ImageObj imageObj = userService.findById(imageIdStrTemp);
                        if (imageObj != null) {
                            imageObj.setObjId(assemblyId);
                            userService.saveOrUpdateImg(imageObj);
                        }
                    }
                }
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
                assembly.setEndTime(format2.format(format1.parse(assembly.getEndTime())));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        List<Attender> attenderList = attenderService.getListByAssemblyId(assemblyId);
        BigDecimal minMoney = new BigDecimal(0);
        BigDecimal maxMoney = new BigDecimal(0);
        String attenderTouser = "farse";
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
        model.addAttribute("userCount", attenderList.size());
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
    public String saveAttender(@RequestParam String assemblyId, @RequestParam String feeId, @RequestParam String questionAnswer, @CookieValue(value = "time_sid", defaultValue = "admin") String userId, Model model) {
        String resultId = "";
        try {
            Attender attender = new Attender();
            attender.setFeedId(feeId);
            attender.setAssemblyId(assemblyId);
            attender.setQuestionAnswer(questionAnswer);
            attender.setUserId(userId);
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            attender.setCreateTime(sdf.format(cal.getTime()));
            if (StringUtils.isNotEmpty(questionAnswer)) {
                String[] questionAnswers = questionAnswer.split(",");
                if (!questionAnswers[0].equals("undefined")) {
                    attender.setUserName(questionAnswers[0]);
                }
                if (!questionAnswers[1].equals("undefined")) {
                    attender.setPhone(questionAnswers[1]);
                }
                if (!questionAnswers[2].equals("undefined")) {
                    attender.setWx(questionAnswers[2]);
                }
                if (!questionAnswers[3].equals("undefined")) {
                    attender.setEmail(questionAnswers[3]);
                }
                if (!questionAnswers[4].equals("undefined")) {
                    attender.setCompany(questionAnswers[4]);
                }
            }
            attenderService.saveAttender(attender);
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
        System.out.println("feeId" + feeId);
        System.out.println("questionAnswer" + questionAnswer);
        System.out.println("assemblyId" + assemblyId);

        Fee fee = feeService.findFeeById(feeId);

        String payMessageTitle = "您在邂逅活动的报名款项：" + fee.getFeeTitle();
        String jsApiParams = WxPayUtils.userPayToCorp(code, payMessageTitle, fee.getFee());
        attr.addAttribute("jsApiParams", jsApiParams);
        attr.addAttribute("payTip", "你确定要支付" + fee.getFee() + "元吗");
        attr.addAttribute("okUrl", request.getContextPath() + "/assembly/saveAttender?assemblyId=" + assemblyId + "&feeId=" + feeId + "&questionAnswer=" + questionAnswer);
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
}