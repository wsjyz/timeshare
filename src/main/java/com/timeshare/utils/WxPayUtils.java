package com.timeshare.utils;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by user on 2016/10/12.
 */
public class WxPayUtils {

    protected static Logger payLogger = LoggerFactory.getLogger("payLogger");

    public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/";
    public static final String REFUND_URL="https://api.mch.weixin.qq.com/secapi/pay/refund";

    public static String payToSeller(String tradeNo,BigDecimal price ,String openId){

        payLogger.info("处理订单:,"+tradeNo+",提现金额为,"+price);
        WxPayParamsBean xmlParams = new WxPayParamsBean();
        SortedMap parameters = new TreeMap<>();
        xmlParams.setMch_appid(Contants.APPID);
        parameters.put("mch_appid",Contants.APPID);

        xmlParams.setMchid(Contants.MCHID);
        parameters.put("mchid",Contants.MCHID);

        String noceStr = CommonStringUtils.genPK();
        xmlParams.setNonce_str(noceStr);
        parameters.put("nonce_str",noceStr);

        xmlParams.setPartner_trade_no(tradeNo);
        parameters.put("partner_trade_no",tradeNo);

        xmlParams.setOpenid(openId);
        parameters.put("openid",openId);

        xmlParams.setCheck_name("NO_CHECK");
        parameters.put("check_name","NO_CHECK");

        int amount = FeeUtils.payAmount(price,new BigDecimal(0.006));
        xmlParams.setAmount(amount);
        parameters.put("amount",amount);

        xmlParams.setDesc("佣金");
        parameters.put("desc","佣金");

        xmlParams.setSpbill_create_ip("115.159.30.163");
        parameters.put("spbill_create_ip","115.159.30.163");

        String sign = WxUtils.createSign(parameters,Contants.KEY);
        xmlParams.setSign(sign);
        XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xs.processAnnotations(xmlParams.getClass());
        String xml = xs.toXML(xmlParams);

        HTTPSClient client = new HTTPSClient();
        try {
            client.setBodyParams(new String(xml.getBytes("UTF-8"),"ISO-8859-1"));//狗日的微信，神经病
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String p12FilePath = "/work/cert/apiclient_cert.p12";
        String postUri = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
        String response = client.httpsRequest(p12FilePath,Contants.MCHID,postUri);

        //logger.info("调用返回:"+response);//微信变态啊，这个返回不需要转码

        WxPayResponseBean bean = new WxPayResponseBean();
        xs.processAnnotations(bean.getClass());
        bean = (WxPayResponseBean)xs.fromXML(response);
        if(bean != null){
            if(bean.getReturn_code().equals("SUCCESS") && bean.getResult_code().equals("SUCCESS")){
                //更新数据库
                payLogger.info(tradeNo+"提现成功");
                return Contants.SUCCESS.toString();
            }else{
                //打印错误日志
                payLogger.info(tradeNo+"提现失败："+bean.getErr_code_des());
                return bean.getErr_code_des();
            }
        }
        return null;
    }

    public static String payRefund(String outRefundNo,String tradeNo,int totalFee,int refundFee){

        payLogger.info("处理订单退款:,"+tradeNo+",退款金额为,"+refundFee);
        WxRefundParamsBean xmlParams = new WxRefundParamsBean();
        SortedMap parameters = new TreeMap<>();
        xmlParams.setAppid(Contants.APPID);
        parameters.put("appid",Contants.APPID);

        xmlParams.setMch_id(Contants.MCHID);
        parameters.put("mch_id",Contants.MCHID);

        String noceStr = CommonStringUtils.genPK();
        xmlParams.setNonce_str(noceStr);
        parameters.put("nonce_str",noceStr);

        xmlParams.setOut_trade_no(tradeNo);
        parameters.put("out_trade_no",tradeNo);

        xmlParams.setOut_refund_no(outRefundNo);
        parameters.put("out_refund_no",outRefundNo);

        xmlParams.setTotal_fee(totalFee);
        parameters.put("total_fee",totalFee);

        xmlParams.setRefund_fee(refundFee);
        parameters.put("refund_fee",refundFee);

        xmlParams.setOp_user_id(Contants.MCHID);
        parameters.put("op_user_id",Contants.MCHID);
        //使用余额来退款，当日交易不可退款，次日进行退款
        xmlParams.setRefund_account("REFUND_SOURCE_RECHARGE_FUNDS");
        parameters.put("refund_account","REFUND_SOURCE_RECHARGE_FUNDS");


        String sign = WxUtils.createSign(parameters,Contants.KEY);
        xmlParams.setSign(sign);
        XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xs.processAnnotations(xmlParams.getClass());
        String xml = xs.toXML(xmlParams);

        HTTPSClient client = new HTTPSClient();
        try {
            client.setBodyParams(new String(xml.getBytes("UTF-8"),"ISO-8859-1"));//狗日的微信，神经病
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String p12FilePath = "/work/cert/apiclient_cert.p12";
        String postUri = REFUND_URL;

        String response = client.httpsRequest(p12FilePath,Contants.MCHID,postUri);

        WxRefundResponseBean bean = new WxRefundResponseBean();
        xs.processAnnotations(bean.getClass());
        bean = (WxRefundResponseBean)xs.fromXML(response);
        if(bean != null){
            if(bean.getReturn_code().equals("SUCCESS") && bean.getResult_code().equals("SUCCESS")){
                //更新数据库
                payLogger.info(tradeNo+"退款成功");
                return Contants.SUCCESS.toString();
            }else{
                //打印错误日志
                payLogger.info(tradeNo+"退款失败："+bean.getResult_code()+"|"+bean.getErr_code()+"|"+bean.getErr_code_des());
            }
        }
        return null;
    }
    public static String userPayToCorp(String code,String bodyStr,BigDecimal price,String outTradeNo){


        WeixinOauth weixinOauth = new WeixinOauth();
        String openId = weixinOauth.obtainOpenId(code);
        WxPayConfigBean config = new WxPayConfigBean();
        SortedMap parameters = new TreeMap<>();

        config.setAppid(Contants.APPID);
        parameters.put("appid",Contants.APPID);

        config.setMch_id(Contants.MCHID);
        parameters.put("mch_id",Contants.MCHID);

        String noceStr = CommonStringUtils.genPK();
        config.setNonce_str(noceStr);
        parameters.put("nonce_str",noceStr);


        config.setBody(bodyStr);
        parameters.put("body",bodyStr);


        config.setOut_trade_no(outTradeNo);
        parameters.put("out_trade_no",outTradeNo);


        int fenPrice = (price.multiply(new BigDecimal(100))).intValue();
        System.out.println(" 价格为 "+fenPrice);
        config.setTotal_fee(fenPrice);
        parameters.put("total_fee",fenPrice);

        //ip sbwx
        config.setSpbill_create_ip("123.0.1.2");
        parameters.put("spbill_create_ip","123.0.1.2");

        config.setTrade_type("JSAPI");
        parameters.put("trade_type","JSAPI");

        config.setNotify_url("http://jk.zhangqidong.cn/time/wxPay/notify-url");
        parameters.put("notify_url","http://jk.zhangqidong.cn/time/wxPay/notify-url");

        config.setOpenid(openId);
        parameters.put("openid",openId);

        parameters.put("key",Contants.KEY);

        String signStr = WxUtils.createSign(parameters,Contants.KEY);
        config.setSign(signStr);

        XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xs.processAnnotations(config.getClass());
        String xml = xs.toXML(config);
        System.out.println("xml is "+xml);

        HTTPSClient client = new HTTPSClient();
        client.setSERVER_HOST_URL(UNIFIEDORDER_URL);
        client.setServiceUri("unifiedorder");
        try {
            client.setBodyParams(new String(xml.getBytes("UTF-8"),"ISO-8859-1"));//狗日的微信，神经病
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String response = client.request();
        System.out.println("response is "+response);


        WxResponseBean responseBean = new WxResponseBean();
        xs.processAnnotations(responseBean.getClass());
        responseBean = (WxResponseBean)xs.fromXML(response);
        String prepayId = "";
        String payStatus = "NOTPAY";
        String jsApiParams = "\'\'";
        if(responseBean.getReturn_code() != null && responseBean.getReturn_code().equals("SUCCESS")){

            if(responseBean.getResult_code() != null && responseBean.getResult_code().equals("FAIL")){
                if(responseBean.getErr_code().equals("ORDERPAID")){//已支付过了
                    payStatus = "ORDERPAID";
                }

            }else if(responseBean.getResult_code() != null && responseBean.getResult_code().equals("SUCCESS")){
                prepayId = responseBean.getPrepay_id();
                SortedMap signMap = new TreeMap<>();
                signMap.put("appId",Contants.APPID);
                String timestampStr = System.currentTimeMillis()+"";
                signMap.put("timeStamp",timestampStr);
                String randomStr = CommonStringUtils.genPK();
                signMap.put("nonceStr",randomStr);
                signMap.put("package","prepay_id="+prepayId);
                signMap.put("signType","MD5");

                String paySign = WxUtils.createSign(signMap,Contants.KEY);
                signMap.put("paySign",paySign);
                jsApiParams = JSON.toJSONString(signMap);
                System.out.println("jsApiParams "+jsApiParams);

            }

        }
        return jsApiParams;
    }

    public static String userPayToCorp(String code,String bodyStr,BigDecimal price){
        String outTradeNo = CommonStringUtils.gen18RandomNumber();
        return userPayToCorp(code,bodyStr,price,outTradeNo);
    }
    public static String userPayToCorpByHuodong(String code,String bodyStr,BigDecimal price,WeixinOauth weixinOauth,String openId){
        String outTradeNo = CommonStringUtils.gen18RandomNumber();
        WxPayConfigBean config = new WxPayConfigBean();
        SortedMap parameters = new TreeMap<>();

        config.setAppid(Contants.APPID);
        parameters.put("appid",Contants.APPID);

        config.setMch_id(Contants.MCHID);
        parameters.put("mch_id",Contants.MCHID);

        String noceStr = CommonStringUtils.genPK();
        config.setNonce_str(noceStr);
        parameters.put("nonce_str",noceStr);


        config.setBody(bodyStr);
        parameters.put("body",bodyStr);


        config.setOut_trade_no(outTradeNo);
        parameters.put("out_trade_no",outTradeNo);


        int fenPrice = (price.multiply(new BigDecimal(100))).intValue();
        System.out.println(" 价格为 "+fenPrice);
        config.setTotal_fee(fenPrice);
        parameters.put("total_fee",fenPrice);

        //ip sbwx
        config.setSpbill_create_ip("123.0.1.2");
        parameters.put("spbill_create_ip","123.0.1.2");

        config.setTrade_type("JSAPI");
        parameters.put("trade_type","JSAPI");

        config.setNotify_url("http://jk.zhangqidong.cn/time/wxPay/notify-url");
        parameters.put("notify_url","http://jk.zhangqidong.cn/time/wxPay/notify-url");

        config.setOpenid(openId);
        parameters.put("openid",openId);

        parameters.put("key",Contants.KEY);

        String signStr = WxUtils.createSign(parameters,Contants.KEY);
        config.setSign(signStr);

        XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xs.processAnnotations(config.getClass());
        String xml = xs.toXML(config);
        System.out.println("xml is "+xml);

        HTTPSClient client = new HTTPSClient();
        client.setSERVER_HOST_URL(UNIFIEDORDER_URL);
        client.setServiceUri("unifiedorder");
        try {
            client.setBodyParams(new String(xml.getBytes("UTF-8"),"ISO-8859-1"));//狗日的微信，神经病
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String response = client.request();
        System.out.println("response is "+response);


        WxResponseBean responseBean = new WxResponseBean();
        xs.processAnnotations(responseBean.getClass());
        responseBean = (WxResponseBean)xs.fromXML(response);
        String prepayId = "";
        String payStatus = "NOTPAY";
        String jsApiParams = "\'\'";
        if(responseBean.getReturn_code() != null && responseBean.getReturn_code().equals("SUCCESS")){

            if(responseBean.getResult_code() != null && responseBean.getResult_code().equals("FAIL")){
                if(responseBean.getErr_code().equals("ORDERPAID")){//已支付过了
                    payStatus = "ORDERPAID";
                }

            }else if(responseBean.getResult_code() != null && responseBean.getResult_code().equals("SUCCESS")){
                prepayId = responseBean.getPrepay_id();
                SortedMap signMap = new TreeMap<>();
                signMap.put("appId",Contants.APPID);
                String timestampStr = System.currentTimeMillis()+"";
                signMap.put("timeStamp",timestampStr);
                String randomStr = CommonStringUtils.genPK();
                signMap.put("nonceStr",randomStr);
                signMap.put("package","prepay_id="+prepayId);
                signMap.put("signType","MD5");

                String paySign = WxUtils.createSign(signMap,Contants.KEY);
                signMap.put("paySign",paySign);
                jsApiParams = JSON.toJSONString(signMap);
                System.out.println("jsApiParams "+jsApiParams);

            }

        }
        return jsApiParams;
    }
}
