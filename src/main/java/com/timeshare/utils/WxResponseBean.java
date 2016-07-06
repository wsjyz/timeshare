package com.timeshare.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by user on 2016/7/6.
 */
@XStreamAlias("xml")
public class WxResponseBean {

    public String return_code;
    public String return_msg;
    public String appid;
    public String mch_id;
    public String nonce_str;
    public String sign;
    public String result_code;
    public String prepay_id;
    public String trade_type;
    public String err_code;
    public String err_code_des;

    private static String removeTag(String str){
        if(str.indexOf("<![CDATA[") != -1){
            return str.substring(9,str.length() - 3);
        }
        return str;
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

    public String getAppid() {
        return removeTag(appid);
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return removeTag(mch_id);
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return removeTag(nonce_str);
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return removeTag(sign);
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return removeTag(result_code);
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getPrepay_id() {
        return removeTag(prepay_id);
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getTrade_type() {
        return removeTag(trade_type);
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
}
