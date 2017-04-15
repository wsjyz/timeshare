package com.timeshare.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by user on 2016/7/6.
 */
public class WxUtils {

    public static AccessTokenBean weixinAccessTokenBean;
    public static JsapiTicketBean jsapiTicketBean;
    public static final String ACCESSTOKENURL = "https://api.weixin.qq.com/cgi-bin/token";
    public static final String TICKETURL = "https://api.weixin.qq.com/cgi-bin/ticket/";

    public static String createSign(SortedMap parameters,String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(entry != null){
                String k = (String)entry.getKey();
                System.out.println("createSign... k:"+k);
                String v = entry.getValue().toString();
                System.out.println("createSign... v:"+v);
                if(null != v && !"".equals(v)
                        && !"sign".equals(k) && !"key".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }

        }
        sb.append("key=" + key);


        System.out.println("sign "+sb.toString());
        String sign = CommonStringUtils.MD5(sb.toString(),"utf-8");

        return sign;

    }
    private static AccessTokenBean obtainAccessToken(){
        weixinAccessTokenBean = new AccessTokenBean();
        OkhttpClient client = new OkhttpClient();

        Map<String,Object> paramsMap = new HashMap<String,Object>();
        paramsMap.put("appid", Contants.APPID);
        paramsMap.put("secret",Contants.SECRET);
        paramsMap.put("grant_type","client_credential");
        String response = client.post(ACCESSTOKENURL,paramsMap);
        System.out.println(response);
        AccessTokenBean accessTokenBean = JSON.parseObject(response,new TypeReference<AccessTokenBean>(){});
        return accessTokenBean;
    }

    public static AccessTokenBean getAccessToken(){
        long currentTime = new Date().getTime();

        if(weixinAccessTokenBean == null || weixinAccessTokenBean.getExpires_in() <= currentTime){
            AccessTokenBean newToken = obtainAccessToken();
            weixinAccessTokenBean = newToken;
            weixinAccessTokenBean.setExpires_in(currentTime+weixinAccessTokenBean.getExpires_in()*1000);
        }

        return weixinAccessTokenBean;

    }
    private static JsapiTicketBean obtainJsapiTicket(){
        jsapiTicketBean = new JsapiTicketBean();
        HTTPSClient client = new HTTPSClient();
        client.setSERVER_HOST_URL(TICKETURL);
        client.setServiceUri("getticket");
        Map<String,Object> paramsMap = new HashMap<String,Object>();
        paramsMap.put("access_token", getAccessToken().getAccess_token());
        paramsMap.put("type","jsapi");
        client.setParams(paramsMap);
        String response = client.request();
        JsapiTicketBean ticketBean = JSON.parseObject(response,new TypeReference<JsapiTicketBean>(){});
        return ticketBean;
    }

    public static JsapiTicketBean getJsapiTicket(){
        long currentTime = new Date().getTime();

        if(jsapiTicketBean == null || jsapiTicketBean.getExpires_in() <= currentTime){
            JsapiTicketBean newTicket = obtainJsapiTicket();
            jsapiTicketBean = newTicket;
            jsapiTicketBean.setExpires_in(currentTime+jsapiTicketBean.getExpires_in()*1000);
        }

        return jsapiTicketBean;

    }
    public static String getUrl(HttpServletRequest request){

        StringBuffer requestUrl = request.getRequestURL();

        String queryString = request.getQueryString();
        if(StringUtils.isNotBlank(queryString)){
            requestUrl.append("?"+queryString);
        }

        return requestUrl.toString();
    }
    public static Map<String, String> sign(String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = CommonStringUtils.genPK();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String str;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        String jsapi_ticket = getJsapiTicket().getTicket();

        str = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId",Contants.APPID);
        for(Map.Entry<String,String> entry:ret.entrySet()){
            System.out.println(entry.getKey()+"-"+entry.getValue());
        }
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
