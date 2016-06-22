package com.timeshare.service.impl;

import com.timeshare.domain.OpenPage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2016/6/21.
 */
@Service("UserService")
public class UserServiceImpl implements UserService{

    @Override
    public void saveUser(UserInfo info) {

    }

    @Override
    public String modifyUser(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo findUserByUserId(String userId) {
        return null;
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
