package com.timeshare.controller;

import com.alibaba.fastjson.JSON;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.UserInfo;
import com.timeshare.service.UserService;
import com.timeshare.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by user on 2016/6/23.
 */
@Controller
@RequestMapping(value = "/wx")
public class WxController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/sanctus",method = RequestMethod.GET)
    public String sanctus(@RequestParam String signature, @RequestParam String timestamp,
                          @RequestParam String nonce, @RequestParam String echostr) {
        String result = "";

        String token = "SanctusII";
        // 1. 将token、timestamp、nonce三个参数进行字典序排序
        String[] strArr = new String[] { token, timestamp, nonce };
        java.util.Arrays.sort(strArr);
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuffer sb = new StringBuffer();
        for (String str : strArr){
            sb.append(str);
        }
        MessageDigest mdSha1 = null;
        try{
            mdSha1 = MessageDigest.getInstance("SHA-1");
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        mdSha1.update(sb.toString().getBytes());
        byte[] codedBytes = mdSha1.digest();
        String codedString = new BigInteger(1, codedBytes).toString(16);
        System.out.println(codedString);
        System.out.println(signature);
        // 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (codedString.equals(signature)){
            result = echostr;
        }
        return result;
    }
    @RequestMapping(value = "/to-get-open-id")
    public String toOauth(@RequestParam(value = "backUrl",required = false,defaultValue = "") String backUrl){
        String wxOauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                + Contants.APPID+"&redirect_uri=http%3A%2F%2F"+Contants.DOMAIN+"%2Ftime%2Fwx%2Fget-open-id%2F&response_type=code&scope=snsapi_base&state="+backUrl+"#wechat_redirect";
        return "redirect:"+wxOauthUrl;
    }
    @RequestMapping(value = "/get-open-id")
    public String getOpenId(HttpServletRequest request, HttpServletResponse response,Model model){

        String toUrl = request.getParameter("state");
        String code = request.getParameter("code");
        WeixinOauth oauth = new WeixinOauth();
        String openId = oauth.obtainOpenId(code);
        UserInfo userInfo = userService.findUserByOpenId(openId);
        String sendUrl = "";
        if(userInfo == null){
            sendUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + Contants.APPID+"&redirect_uri=http%3A%2F%2F"+Contants.DOMAIN+"%2Ftime%2Fwx%2Foauth%2F&response_type=code&scope=snsapi_userinfo&state="+toUrl+"#wechat_redirect";
        }else{

            String userId = CookieUtils.getCookie(request,"time_sid");
            if(StringUtils.isBlank(userId)){
                CookieUtils.setCookie(response,"time_sid",userInfo.getUserId(),60*60*24*7);
            }
            String mobile = userInfo.getMobile();
            String nickName = userInfo.getNickName();
            if(StringUtils.isNotBlank(mobile) && StringUtils.isNotBlank(nickName)){
                if(StringUtils.isNotBlank(toUrl)){
                    try {
                        sendUrl = URLDecoder.decode(toUrl,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else{
                    sendUrl = "/user/to-my-page";
                }

            }else{

                sendUrl = "/user/to-userinfo?userId="+userInfo.getUserId();
            }
        }
        return "redirect:"+sendUrl;
    }

//https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcd8903dd6a9552eb&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Ftime%2Fwx%2Foauth&response_type=code&scope=snsapi_userinfo&state=http%3A%2F%2Fwww.baidu.com#wechat_redirect
    @RequestMapping(value = "/oauth")
    public String oauth(HttpServletRequest request, HttpServletResponse response){
        String code = request.getParameter("code");
        String toUrl = request.getParameter("state");
        WeixinOauth weixinOauth = new WeixinOauth();
        AccessTokenBean accessTokenBean = weixinOauth.obtainOauthAccessToken(code);
        WeixinUser weixinUser = weixinOauth.getUserInfo(accessTokenBean.getAccess_token(), accessTokenBean.getOpenid());
        System.out.println(JSON.toJSONString(weixinUser));

        UserInfo user = new UserInfo();
        String userId = CommonStringUtils.genPK();
        if(weixinUser != null){
            user.setUserId(userId);
            user.setOpenId(weixinUser.getOpenId());
            user.setNickName(weixinUser.getNickname());
            user.setSex(weixinUser.getSex());
            user.setCity(weixinUser.getCity());
            ImageObj imageObj = new ImageObj();
            imageObj.setImageUrl(weixinUser.getHeadimgurl());
            user.setImageObj(imageObj);
            String result = userService.saveUser(user);
            if(result.equals(Contants.SUCCESS)){
                CookieUtils.setCookie(response,"time_sid",userId,60*60*24*7);
            }
        }

        request.setAttribute("user",user);
        String sendUrl = "";
        if(StringUtils.isNotBlank(toUrl)){
            try {
                sendUrl = URLDecoder.decode(toUrl,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            sendUrl = "/user/to-userinfo?userId="+userId;
        }
        return "redirect:"+sendUrl;
    }
}
