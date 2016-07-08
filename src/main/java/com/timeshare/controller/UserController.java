package com.timeshare.controller;

import com.timeshare.domain.*;
import com.timeshare.service.ItemService;
import com.timeshare.service.RemindService;
import com.timeshare.service.UserService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by adam on 2016/6/11.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController{

    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired
    RemindService remindService;

    @RequestMapping(value = "/to-userinfo")
    public String toUserInfo(@RequestParam String userId,Model model) {
        model.addAttribute("userId",userId);
        return "userinfo";
    }

    @ResponseBody
    @RequestMapping(value = "/get-userinfo")
    public UserInfo getUserInfo(@RequestParam String userId) {
        ImageObj imageObj = new ImageObj();
        imageObj.setImageType(Contants.IMAGE_TYPE.USER_HEAD.toString());
        UserInfo userInfo = userService.findUserByUserId(userId,imageObj);
        return userInfo;
    }

    @RequestMapping(value = "/to-my-page")
    public String toMyPage() {

        return "mypage";
    }


    @RequestMapping(value = "/to-upload-img")
    public String toUploadImg(HttpServletRequest request,Model model) {
        UserInfo userInfo = getCurrentUser("");
        ImageObj imageObj = userService.findUserImg(userInfo.getUserId(), Contants.ITEM_SHOW_IMG);
        String objId = "";
        String imgType = "";
        String imageId = "";
        if(imageObj == null){
            objId = userInfo.getUserId();
            imgType = Contants.ITEM_SHOW_IMG;
        }else{
            objId = imageObj.getObjId();
            imgType = imageObj.getImageType();
            imageId = imageObj.getImageId();
        }
        model.addAttribute("objId",objId);
        model.addAttribute("imageType",imgType);
        model.addAttribute("imageId",imageId);
        model.addAttribute("imgPath",request.getContextPath()+imageObj.getImageUrl());
        return "uploadimg";
    }
    @ResponseBody
    @RequestMapping(value = "/save-img")
    public String saveUserImg(@RequestParam String imageId,@RequestParam String imgUrl){
        ImageObj obj = new ImageObj();
        //TODO userId
        obj.setImageId(imageId);
        obj.setObjId("admin");
        if(imgUrl.indexOf("images") != -1){
            imgUrl = imgUrl.substring(imgUrl.indexOf("images") - 1,imgUrl.length());
        }
        obj.setImageUrl(imgUrl);
        obj.setImageType(Contants.ITEM_SHOW_IMG);
        userService.saveOrUpdateImg(obj);
        return Contants.SUCCESS;
    }

    @RequestMapping(value = "/to-view-items/{userId}")
    public String toViewUserItems(@PathVariable String userId, Model model) {
        model.addAttribute("userId",userId);
        return "useritems";
    }
    @RequestMapping(value = "/get-user-items")
    @ResponseBody
    public UserInfo getUserItems(@RequestParam String userId) {

        UserInfo userInfo = getCurrentUser(userId);
        Item params = new Item();
        params.setUserId(userId);
        params.setItemStatus(Contants.ITEM_STATUS.for_sale.toString());
        //TODO 最大显示10个项目
        List<Item> items = itemService.findItemPage(params,0,10);
        userInfo.setUserItemList(items);
        return userInfo;
    }
    @RequestMapping(value = "/get-user-img")
    @ResponseBody
    public String getUserImg(@RequestParam String userId) {

        String hasImg = "no";

        ImageObj imageObj = userService.findUserImg(userId, Contants.ITEM_SHOW_IMG);
        if(imageObj != null){
            hasImg = "yes";
        }
        return hasImg;
    }


    @RequestMapping(value = "/get-remind")
    @ResponseBody
    public MyRemind getRemind() {

        MyRemind remind = new MyRemind();
        int sellCount = remindService.queryCountByObjIdAndType("admin","",Contants.REMIND_TYPE.ORDER.toString());
        remind.setSellRemindCount(sellCount);
        return remind;
    }

    @RequestMapping(value = "/save")
    public String save(UserInfo user,Model model) {
        SystemMessage message = new SystemMessage();
        if(user != null){

            String result = userService.modifyUser(user);
            message.setMessageType(result);
            if(result.equals(Contants.SUCCESS)){
                message.setContent("操作成功！");
            }
        }
        model.addAttribute("message",message);
        model.addAttribute("jumpUrl","/user/to-my-page");
        return "info";
    }
}
