package com.timeshare.dao;

import com.timeshare.domain.OpenPage;
import com.timeshare.domain.UserInfo;

/**
 * Created by adam on 2016/6/11.
 */
public interface UserDAO {

    void saveUser(UserInfo info);

    String modifyUser(UserInfo userInfo);

    UserInfo findUserByUserId(String userId);

    String deleteById(String userId);

    OpenPage<UserInfo> findUserPage(String mobile, String nickName, OpenPage page);

}
