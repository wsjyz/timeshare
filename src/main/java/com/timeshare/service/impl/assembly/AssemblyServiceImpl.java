package com.timeshare.service.impl.assembly;

import com.timeshare.dao.BidDAO;
import com.timeshare.dao.ImageObjDAO;
import com.timeshare.dao.assembly.AssemblyDAO;
import com.timeshare.domain.Bid;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Assembly;
import com.timeshare.domain.assembly.Fee;
import com.timeshare.service.BidService;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.AssemblyService;
import com.timeshare.service.assembly.FeeService;
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
@Service("AssemblyService")
public class AssemblyServiceImpl implements AssemblyService {

    @Autowired
    AssemblyDAO assemblyDAO;
    @Autowired
    UserService userService;
    @Autowired
    FeeService feeService;
    @Autowired
    ImageObjDAO imageObjDAO;
    @Override
    public String saveAssembly(Assembly Assembly) {
        return assemblyDAO.saveAssembly(Assembly);
    }

    @Override
    public String modifyAssembly(Assembly Assembly) {
        return assemblyDAO.modifyAssembly(Assembly);
    }

    @Override
    public Assembly findAssemblyById(String AssemblyId) {
        Assembly assembly= assemblyDAO.findAssemblyById(AssemblyId);
        UserInfo userInfo=userService.findUserByUserId(assembly.getUserId());
        assembly.setUserName(userInfo.getNickName());
        ImageObj userImg = userService.findUserImg(assembly.getUserId(), Contants.IMAGE_TYPE.USER_HEAD.toString());
        if(userImg!=null && StringUtils.isNotEmpty(userImg.getImageId())){
            String headImg = userImg.getImageUrl();
            if(headImg.indexOf("http") == -1){//修改过头像
                headImg = "/time"+headImg+"_320x240.jpg";
            }
            assembly.setUserImg(headImg);
        }
        List<ImageObj> imageObjList = imageObjDAO.findImgByObjIds("('"+assembly.getAssemblyId()+"')");
        if(!CollectionUtils.isEmpty(imageObjList)){
            List<String> contentImageList=new ArrayList<String>();
            for (ImageObj imageObj:imageObjList){
                if (imageObj.getImageType().equals(Contants.IMAGE_TYPE.ASSEMBLY_SHOW_IMG.toString())){
                    assembly.setTitleImg("/time"+imageObj.getImageUrl()+"_320x240.jpg");
                }else  if (imageObj.getImageType().equals(Contants.IMAGE_TYPE.ASSEMBLY_CONTENT_IMG.toString())){
                    contentImageList.add("/time"+imageObj.getImageUrl()+".jpg");
                }
            }
            assembly.setContentImgList(contentImageList);
        }
        List<Fee> feeList=feeService.findFeeByAssemblyId(assembly.getAssemblyId());
        if (!CollectionUtils.isEmpty(feeList)) {
            BigDecimal cost = new BigDecimal(0);
            for (int i = 0; i < feeList.size(); i++) {
                Fee fee = feeList.get(i);
                if (i == 0) {
                    cost = fee.getFee();
                } else {
                    if (fee.getFee().compareTo(cost) < 0) {
                        cost = fee.getFee();
                    }
                }
            }
            assembly.setCost(cost.toString());
            assembly.setFeeList(feeList);
        }
            return assembly;
    }

    @Override
    public List<Assembly> findAssemblyList(Assembly Assembly, int startIndex, int loadSize) {
        List<Assembly> list= assemblyDAO.findAssemblyList(Assembly,startIndex,loadSize);
        if (!CollectionUtils.isEmpty(list)){
            for (Assembly assembly:list){
                UserInfo userInfo=userService.findUserByUserId(assembly.getUserId());
                assembly.setUserName(userInfo.getNickName());
                ImageObj userImg = userService.findUserImg(assembly.getUserId(), Contants.IMAGE_TYPE.USER_HEAD.toString());
                if(userImg!=null && StringUtils.isNotEmpty(userImg.getImageId())){
                    String headImg = userImg.getImageUrl();
                    if(headImg.indexOf("http") == -1){//修改过头像
                        headImg = "/time"+headImg+"_320x240.jpg";
                    }
                    assembly.setUserImg(headImg);
                }
                ImageObj titleImg = userService.findUserImg(assembly.getAssemblyId(), Contants.IMAGE_TYPE.ASSEMBLY_SHOW_IMG.toString());
                if(titleImg!=null && StringUtils.isNotEmpty(titleImg.getImageId())){
                    assembly.setTitleImg("/time"+titleImg.getImageUrl()+".jpg");
                }
                List<Fee> feeList=feeService.findFeeByAssemblyId(assembly.getAssemblyId());
                if (!CollectionUtils.isEmpty(feeList)){
                    BigDecimal cost=new BigDecimal(0);
                    for (int i=0;i<feeList.size();i++){
                        Fee fee=feeList.get(i);
                        if (i==0){
                            cost=fee.getFee();
                        }else{
                            if (fee.getFee().compareTo(cost)<0){
                                cost=fee.getFee();
                            }
                        }
                    }
                    assembly.setCost(cost.toString());
                }
            }
        }
        return list;
    }

    @Override
    public int findAssemblyCount(Assembly Assembly) {
        return assemblyDAO.findAssemblyCount(Assembly);
    }
}
