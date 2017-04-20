package com.timeshare.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by user on 2016/7/13.
 */
@XStreamAlias("xml")
public class WxRefundParamsBean {
    //微信退款请求Bean

    //公众账号ID	appid	是	String(32)	wx8888888888888888	微信分配的公众账号ID（企业号corpid即为此appId）
    private String appid;

    //商户号	mch_id	是	String(32)	1900000109	微信支付分配的商户号
    private String mch_id;

    //随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
    private String nonce_str;

    //签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
    private String sign;

    //微信订单号	transaction_id	与 "商户订单号" 二选一	String(28)	1217752501201407033233368018	微信生成的订单号，在支付通知中有返回
    private String transaction_id;

    //商户订单号	out_trade_no	String(32)	1217752501201407033233368018	商户侧传给微信的订单号
    private String out_trade_no;

    //商户退款单号	out_refund_no	是	String(32)	1217752501201407033233368018	商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
    private String out_refund_no;

    //订单金额	total_fee	是	Int	100	订单总金额，单位为分，只能为整数，详见支付金额
    private Integer total_fee;

    //退款金额	refund_fee	是	Int	100	退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
    private Integer refund_fee;

    //操作员	op_user_id	是	String(32)	1900000109	操作员帐号, 默认为商户号
    private String op_user_id;

    //退款资金来源	refund_account	否	String(30)	REFUND_SOURCE_RECHARGE_FUNDS
    //仅针对老资金流商户使用
    //REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
    //REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款(限非当日交易订单的退款）
    private String refund_account;


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public Integer getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(Integer refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getOp_user_id() {
        return op_user_id;
    }

    public void setOp_user_id(String op_user_id) {
        this.op_user_id = op_user_id;
    }

    public String getRefund_account() {
        return refund_account;
    }

    public void setRefund_account(String refund_account) {
        this.refund_account = refund_account;
    }
}

