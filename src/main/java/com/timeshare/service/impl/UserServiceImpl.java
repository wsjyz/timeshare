package com.timeshare.service.impl;

import com.timeshare.dao.ImageObjDAO;
import com.timeshare.dao.UserDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.Item;
import com.timeshare.domain.OpenPage;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.ItemService;
import com.timeshare.service.UserService;
import com.timeshare.utils.CommonStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 2016/6/21.
 */
@Service("UserService")
public class UserServiceImpl implements UserService{

    @Autowired
    UserDAO userDAO;
    @Autowired
    ImageObjDAO imageObjDAO;
    @Autowired
    ItemService itemService;

    @Override
    public String saveUser(UserInfo info) {

        if(StringUtils.isBlank(info.getUserId())){
            info.setUserId(CommonStringUtils.genPK());
        }
        return userDAO.saveUser(info);
    }

    @Override
    public String modifyUser(UserInfo userInfo) {
        return userDAO.modifyUser(userInfo);
    }

    @Override
    public UserInfo findUserByUserId(String userId,ImageObj imageObj) {
        return userDAO.findUserByUserId(userId,imageObj);
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

    @Override
    public UserInfo findUserByOpenId(String openId) {
        return userDAO.findUserByOpenId(openId);
    }

    /**
     *
     * @param params
     * @param firstItemId 要放到第一位的
     * @return
     */
    @Override
    public List<Item> getUserItems(Item params,String firstItemId) {
        //TODO 最大显示10个项目
        List<Item> items = itemService.findItemPage(params,0,10);
        //下面要把firstItem的项目放到第一位
        if(items.size() > 1){//如果集合中就一个元素，就不用进行下面的操作

            Item currentFirstItem = items.get(0);//先找出第一个
            //检查第一个是否就是要放到第一位的，如果是就不用进行下面的操作
            if(!currentFirstItem.getItemId().equals(firstItemId)){
                int exchangeIndex = 0;
                for(int i = 1;i < items.size();i ++){//从第二个元素开始遍历，找出要放到第一位的
                    Item tmpItem = items.get(i);
                    if(tmpItem != null && tmpItem.getItemId().equals(firstItemId)){
                        exchangeIndex = i;
                        break;
                    }
                }
                items.set(0,items.get(exchangeIndex));//把要放到第一位的，放到第一位
                items.set(exchangeIndex,currentFirstItem);//再把刚才第一位的元素交换位置
            }

        }

        return items;
    }
}
