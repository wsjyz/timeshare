package com.timeshare.controller.crowdfunding;

import com.timeshare.controller.BaseController;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.service.crowdfunding.EnrollService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/9/26.
 */
@Controller
@RequestMapping(value = "/crowdFunding")
public class CrowdFundingController extends  BaseController{


    @Autowired
    UserService userService;
    @Autowired
    private CrowdFundingService crowdFundingService;

    @Autowired
    private EnrollService enrollService;

    protected Logger logger = LoggerFactory.getLogger(CrowdFundingController.class);

    //发布众筹页面
    @RequestMapping(value = "/createCrowdFunding")
    public String createCrowdFunding() {
        return "crowdfunding/fbzc";
    }
    //众筹首页
    @RequestMapping(value = "/toIndex")
    public String toIndex() {
        return "crowdfunding/list";
    }
    //我发起的众筹
    @RequestMapping(value = "/toMyCrowdFunding")
    public String toMyCrowdFunding() {
        return "crowdfunding/wfqdzc";
    }
    //我的
    @RequestMapping(value = "/toMe")
    public String toMe(@CookieValue(value="time_sid", defaultValue="") String userId,Model model) {
        //MOCK
        userId="00359e8721c44d168aac7d501177e314";
        ImageObj imageObj=new ImageObj();
        imageObj.setImageType(Contants.IMAGE_TYPE.USER_HEAD.name());
        UserInfo userInfo=userService.findUserByUserId(userId,imageObj);
        model.addAttribute("userInfo",userInfo);
        return "crowdfunding/me";
    }
    //众筹详情页
    @RequestMapping(value = "/toDetail")
    public String toDetail(@RequestParam String crowdFundingId,Model model) {
        //众筹详情页
        CrowdFunding crowdFunding=crowdFundingService.findCrowdFundingDetailByCrowdfundingId(crowdFundingId);
        model.addAttribute("crowdFunding",crowdFunding);

        //已报名对象
        if(crowdFunding!=null && StringUtils.isNotBlank(crowdFunding.getCrowdfundingId())){
            List<Enroll> enrollList=enrollService.findCrowdfundingEnrollList(crowdFunding.getCrowdfundingId());
            model.addAttribute("enrollList",enrollList);
        }
        return "crowdfunding/details";
    }
    //保存众筹
    @ResponseBody
    @RequestMapping(value = "/save")
    public String save(CrowdFunding crowdFunding, @RequestParam String imgUrl,@CookieValue(value="time_sid", defaultValue="") String userId, Model model) {
        try{
            //MOCK
            userId="00359e8721c44d168aac7d501177e314";
            crowdFunding.setUserId(userId);
            UserInfo userinfo=getCurrentUser(userId);
            crowdFunding.setCreateUserName(userinfo.getNickName());
            crowdFunding.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            crowdFunding.setOptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            //保存众筹
            String pk= crowdFundingService.saveCrowdFunding(crowdFunding);

            ImageObj obj = new ImageObj();
            obj.setObjId(pk);
            if(imgUrl.indexOf("images") != -1){
                imgUrl = imgUrl.substring(imgUrl.indexOf("images") - 1,imgUrl.indexOf("_"));
            }
            obj.setImageUrl(imgUrl);
            obj.setImageType(Contants.IMAGE_TYPE.CROWD_FUNDING_IMG.name());
            //保存众筹图片
            userService.saveOrUpdateImg(obj);
            return Contants.SUCCESS;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Contants.FAILED;
    }
    //获取众筹列表
    @ResponseBody
    @RequestMapping(value = "/listCrowdFundingToIndex")
    public List<CrowdFunding> listCrowdFundingToIndex(@RequestParam int startIndex, @RequestParam int loadSize) {
        try{
            return crowdFundingService.findCrowdFundingToIndex(startIndex,loadSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //我发起的众筹
    @ResponseBody
    @RequestMapping(value = "/findCrowdFundingByOwner")
    List<CrowdFunding> findCrowdFundingByOwner(@CookieValue(value="time_sid", defaultValue="") String userId,@RequestParam int startIndex,@RequestParam int loadSize){
        try{
            //MOCK
            userId="00359e8721c44d168aac7d501177e314";
            return crowdFundingService.findCrowdFundingToMyCrowdFunding(startIndex,loadSize,userId);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //下架
    @ResponseBody
    @RequestMapping(value = "/crowdFundingToShelve")
    String crowdFundingToShelve(@RequestParam String crowdfundingId,@RequestParam String offShelveReason,@CookieValue(value="time_sid", defaultValue="") String userId){
        try{
            //下架
            return crowdFundingService.crowdFundingToShelveByCrowdfundingId(crowdfundingId,offShelveReason) ;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
