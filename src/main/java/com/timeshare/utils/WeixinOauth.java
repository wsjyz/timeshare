package com.timeshare.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dam on 2014/12/27.
 */
public class WeixinOauth {

    public static final String ACCESSTOKENURL = "https://api.weixin.qq.com/sns/oauth2/";
    public static final String USERINFOURL = "https://api.weixin.qq.com/sns/";
    public static final String REFRESH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/";
    public static AccessTokenBean weixinOauthAccessTokenBean;


    /**
     * 这一步获取access_token和openid
     * @param code
     */
    public AccessTokenBean obtainOauthAccessToken(String code){
        weixinOauthAccessTokenBean = new AccessTokenBean();
        HTTPSClient client = new HTTPSClient();
        client.setSERVER_HOST_URL(ACCESSTOKENURL);
        client.setServiceUri("access_token");
        Map<String,Object> paramsMap = new HashMap<String,Object>();
        paramsMap.put("appid", Contants.APPID);
        paramsMap.put("secret",Contants.SECRET);
        paramsMap.put("code",code);
        paramsMap.put("grant_type","authorization_code");
        client.setParams(paramsMap);
        String response = client.request();
        AccessTokenBean accessTokenBean = JSON.parseObject(response,new TypeReference<AccessTokenBean>(){});
        weixinOauthAccessTokenBean = accessTokenBean;
        long currentTime = new Date().getTime();
        weixinOauthAccessTokenBean.setExpires_in(currentTime+accessTokenBean.getExpires_in()*1000);

        return accessTokenBean;
    }

    /**
     * 获取openId
     * @param code
     * @return
     */
    public String obtainOpenId(String code){
        HTTPSClient client = new HTTPSClient();
        client.setSERVER_HOST_URL(ACCESSTOKENURL);
        client.setServiceUri("access_token");
        Map<String,Object> paramsMap = new HashMap<String,Object>();
        paramsMap.put("appid", Contants.APPID);
        paramsMap.put("secret",Contants.SECRET);
        paramsMap.put("code",code);
        paramsMap.put("grant_type","authorization_code");
        client.setParams(paramsMap);
        String response = client.request();
        AccessTokenBean accessTokenBean = JSON.parseObject(response,new TypeReference<AccessTokenBean>(){});
        return accessTokenBean.getOpenid();
    }
    public String refreshAccessToken(String refreshAccessToken){
        HTTPSClient client = new HTTPSClient();
        client.setSERVER_HOST_URL(REFRESH_ACCESS_TOKEN_URL);
        client.setServiceUri("refresh_token");
        Map<String,Object> paramsMap = new HashMap<String,Object>();
        paramsMap.put("appid", Contants.APPID);
        paramsMap.put("grant_type","refresh_token");
        paramsMap.put("refresh_token",refreshAccessToken);
        client.setParams(paramsMap);
        String response = client.request();
        AccessTokenBean accessTokenBean = JSON.parseObject(response,new TypeReference<AccessTokenBean>(){});
        long currentTime = new Date().getTime();
        weixinOauthAccessTokenBean.setExpires_in(currentTime+accessTokenBean.getExpires_in()*1000);
        return accessTokenBean.getOpenid();
    }
    public AccessTokenBean getAccessToken(){
//        long currentTime = new Date().getTime();
//
//        if(tokenBean != null && tokenBean.getExpires_in() > currentTime){
//            return tokenBean;
//        }else{
//            return obtainOauthAccessToken("");
//        }
        return weixinOauthAccessTokenBean;
    }

    public WeixinUser getUserInfo(String accessToken,String openId){
        HTTPSClient client = new HTTPSClient();
        client.setSERVER_HOST_URL(USERINFOURL);
        client.setServiceUri("userinfo");
        Map<String,Object> paramsMap = new HashMap<String,Object>();
        paramsMap.put("access_token", accessToken);
        paramsMap.put("openid",openId);
        paramsMap.put("lang","zh_CN");
        client.setParams(paramsMap);
        String response = client.request();
        WeixinUser weixinUser = JSON.parseObject(response,new TypeReference<WeixinUser>(){});
        return weixinUser;
    }

    public static void main(String[] args) {
        WeixinOauth oauth = new WeixinOauth();
        AccessTokenBean accessTokenBean = oauth.obtainOauthAccessToken("021e55a213feae41a75eb312e8e8a58R");
        System.out.println(accessTokenBean.getErrcode());

    }





}
