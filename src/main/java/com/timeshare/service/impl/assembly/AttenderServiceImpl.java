package com.timeshare.service.impl.assembly;

import com.timeshare.dao.assembly.AssemblyDAO;
import com.timeshare.dao.assembly.AttenderDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.domain.assembly.Assembly;
import com.timeshare.domain.assembly.Attender;
import com.timeshare.domain.assembly.Fee;
import com.timeshare.domain.email.MailSenderInfo;
import com.timeshare.service.UserService;
import com.timeshare.service.assembly.AssemblyService;
import com.timeshare.service.assembly.AttenderService;
import com.timeshare.utils.Contants;
import com.timeshare.utils.SimpleMailSender;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        List<Attender> list= attenderDAO.findAttenderList(Attender,startIndex,loadSize);
        if (!CollectionUtils.isEmpty(list)){
            for (Attender attender:list){
                UserInfo userInfo=userService.findUserByUserId(attender.getUserId());
                if (userInfo!=null){
                    attender.setUserName(userInfo.getNickName());
                    ImageObj userImg = userService.findUserImg(attender.getUserId(), Contants.IMAGE_TYPE.USER_HEAD.toString());
                    if(userImg!=null && StringUtils.isNotEmpty(userImg.getImageId())){
                        String headImg = userImg.getImageUrl();
                        if(headImg.indexOf("http") == -1){//修改过头像
                            headImg = "/time"+headImg+"_320x240.jpg";
                        }
                        attender.setUserImg(headImg);
                    }
                }
            }
        }else{
            list=new ArrayList<Attender>();
        }
        return list;
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
                if (userInfo!=null){
                    attender.setUserName(userInfo.getNickName());
                }
                ImageObj userImg = userService.findUserImg(attender.getUserId(), Contants.IMAGE_TYPE.USER_HEAD.toString());
                if(userImg!=null && StringUtils.isNotEmpty(userImg.getImageId())){
                    String headImg = userImg.getImageUrl();
                    if(headImg.indexOf("http") == -1){//修改过头像
                        headImg = "/time"+headImg+"_320x240.jpg";
                    }
                    attender.setUserImg(headImg);
                }
            }
        }else{
            list=new ArrayList<Attender>();
        }
        return list;
    }

    @Override
    public Boolean exportAttenderListToEmail(String assemblyId, String toEmailAddress )throws IOException {
        List<Attender> attenderList = getListByAssemblyId(assemblyId);

        //创建excel
        HSSFWorkbook hssfWorkbook=createSheetFormat(attenderList);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        hssfWorkbook.write(baos);
        InputStream excelFile1= new ByteArrayInputStream(baos.toByteArray());



        //发送邮件
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/email.properties"));
        //这个类主要是设置邮件
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost(properties.getProperty("EMAIL_SMTP"));
        mailInfo.setMailServerPort(properties.getProperty("EMAIL_SMTP"));
        mailInfo.setValidate(true);
        mailInfo.setUserName(properties.getProperty("EMAIL_USER_NAME"));
        mailInfo.setPassword(properties.getProperty("EMAIL_PASS_WORD"));//您的邮箱密码
        mailInfo.setFromAddress(properties.getProperty("EMAIL_USER_NAME"));
        mailInfo.setToAddress(toEmailAddress);
        mailInfo.setSubject("报名名单");
        mailInfo.setContent("报名名单详见附件，请查收!");
        //设置附件
        mailInfo.setAttachFileName("enroll_list.xls");
        mailInfo.setInputStream(excelFile1);
        //这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        //sms.sendTextMail(mailInfo);//发送文体格式
        Boolean result=sms.sendHtmlMail(mailInfo);//发送html格式
        return result;
    }
    //创建excel
    private HSSFWorkbook createSheetFormat(List<Attender> attenderList ) throws UnsupportedEncodingException {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        Sheet sheet = hssfWorkbook.createSheet("报名名单");
        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 3000);

        CellStyle cellStyle = hssfWorkbook.createCellStyle();
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        hssfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        hssfCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Row row = sheet.createRow(0);
        row.setHeight((short)300);

        Cell cell0 = row.createCell(0);
        cell0.setCellValue("姓名");
        cell0.setCellStyle(cellStyle);

        Cell cell1 = row.createCell(1);
        cell1.setCellValue("手机号");
        cell1.setCellStyle(cellStyle);
        Cell cell2 = row.createCell(2);
        cell2.setCellValue("报名数量");
        cell2.setCellStyle(cellStyle);

        for (int i = 1; i <= attenderList.size(); i++) {
            Attender enroll = attenderList.get(i-1);
            row = sheet.createRow(i);
            row.setHeight((short)300);
            row.createCell(0).setCellValue(enroll.getUserName());//姓名
            row.createCell(1).setCellValue(enroll.getPhone());//手机号
            row.createCell(2).setCellValue(enroll.getUserCount());//手机号

        }
        return hssfWorkbook;
    }
}
