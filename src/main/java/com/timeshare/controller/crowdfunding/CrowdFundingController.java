package com.timeshare.controller.crowdfunding;

import com.alibaba.fastjson.JSON;
import com.timeshare.controller.BaseController;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.Item;
import com.timeshare.domain.SystemMessage;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Assembly;
import com.timeshare.domain.assembly.Attender;
import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.assembly.Fee;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.utils.Contants;
import com.timeshare.utils.WxUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    protected Logger logger = LoggerFactory.getLogger(CrowdFundingController.class);

    @RequestMapping(value = "/createCrowdFunding")
    public String createCrowdFunding() {
        return "crowdFunding/fbzc";
    }
    @RequestMapping(value = "/toIndex")
    public String toIndex() {
        return "crowdFunding/list";
    }

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

            String pk= crowdFundingService.saveCrowdFunding(crowdFunding);


            ImageObj obj = new ImageObj();
            obj.setObjId(pk);
            if(imgUrl.indexOf("images") != -1){
                imgUrl = imgUrl.substring(imgUrl.indexOf("images") - 1,imgUrl.indexOf("_"));
            }
            obj.setImageUrl(imgUrl);
            obj.setImageType(Contants.IMAGE_TYPE.CROWD_FUNDING_IMG.name());
            userService.saveOrUpdateImg(obj);
            return Contants.SUCCESS;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Contants.FAILED;
    }
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





















    @RequestMapping(value = "/to-upload-img")
    public String toUploadImg(String crowdFundingId,HttpServletRequest request,Model model,@CookieValue(value="time_sid", defaultValue="admin") String userId) {
        ImageObj imageObj = userService.findUserImg(crowdFundingId, Contants.IMAGE_TYPE.CROWD_FUNDING_IMG.name());
        String objId = "";
        String imgType = "";
        String imageId = "";
        if(imageObj == null){
            objId = crowdFundingId;
            imgType = Contants.IMAGE_TYPE.CROWD_FUNDING_IMG.toString();
        }else{
            objId = imageObj.getObjId();
            imgType = imageObj.getImageType();
            imageId = imageObj.getImageId();
            model.addAttribute("imgPath",imageObj.getImageUrl());
        }
        model.addAttribute("objId",objId);
        model.addAttribute("imageType",imgType);
        model.addAttribute("imageId",imageId);

        return "crowdfunding/uploadimg";
    }
    @ResponseBody
    @RequestMapping(value = "/save-img")
    public String saveUserImg(@RequestParam String imageId,@RequestParam String imgUrl,
                              @RequestParam(defaultValue = "",required = false) String objId,
                              @CookieValue(value="time_sid", defaultValue="") String userId){
        ImageObj obj = new ImageObj();
        obj.setImageId(imageId);
        if(StringUtils.isBlank(objId)){
            objId = objId;
        }
        obj.setObjId(objId);
        if(imgUrl.indexOf("images") != -1){
            imgUrl = imgUrl.substring(imgUrl.indexOf("images") - 1,imgUrl.indexOf("_"));
        }
        obj.setImageUrl(imgUrl);
        obj.setImageType(Contants.IMAGE_TYPE.CROWD_FUNDING_IMG.toString());
        userService.saveOrUpdateImg(obj);
        return Contants.SUCCESS;
    }

}
