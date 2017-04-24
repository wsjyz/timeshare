package com.timeshare.utils;

import com.alibaba.fastjson.JSON;

import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by user on 2016/6/21.
 */
public class CommonStringUtils {

    public static String genPK() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String gen18RandomNumber(){

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char firstChar = chars.charAt((int)(Math.random() * 26));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        return firstChar + sdf.format(new Date());
    }

    public static String MD5(String s,String charSetName) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes(charSetName);
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stringToDate(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



    public static void main(String[] args) {
//        String s = "appid=wxcd8903dd6a9552eb&body=agw|管理员&mch_id=1358876502&nonce_str=eaf60281f10e457eb0a83d6f59124f3b&out_trade_no=2d420b55a30948579f3ed7c8c091502a&spbill_create_ip=123.0.1.2&total_fee=1&trade_type=JSAPI&key=5bab4e2c09194e6198fe351a04991949";
//        //System.out.println(MD5(s,"gbk"));
//
//        Map<String,String> jsParmsMap = new HashMap<>();
//        jsParmsMap.put("appId","xxx");
//        jsParmsMap.put("timeStamp",System.currentTimeMillis()+"");
//        jsParmsMap.put("nonceStr",genPK());
//        jsParmsMap.put("package","prepay_id=fuck");
//        jsParmsMap.put("signType","MD5");
//        jsParmsMap.put("paySign","xxx");
//        System.out.println(JSON.toJSONString(jsParmsMap));
        for(int i = 0;i < 20;i ++){
            String randomStr = gen18RandomNumber();
            String insertSQL = "INSERT INTO `m`.`t_wgs_invitation` (`invitation_id`, `invitation_code`, `create_time`, `status`)" +
                    " VALUES ('"+randomStr+"', '"+randomStr+"', NULL, 'NO_USED');";
            System.out.println(insertSQL);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //小米推送密钥
    private static final String APP_SECRET_KEY = "GBXx3bqW+oOuZFspoH8ELA==";

    public static Result sendMessage(String title, String json, String wxId)  {
        Constants.useOfficial();
        Sender sender = new Sender(APP_SECRET_KEY);
        String messagePayload= json;
        String description ="wgs";
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName("")
                .notifyType(1)     // 使用默认提示音提示
                .build();
        Result result = null;//根据regID，发送消息到指定设备上，不重试。
        try {
            try {
                result = sender.sendToUserAccount(message, wxId, 0);
            } catch (org.json.simple.parser.ParseException e) {
                e.printStackTrace();
            }
            System.out.println("Server response: "+"MessageId: " + result.getMessageId()+ " ErrorCode: " + result.getErrorCode().getValue()+ " Reason: " + result.getReason());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
