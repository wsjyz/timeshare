package com.timeshare.controller.manage;

import com.timeshare.controller.BaseController;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.CommentService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.service.crowdfunding.EnrollService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016/9/26.
 */
@Controller
@RequestMapping(value = "/manager/crowdFunding")
public class CrowdFundingManagerController extends  BaseController{


    @Autowired
    UserService userService;
    @Autowired
    private CrowdFundingService crowdFundingService;

    @Autowired
    private EnrollService enrollService;

    @Autowired
    private CommentService commentService;

    protected Logger logger = LoggerFactory.getLogger(CrowdFundingManagerController.class);

    //众筹管理-管理端
    @RequestMapping(value = "/toCrowdFundingManager")
    public String toCrowdFundingManager() {
        return "manager/zcgl";
    }
    //众筹管理
    @ResponseBody
    @RequestMapping(value = "/findCrowdFundingByOwnerToManager")
    List<CrowdFunding> findCrowdFundingByOwnerToManager(@CookieValue(value="time_sid", defaultValue="") String userId,@RequestParam int startIndex,@RequestParam int loadSize){
        try{
            return crowdFundingService.findCrowdFundingToMyCrowdFunding(startIndex,loadSize,"");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //修改项目草稿、已下架为已发布状态
    @ResponseBody
    @RequestMapping(value = "/updateSketchAndOffShelveToReleased")
    public String updateSketchAndOffShelveToReleased(@RequestParam String crowdfundingId) {
        try{
            if(StringUtils.isNotBlank(crowdfundingId)){
                CrowdFunding crowdFunding=new CrowdFunding();
                crowdFunding.setCrowdfundingId(crowdfundingId);
                crowdFunding.setCrowdfundingStatus(Contants.CROWD_FUNDING_STATUS.RELEASED.name());
                return crowdFundingService.modifyEnroll(crowdFunding);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Contants.FAILED;
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
    //修改项目是否在轮播图显示
    @ResponseBody
    @RequestMapping(value = "/updateCrowdfundingIsShow")
    public String updateCrowdfundingIsShow(@RequestParam String crowdfundingId,@RequestParam String isShow) {
        try{
            if(StringUtils.isNotBlank(crowdfundingId)){
                CrowdFunding crowdFunding=new CrowdFunding();
                crowdFunding.setCrowdfundingId(crowdfundingId);
                crowdFunding.setIsShow(isShow);
                crowdFunding.setOptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                return crowdFundingService.modifyEnroll(crowdFunding);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Contants.FAILED;
    }
}
