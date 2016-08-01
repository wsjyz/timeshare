package com.timeshare.utils;

import com.alibaba.fastjson.JSON;

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
        String s = "appid=wxcd8903dd6a9552eb&body=agw|管理员&mch_id=1358876502&nonce_str=eaf60281f10e457eb0a83d6f59124f3b&out_trade_no=2d420b55a30948579f3ed7c8c091502a&spbill_create_ip=123.0.1.2&total_fee=1&trade_type=JSAPI&key=5bab4e2c09194e6198fe351a04991949";
        //System.out.println(MD5(s,"gbk"));

        Map<String,String> jsParmsMap = new HashMap<>();
        jsParmsMap.put("appId","xxx");
        jsParmsMap.put("timeStamp",System.currentTimeMillis()+"");
        jsParmsMap.put("nonceStr",genPK());
        jsParmsMap.put("package","prepay_id=fuck");
        jsParmsMap.put("signType","MD5");
        jsParmsMap.put("paySign","xxx");
        System.out.println(JSON.toJSONString(jsParmsMap));
    }

}
