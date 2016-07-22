package com.timeshare.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by user on 2016/7/6.
 */
@XStreamAlias("xml")
public class WxPayResponseBean {

    public String return_code;
    public String return_msg;
    public String mch_appid;
    public String mchid;
    public String device_info;
    public String partner_trade_no;
    public String payment_no;
    public String payment_time;
    public String nonce_str;
    public String result_code;
    public String err_code;
    public String err_code_des;

    private static String removeTag(String str){
        if(str.indexOf("<![CDATA[") != -1){
            return str.substring(9,str.length() - 3);
        }
        return str;
    }

    public String getMch_appid() {
        return removeTag(mch_appid);
    }

    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    public String getMchid() {
        return removeTag(mchid);
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getDevice_info() {
        return removeTag(device_info);
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getPartner_trade_no() {
        return removeTag(partner_trade_no);
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getPayment_no() {
        return removeTag(payment_no);
    }

    public void setPayment_no(String payment_no) {
        this.payment_no = payment_no;
    }

    public String getPayment_time() {
        return removeTag(payment_time);
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getErr_code() {
        return removeTag(err_code);
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return removeTag(err_code_des);
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getReturn_code() {

        return removeTag(return_code);
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return removeTag(return_msg);
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }



    public String getNonce_str() {
        return removeTag(nonce_str);
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }



    public String getResult_code() {
        return removeTag(result_code);
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }


}
