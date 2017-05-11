package com.timeshare.controller.crowdfunding;

import com.sun.tools.internal.jxc.ap.Const;
import com.taobao.api.internal.toplink.embedded.websocket.util.StringUtil;
import com.timeshare.controller.BaseController;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.CommentService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.service.crowdfunding.EnrollService;
import com.timeshare.utils.Contants;
import com.timeshare.utils.WxUtils;
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
import java.util.Map;

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

    @Autowired
    private CommentService commentService;

    protected Logger logger = LoggerFactory.getLogger(CrowdFundingController.class);

    //发布众筹页面
    @RequestMapping(value = "/createCrowdFunding")
    public String createCrowdFunding(@CookieValue(value="time_sid", defaultValue="00359e8721c44d168aac7d501177e314") String userId,Model model) {
        ImageObj imageObj=new ImageObj();
        imageObj.setImageType(Contants.IMAGE_TYPE.USER_HEAD.name());
        UserInfo userInfo=userService.findUserByUserId(userId,imageObj);
        model.addAttribute("userInfo",userInfo);
        return "crowdfunding/fbzc";
    }
    //编辑众筹页面
    @RequestMapping(value = "/editCrowdFunding")
    public String editCrowdFunding(@RequestParam String crowdFundingId,Model model) {
        if(StringUtils.isNotBlank(crowdFundingId)){
            CrowdFunding crowdFunding=crowdFundingService.editCrowdFundingByCrowdFundingId(crowdFundingId);
            model.addAttribute("crowdFunding",crowdFunding);
        }
        return "crowdfunding/bjzc";
    }
    //众筹首页
    @RequestMapping(value = "/toIndex")
    public String toIndex(Model model) {
        List<CrowdFunding> crowdFundingList=crowdFundingService.findCrowdFundingToIndex(0,5,"true");
        model.addAttribute("crowdFundingList",crowdFundingList);
        return "crowdfunding/list";
    }
    //我发起的众筹
    @RequestMapping(value = "/toMyCrowdFunding")
    public String toMyCrowdFunding() {
        return "crowdfunding/wfqdzc";
    }

    //我的
    @RequestMapping(value = "/toMe")
    public String toMe(@CookieValue(value="time_sid", defaultValue="00359e8721c44d168aac7d501177e314") String userId,Model model) {
        ImageObj imageObj=new ImageObj();
        imageObj.setImageType(Contants.IMAGE_TYPE.USER_HEAD.name());
        UserInfo userInfo=userService.findUserByUserId(userId,imageObj);
        model.addAttribute("userInfo",userInfo);
        return "crowdfunding/me";
    }
    //众筹详情页
    @RequestMapping(value = "/toDetail")
    public String toDetail(@RequestParam String crowdFundingId,Model model,@RequestParam(defaultValue = "false" ,required = false) String commentFlag,@CookieValue(value = "time_sid", defaultValue = "00359e8721c44d168aac7d501177e314") String userId,HttpServletRequest request) {
        //是否默认显示评论tab
        model.addAttribute("commentFlag",commentFlag);
        //众筹详情页
        CrowdFunding crowdFunding=crowdFundingService.findCrowdFundingDetailByCrowdfundingId(crowdFundingId);
        model.addAttribute("crowdFunding",crowdFunding);

        //已报名对象
        if(crowdFunding!=null && StringUtils.isNotBlank(crowdFunding.getCrowdfundingId())){
            List<Enroll> enrollList=enrollService.findCrowdfundingEnrollList(crowdFunding.getCrowdfundingId());
            model.addAttribute("enrollList",enrollList);
        }
        UserInfo userInfo=getCurrentUser(userId);
        model.addAttribute("userInfo",userInfo);


        //微信jssdk相关代码
        String url = WxUtils.getUrl(request);
        Map<String,String> parmsMap = WxUtils.sign(url);
        model.addAttribute("parmsMap",parmsMap);

        return "crowdfunding/details";
    }
    //保存众筹
    @ResponseBody
    @RequestMapping(value = "/save")
    public String save(CrowdFunding crowdFunding, @RequestParam(required =  false) String imgUrl,@CookieValue(value="time_sid", defaultValue="00359e8721c44d168aac7d501177e314") String userId, Model model) {
        try{
            if(crowdFunding!=null && StringUtils.isNotBlank(crowdFunding.getProjectName())){
                boolean isExisting=crowdFundingService.crowdFundingPrjectNameIsExisting(crowdFunding.getProjectName());
                if(!isExisting){
                    crowdFunding.setUserId(userId);
                    UserInfo userinfo=getCurrentUser(userId);
                    crowdFunding.setCreateUserName(userinfo.getNickName());
                    crowdFunding.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    crowdFunding.setOptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    //保存众筹
                    String pk= crowdFundingService.saveCrowdFunding(crowdFunding);

                    if(StringUtils.isNotBlank(imgUrl)){
                        ImageObj obj = new ImageObj();
                        obj.setObjId(pk);
                        if(imgUrl.indexOf("images") != -1 && imgUrl.indexOf(".jpg")!=-1){
                            imgUrl = imgUrl.substring(imgUrl.indexOf("images") - 1,imgUrl.indexOf(".jpg"));
                        }
                        obj.setImageUrl(imgUrl);
                        obj.setImageType(Contants.IMAGE_TYPE.CROWD_FUNDING_IMG.name());
                        //保存众筹图片
                        userService.saveOrUpdateImg(obj);
                    }
                    return Contants.SUCCESS;
                }
                else{
                    return "PROJECT_NAME_IS_EXISTING";
                }
            }
            else{
                return "PROJECT_NAME_IS_NULL";
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Contants.FAILED;
    }

    //编辑众筹
    @ResponseBody
    @RequestMapping(value = "/edit")
    public String edit(CrowdFunding crowdFunding, @RequestParam String imgUrl,@CookieValue(value="time_sid", defaultValue="00359e8721c44d168aac7d501177e314") String userId, Model model) {
        try{
            if(crowdFunding!=null && StringUtils.isNotBlank(crowdFunding.getProjectName())){

                CrowdFunding crowdFundingDB=crowdFundingService.findCrowdFundingById(crowdFunding.getCrowdfundingId());

                UserInfo userinfo=getCurrentUser(crowdFundingDB.getUserId());
                crowdFunding.setCrowdfundingId(crowdFundingDB.getCrowdfundingId());
                crowdFunding.setUserId(userId);
                crowdFunding.setCreateUserName(userinfo.getNickName());
                crowdFunding.setCreateTime(crowdFundingDB.getCreateTime());
                crowdFunding.setOptTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                //保存众筹
                String pk= crowdFundingService.editCrowdFunding(crowdFunding);

                if(StringUtils.isNotBlank(imgUrl)){
                    ImageObj obj = new ImageObj();
                    obj.setObjId(pk);
                    if(imgUrl.indexOf("images") != -1 && imgUrl.indexOf(".jpg")!=-1){
                        imgUrl = imgUrl.substring(imgUrl.indexOf("images") - 1,imgUrl.indexOf(".jpg"));
                    }
                    obj.setImageUrl(imgUrl);
                    obj.setImageType(Contants.IMAGE_TYPE.CROWD_FUNDING_IMG.name());
                    //保存众筹图片
                    userService.saveOrUpdateImg(obj);
                }
                return Contants.SUCCESS;

            }
            else{
                return "PROJECT_NAME_IS_NULL";
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Contants.FAILED;
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

    //获取众筹列表
    @ResponseBody
    @RequestMapping(value = "/listCrowdFundingToIndex")
    public List<CrowdFunding> listCrowdFundingToIndex(@RequestParam int startIndex, @RequestParam int loadSize,@RequestParam(defaultValue = "",required = false) String isShowFlag) {
        try{
            return crowdFundingService.findCrowdFundingToIndex(startIndex,loadSize,isShowFlag);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //我发起的众筹
    @ResponseBody
    @RequestMapping(value = "/findCrowdFundingByOwner")
    List<CrowdFunding> findCrowdFundingByOwner(@CookieValue(value="time_sid", defaultValue="00359e8721c44d168aac7d501177e314") String userId,@RequestParam int startIndex,@RequestParam int loadSize){
        try{
            //如果是管理员请求则默认将userId置空 让其可以查询所有众筹项目
            if("admin".equals(userId)){
                userId="";
            }
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


    @RequestMapping(value = "/saveComment")
    @ResponseBody
    public Comment saveComment(@RequestParam String crowdfundingId, @RequestParam String content, @CookieValue(value = "time_sid", defaultValue = "00359e8721c44d168aac7d501177e314") String userId) {
        try {
            UserInfo userInfo=getCurrentUser(userId);
            Comment comment = new Comment();
            comment.setUserId(userId);
            comment.setContent(content);
            comment.setObjId(crowdfundingId);
            comment.setObjType(Contants.COMMENT_OBJ_TYPE.CROWDFUNDING.name());
            comment.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            commentService.saveComment(comment);

            comment.setUserImg(userInfo.getHeadImgPath());
            comment.setUserName(userInfo.getNickName());
            return comment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/modifyComment")
    @ResponseBody
    public String modifyComment(@RequestParam String commentId, @RequestParam(required = false) String zanContent, @RequestParam(required = false) String replyContent, @CookieValue(value = "time_sid", defaultValue = "00359e8721c44d168aac7d501177e314") String userId) {
        try {
            if(StringUtils.isNotBlank(commentId)){
                Comment comment = new Comment();
                comment.setCommentId(commentId);
                if(StringUtils.isNotBlank(zanContent)){
                    comment.setZanContent(zanContent);
                }
                if(StringUtils.isNotBlank(replyContent)){
                    comment.setReplyContent(replyContent);
                }
                commentService.modifyComment(comment);
                return Contants.SUCCESS;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Contants.FAILED;
    }

    @ResponseBody
    @RequestMapping(value = "/commentList")
    List<Comment> commentList(@RequestParam String crowdfundingId, @RequestParam int startIndex, @RequestParam int loadSize){
        try{
            return commentService.findCommentByObjId(crowdfundingId,startIndex,loadSize);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
