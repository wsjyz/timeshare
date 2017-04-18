package com.timeshare.service.impl.crowdfunding;

import com.timeshare.dao.crowdfunding.CrowdFundingDAO;
import com.timeshare.dao.crowdfunding.EnrollDAO;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.domain.email.MailSenderInfo;
import com.timeshare.service.UserService;
import com.timeshare.service.crowdfunding.CrowdFundingService;
import com.timeshare.service.crowdfunding.EnrollService;
import com.timeshare.utils.SimpleMailSender;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by user on 2016/9/23.
 */
@Service("EnrollService")
public class EnrollServiceImpl implements EnrollService {

    @Autowired
    EnrollDAO enrollDAO;

    @Override
    public String saveEnroll(Enroll enroll) {
        return enrollDAO.saveEnroll(enroll);
    }

    @Override
    public List<Enroll> findEnrollByOwner(String userId, int startIndex, int loadSize) {
        return enrollDAO.findEnrollByOwner(userId,startIndex,loadSize);
    }

    @Override
    public int findEnrollByOwnerCount(String userId) {
        return enrollDAO.findEnrollByOwnerCount(userId);
    }
    public List<Enroll> findEnrollByCrowdfundingId(String crowdfundingId, int startIndex, int loadSize){
        return enrollDAO.findEnrollByCrowdfundingId(crowdfundingId,startIndex,loadSize);
    }

    @Override
    public Enroll findEnrollById(String enrollId) {
        List<Enroll> enrollList=enrollDAO.findEnrollById(enrollId);
        if(enrollList!=null && enrollList.size()>0){
            return enrollList.get(0);
        }
        return null;
    }

    public Boolean exportEnrollListToEmail(String crowdfundingId,String toEmailAddress) throws IOException {
        List<Enroll> enrollList=enrollDAO.findCrowdfundingEnrollList(crowdfundingId,0,0);

        //创建excel
        HSSFWorkbook hssfWorkbook=createSheetFormat(enrollList);
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
    private HSSFWorkbook createSheetFormat(List<Enroll> enrollList) throws UnsupportedEncodingException {
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

        for (int i = 1; i <= enrollList.size(); i++) {
            Enroll enroll = enrollList.get(i-1);
            row = sheet.createRow(i);
            row.setHeight((short)300);
            row.createCell(0).setCellValue(enroll.getUserName());//姓名
            row.createCell(1).setCellValue(enroll.getPhone());//手机号
        }
        return hssfWorkbook;
    }
    //修改报名对象
    public String modifyEnroll(Enroll enroll){
        return enrollDAO.modifyEnroll(enroll);
    }
    //我预约的众筹
    public List<Enroll> findCrowdfundingByMyEnroll(int startIndex, int loadSize,String userId) {
        if(StringUtils.isNotBlank(userId)){
            return enrollDAO.findCrowdfundingByMyEnroll(startIndex,loadSize,userId);
        }
        else{
            return null;
        }
    }
    //获取已报名对象
    public List<Enroll> findCrowdfundingEnrollList(String crowdfundingId){
        if(StringUtils.isNotBlank(crowdfundingId)){
            return enrollDAO.findCrowdfundingEnrollList(crowdfundingId,0,0);
        }
        else{
            return null;
        }
    }
    //获取已报名名单
    public List<Enroll> findCrowdfundingEnrollListToPaging(String crowdfundingId,int startIndex, int loadSize){
        if(StringUtils.isNotBlank(crowdfundingId)){
            return enrollDAO.findCrowdfundingEnrollList(crowdfundingId,startIndex,loadSize);
        }
        else{
            return null;
        }
    }
    //查询出需要自动退款的报名记录
    public List<Enroll> findNeedAotuRefundEnroll() {
        return enrollDAO.findNeedAotuRefundEnroll();
    }
    //自动退款完成后更新支付状态及退款交易号
    public String autoRefundAfterUpdate(String enrollId,String refundTradeNo) {
        return enrollDAO.autoRefundAfterUpdate(enrollId,refundTradeNo);
    }
    //自动更新现金账户可提现金额后更新已转移标志
    public String autoMoneyTransferAfterUpdate(String enrollId) {
        return enrollDAO.autoMoneyTransferAfterUpdate(enrollId);
    }
    //根据购买用户ID及项目ID 获取预约信息
    public List<Enroll> findEnrollByEnrollUserIdAndCrowdFundingId(String enrollUserId,String crowdFundingId) {
        return enrollDAO.findEnrollByEnrollUserIdAndCrowdFundingId( enrollUserId, crowdFundingId);
    }
    //根据购买用户ID判断用户是否已经购买过
    public Boolean enrollUserIdIsAlreadyBuy(String enrollUserId,String crowdFundingId) {
        List<Enroll> enrollList=enrollDAO.findEnrollByEnrollUserIdAndCrowdFundingId(enrollUserId,crowdFundingId);
        if(enrollList!=null && enrollList.size()>0){
            return true;
        }
        else{
            return true;
        }
    }
}
