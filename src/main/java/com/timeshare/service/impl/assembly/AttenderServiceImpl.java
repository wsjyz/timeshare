package com.timeshare.service.impl.assembly;

import com.timeshare.dao.assembly.AssemblyDAO;
import com.timeshare.dao.assembly.AttenderDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Assembly;
import com.timeshare.domain.assembly.Attender;
import com.timeshare.domain.assembly.Fee;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.AssemblyService;
import com.timeshare.service.assembly.AttenderService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Service("AttenderService")
public class AttenderServiceImpl implements AttenderService {

    @Autowired
    AttenderDAO attenderDAO;
    @Autowired
    UserService userService;
    @Override
    public String saveAttender(Attender Attender) {
        return attenderDAO.saveAttender(Attender);
    }

    @Override
    public String modifyAttender(Attender Attender) {
        return attenderDAO.modifyAttender(Attender);
    }

    @Override
    public Attender findAttenderById(String AttenderId) {
        return attenderDAO.findAttenderById(AttenderId);
    }

    @Override
    public List<Attender> findAttenderList(Attender Attender, int startIndex, int loadSize) {
        return attenderDAO.findAttenderList(Attender,startIndex,loadSize);
    }

    @Override
    public int findAttenderCount(Attender Attender) {
        return attenderDAO.findAttenderCount(Attender);
    }

    @Override
    public List<Attender> getListByAssemblyId(String assemblyId) {
        List<Attender> list= attenderDAO.getListByAssemblyId(assemblyId);
        if (!CollectionUtils.isEmpty(list)){
            for (Attender attender:list){
                UserInfo userInfo=userService.findUserByUserId(attender.getUserId());
                attender.setUserName(userInfo.getNickName());
                ImageObj userImg = userService.findUserImg(attender.getUserId(), Contants.IMAGE_TYPE.USER_HEAD.toString());
                if(userImg!=null && StringUtils.isNotEmpty(userImg.getImageId())){
                    attender.setUserImg(userImg.getImageUrl());
                }
            }
        }else{
            list=new ArrayList<Attender>();
        }
        return list;
    }
}
