package com.timeshare.utils;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * Created by user on 2016/7/14.
 */
public class SmsUtils {

    public static final String SMS_SEND_URL = "http://gw.api.taobao.com/router/rest";
    public static final String APPKEY = "23451679";
    public static final String SECRET = "877ebd4accbe5d3436d4fe7223c7dc81";
    public static final String SIGN = "邂逅时刻";

    public static String senMessage(SmsContentBean bean){
        TaobaoClient client = new DefaultTaobaoClient(SMS_SEND_URL, APPKEY, SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        //req.setExtend("123456");
        req.setSmsType("normal");
        req.setSmsFreeSignName(SIGN);
        req.setSmsParamString(bean.getContent());
        req.setRecNum(bean.getToMobile());
        req.setSmsTemplateCode(bean.getTemplateCode());
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());
        return rsp.getBody();
    }
}
