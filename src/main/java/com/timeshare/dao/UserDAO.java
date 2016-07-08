package com.timeshare.dao;

import com.timeshare.domain.ImageObj;
import com.timeshare.domain.OpenPage;
import com.timeshare.domain.UserInfo;

/**
 * Created by adam on 2016/6/11.
 */
public interface UserDAO {

    String saveUser(UserInfo info);

    String modifyUser(UserInfo userInfo);

    UserInfo findUserByUserId(String userId,ImageObj imageObj);

    String deleteById(String userId);

    OpenPage<UserInfo> findUserPage(String mobile, String nickName, OpenPage page);

    UserInfo findUserByOpenId(String openId);

}
