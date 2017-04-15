package com.timeshare.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by user on 2016/7/6.
 */
@XStreamAlias("xml")
public class WxRefundResponseBean {

//    以下字段在return_code为SUCCESS的时候有返回
//
//    字段名	变量名	必填	类型	示例值	描述
//    业务结果	result_code	是	String(16)	SUCCESS
//    SUCCESS/FAIL
//    SUCCESS退款申请接收成功，结果通过退款查询接口查询
//    FAIL 提交业务失败
//    错误代码	err_code	否	String(32)	SYSTEMERROR	列表详见错误码列表
//    错误代码描述	err_code_des	否	String(128)	系统超时	结果信息描述
//    公众账号ID	appid	是	String(32)	wx8888888888888888	微信分配的公众账号ID
//    商户号	mch_id	是	String(32)	1900000109	微信支付分配的商户号
//    设备号	device_info	否	String(32)	013467007045764	微信支付分配的终端设备号，与下单一致
//    随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位
//    签名	sign	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	签名，详见签名算法
//    微信订单号	transaction_id	是	String(28)	4007752501201407033233368018	微信订单号
//    商户订单号	out_trade_no	是	String(32)	33368018	商户系统内部的订单号
//    商户退款单号	out_refund_no	是	String(32)	121775250	商户退款单号
//    微信退款单号	refund_id	是	String(32)	2007752501201407033233368018	微信退款单号
//    退款金额	refund_fee	是	Int	100	退款总金额,单位为分,可以做部分退款
//    应结退款金额	settlement_refund_fee	否	Int	100	去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
//    标价金额	total_fee	是	Int	100	订单总金额，单位为分，只能为整数，详见支付金额
//    应结订单金额	settlement_total_fee	否	Int	100	去掉非充值代金券金额后的订单总金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
//    标价币种	fee_type	否	String(8)	CNY	订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
//    现金支付金额	cash_fee	是	Int	100	现金支付金额，单位为分，只能为整数，详见支付金额
//    现金支付币种	cash_fee_type	否	String(16)	CNY	货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
//    现金退款金额	cash_refund_fee	否	Int	100	现金退款金额，单位为分，只能为整数，详见支付金额
//    代金券类型	coupon_type_$n	否	String(8)	CASH
//    CASH--充值代金券
//    NO_CASH---非充值代金券
//    订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
//    代金券退款总金额	coupon_refund_fee	否	Int	100	代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
//    单个代金券退款金额	coupon_refund_fee_$n	否	Int	100	代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
//    退款代金券使用数量	coupon_refund_count	否	Int	1	退款代金券使用数量
//    退款代金券ID	coupon_refund_id_$n	否	String(20)	10000 	退款代金券ID, $n为下标，从0开始编号

    public String return_code;
    public String return_msg;
    public String result_code;
    public String err_code;
    public String err_code_des;
    public String appid;
    public String mch_appid;
    public String mch_id;
    public String device_info;
    public String nonce_str;
    public String sign;
    public String transaction_id;
    public String out_trade_no;
    public String out_refund_no;
    public String refund_id;
    public String refund_fee;
    public String settlement_refund_fee;
    public String total_fee;
    public String settlement_total_fee;
    public String fee_type;
    public String cash_fee;
    public String cash_fee_type;
    public String cash_refund_fee;
    public String coupon_type_$n;
    public String coupon_refund_fee;
    public String coupon_refund_fee_$n;
    public String coupon_refund_count;
    public String coupon_refund_id_$n;
    public String refund_channel;

    private static String removeTag(String str){
        if(str.indexOf("<![CDATA[") != -1){
            return str.substring(9,str.length() - 3);
        }
        return str;
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

    public String getResult_code() {
        return removeTag(result_code);
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
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

    public String getAppid() {
        return removeTag(appid);
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_appid() {
        return removeTag(mch_appid);
    }

    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    public String getMch_id() {
        return removeTag(mch_id);
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return removeTag(device_info);
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
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

    public String getTransaction_id() {
        return removeTag(transaction_id);
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return removeTag(out_trade_no);
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_refund_no() {
        return removeTag(out_refund_no);
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public String getRefund_id() {
        return removeTag(refund_id);
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getRefund_fee() {
        return removeTag(refund_fee);
    }

    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getSettlement_refund_fee() {
        return removeTag(settlement_refund_fee);
    }

    public void setSettlement_refund_fee(String settlement_refund_fee) {
        this.settlement_refund_fee = settlement_refund_fee;
    }

    public String getTotal_fee() {
        return removeTag(total_fee);
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getSettlement_total_fee() {
        return removeTag(settlement_total_fee);
    }

    public void setSettlement_total_fee(String settlement_total_fee) {
        this.settlement_total_fee = settlement_total_fee;
    }

    public String getFee_type() {
        return removeTag(fee_type);
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getCash_fee() {
        return removeTag(cash_fee);
    }

    public void setCash_fee(String cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getCash_fee_type() {
        return removeTag(cash_fee_type);
    }

    public void setCash_fee_type(String cash_fee_type) {
        this.cash_fee_type = cash_fee_type;
    }

    public String getCash_refund_fee() {
        return removeTag(cash_refund_fee);
    }

    public void setCash_refund_fee(String cash_refund_fee) {
        this.cash_refund_fee = cash_refund_fee;
    }

    public String getCoupon_type_$n() {
        return removeTag(coupon_type_$n);
    }

    public void setCoupon_type_$n(String coupon_type_$n) {
        this.coupon_type_$n = coupon_type_$n;
    }

    public String getCoupon_refund_fee() {
        return removeTag(coupon_refund_fee);
    }

    public void setCoupon_refund_fee(String coupon_refund_fee) {
        this.coupon_refund_fee = coupon_refund_fee;
    }

    public String getCoupon_refund_fee_$n() {
        return removeTag(coupon_refund_fee_$n);
    }

    public void setCoupon_refund_fee_$n(String coupon_refund_fee_$n) {
        this.coupon_refund_fee_$n = coupon_refund_fee_$n;
    }

    public String getCoupon_refund_count() {
        return removeTag(coupon_refund_count);
    }

    public void setCoupon_refund_count(String coupon_refund_count) {
        this.coupon_refund_count = coupon_refund_count;
    }

    public String getCoupon_refund_id_$n() {
        return removeTag(coupon_refund_id_$n);
    }

    public void setCoupon_refund_id_$n(String coupon_refund_id_$n) {
        this.coupon_refund_id_$n = coupon_refund_id_$n;
    }
}
