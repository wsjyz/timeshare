package com.timeshare.service.impl.assembly;

import com.timeshare.dao.BidDAO;
import com.timeshare.dao.ImageObjDAO;
import com.timeshare.dao.assembly.AssemblyDAO;
import com.timeshare.domain.Bid;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Assembly;
import com.timeshare.domain.assembly.Attender;
import com.timeshare.domain.assembly.Fee;
import com.timeshare.service.BidService;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.AssemblyService;
import com.timeshare.service.assembly.AttenderService;
import com.timeshare.service.assembly.FeeService;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.awt.*;
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
    @Autowired
    AttenderService attenderService;
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
            List<ImageObj> contentImageList=new ArrayList<ImageObj>();
            String imageId="";
            String imgUrl="";
            for (ImageObj imageObj:imageObjList){
                if (imageObj.getImageType().equals(Contants.IMAGE_TYPE.ASSEMBLY_SHOW_IMG.toString())){
                    assembly.setTitleImg("/time"+imageObj.getImageUrl()+"_320x240.jpg");
                    assembly.setTitleImageId(imageObj.getImageId());
                }else  if (imageObj.getImageType().equals(Contants.IMAGE_TYPE.ASSEMBLY_CONTENT_IMG.toString())){
                    imageObj.setImageUrl("/time"+imageObj.getImageUrl()+".jpg");
                    imageId=imageObj.getImageId()+","+imageId;
                    contentImageList.add(imageObj);
                }else  if (imageObj.getImageType().equals(Contants.IMAGE_TYPE.ASSEMBLY_CONSULTATION_IMG.toString())){
                    assembly.setConsultationImg("/time"+imageObj.getImageUrl()+"_320x240.jpg");
                    assembly.setConsultationImgId(imageObj.getImageId());
                }
            }
            assembly.setContentImgIds(imageId);
            assembly.setContentImgList(contentImageList);
        }
        List<Fee> feeList=feeService.findFeeByAssemblyId(assembly.getAssemblyId());
        if (!CollectionUtils.isEmpty(feeList)) {
            String feeId="";
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
                feeId=fee.getFeeId()+","+feeId;
            }
            assembly.setCost(cost.toString());
            assembly.setFeeIds(feeId);
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
                    int quota=0;
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
                        if (fee.getQuota()==0){
                            quota=-1;
                        }
                        if (quota!=-1){
                            quota=quota+fee.getQuota();
                        }
                    }
                    assembly.setCost(cost.toString());
                    if (quota==-1){
                        assembly.setQuota("不限制");
                    }else{
                        assembly.setQuota(quota+"");
                    }
                }
                List<Attender> attenderList = attenderService.getListByAssemblyId(assembly.getAssemblyId());
                assembly.setAttentCount(attenderList.size());
            }
        }
        return list;
    }

    @Override
    public int findAssemblyCount(Assembly Assembly) {
        return assemblyDAO.findAssemblyCount(Assembly);
    }

    @Override
    public List<Assembly> findSignAssemblyList(Assembly Assembly, String userId, int startIndex, int loadSize) {

        List<Assembly> list=  assemblyDAO.findSignAssemblyList(Assembly,userId,startIndex,loadSize);
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
                    int quota=0;
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
                        if (fee.getQuota()==0){
                            quota=-1;
                        }
                        if (quota!=-1){
                            quota=quota+fee.getQuota();
                        }
                    }
                    assembly.setCost(cost.toString());
                    if (quota==-1){
                        assembly.setQuota("不限制");
                    }else{
                        assembly.setQuota(quota+"");
                    }
                }
                List<Attender> attenderList = attenderService.getListByAssemblyId(assembly.getAssemblyId());
                assembly.setAttentCount(attenderList.size());
            }
        }
        return list;
    }

    @Override
    public int deleteAssembly(String assemblyId) {
        return assemblyDAO.deleteAssembly(assemblyId);
    }

    @Override
    public List<Assembly> findCollectionAssemblyList(Assembly Assembly, String userId, int startIndex, int loadSize) {
        List<Assembly> list=  assemblyDAO.findCollectionAssemblyList(Assembly,  userId,  startIndex,  loadSize);
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
                    int quota=0;
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
                        if (fee.getQuota()==0){
                            quota=-1;
                        }
                        if (quota!=-1){
                            quota=quota+fee.getQuota();
                        }
                    }
                    assembly.setCost(cost.toString());
                    if (quota==-1){
                        assembly.setQuota("不限制");
                    }else{
                        assembly.setQuota(quota+"");
                    }
                }
                List<Attender> attenderList = attenderService.getListByAssemblyId(assembly.getAssemblyId());
                assembly.setAttentCount(attenderList.size());
            }
        }
        return list;
    }
}
