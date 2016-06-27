package com.timeshare.service.impl;

import com.timeshare.dao.ImageObjDAO;
import com.timeshare.dao.UserDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.OpenPage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2016/6/21.
 */
@Service("UserService")
public class UserServiceImpl implements UserService{

    @Autowired
    UserDAO userDAO;
    @Autowired
    ImageObjDAO imageObjDAO;

    @Override
    public void saveUser(UserInfo info) {

    }

    @Override
    public String modifyUser(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo findUserByUserId(String userId) {
        return userDAO.findUserByUserId(userId);
    }

    @Override
    public ImageObj findUserImg(String userId, String imgType) {
        return imageObjDAO.findImgByObjIdAndType(userId,imgType);
    }

    @Override
    public void saveOrUpdateImg(ImageObj obj) {
        if(StringUtils.isBlank(obj.getImageId())){
            imageObjDAO.saveImg(obj);
        }else{
            imageObjDAO.updateImg(obj);
        }
    }

    @Override
    public String deleteById(String userId) {
        return null;
    }

    @Override
    public OpenPage<UserInfo> findUserPage(String mobile, String nickName, OpenPage page) {
        return null;
    }
}
