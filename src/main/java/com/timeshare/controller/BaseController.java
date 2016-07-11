package com.timeshare.controller;

import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.UserService;
import com.timeshare.utils.Contants;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by user on 2016/6/23.
 */
public class BaseController {

    @Autowired
    UserService userService;

    public UserInfo getCurrentUser(String userId){
        ImageObj imageObj = new ImageObj();
        imageObj.setImageType(Contants.IMAGE_TYPE.USER_HEAD.toString());
        UserInfo userInfo = userService.findUserByUserId(userId,imageObj);
        return userInfo;
    }
}
