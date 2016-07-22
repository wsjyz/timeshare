package com.timeshare.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dam on 2014/6/26.
 */
public class HTTPSClient {


    private String SERVER_HOST_URL = "";
    //参数
    private Map<String,Object> params = new HashMap<String, Object>();
    //参数名和方法名
    private String serviceUri;
    //为了适应非form请求
    private Object bodyParams;
    //设置content-type
    private String contentType;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Object getBodyParams() {
        return bodyParams;
    }

    public void setBodyParams(Object bodyParams) {
        this.bodyParams = bodyParams;
    }

    public void setServiceUri(String serviceUri) {
        this.serviceUri = serviceUri;
    }

    public String getSERVER_HOST_URL() {
        return SERVER_HOST_URL;
    }

    public void setSERVER_HOST_URL(String SERVER_HOST_URL) {
        this.SERVER_HOST_URL = SERVER_HOST_URL;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getServiceUri() {
        return serviceUri;
    }

    public UrlEncodedFormEntity packageParams(){
        ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        if(params.size() > 0){
            for(String key:params.keySet()){
                Object paramsValueObj = params.get(key);
                String paramsValues = "";
                if(paramsValueObj instanceof String){
                    paramsValues = (String)paramsValueObj;
                }else{
                    paramsValues = JSON.toJSONString(params.get(key));
                }
                System.out.println(key+"|"+paramsValues);
                BasicNameValuePair nameValue = new BasicNameValuePair(key,paramsValues);
                list.add(nameValue);
            }
        }
        UrlEncodedFormEntity urlencodedformentity = null;
        try {
            urlencodedformentity = new UrlEncodedFormEntity(list, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlencodedformentity;
    }
    public StringEntity packageStringParams(){
        StringEntity  postingString = null;
        try {
            String jsonBodyParamsStr = (String) bodyParams;
            if(bodyParams instanceof String == false){
                jsonBodyParamsStr = JSON.toJSONString(bodyParams);
            }
            postingString =new StringEntity(jsonBodyParamsStr);
            System.out.println("body|"+JSON.toJSONString(bodyParams));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return postingString;
    }
    public String request(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String postUri = SERVER_HOST_URL+getServiceUri();
        System.out.println("request uri:"+postUri);
        HttpPost httpPost = new HttpPost(postUri);

        if(params.size() > 0){
            httpPost.setEntity(packageParams());
        }
        if(bodyParams != null){
            httpPost.setEntity(packageStringParams());
        }
        if(StringUtils.isNotBlank(contentType)){
            httpPost.setHeader("Content-type", contentType);
        }


        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new RemoteInvokeException(EntityUtils.toString(response.getEntity()));
                }
            }

        };
        String responseBody = "";
        try {
            responseBody = new String(httpClient.execute(httpPost,responseHandler).getBytes("ISO-8859-1"),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseBody;
    }

    public String httpsRequest(String p12Path,String password,String postUri){

        KeyStore keyStore  = null;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        File p12File = new File(p12Path);
        if(!p12File.exists()){
            return "ERROR:证书路径错误,"+p12Path;
        }
        FileInputStream instream = null;
        try {
            instream = new FileInputStream(p12File);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            keyStore.load(instream, password.toCharArray());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }finally {
            if(instream != null){
                try {
                    instream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // Trust own CA and all self-signed certs
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, password.toCharArray())
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();


        HttpPost httpPost = new HttpPost(postUri);

        if(params.size() > 0){
            httpPost.setEntity(packageParams());
        }
        if(bodyParams != null){
            httpPost.setEntity(packageStringParams());
        }
        if(StringUtils.isNotBlank(contentType)){
            httpPost.setHeader("Content-type", contentType);
        }


        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    System.out.println("ERROR:微信API调用异常："+EntityUtils.toString(response.getEntity()));
                    return "ERROR:微信API调用异常";
                }
            }

        };
        String responseBody = "";
        try {
            //responseBody = new String(httpClient.execute(httpPost,responseHandler).getBytes("ISO-8859-1"),"UTF-8");
            responseBody = httpClient.execute(httpPost,responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseBody;

    }

    public void testjson(){
        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpPost request = new HttpPost("https://api.weixin.qq.com/sns/oauth2/access_token?code=0016f7281ab84ea6fee344f3d689fb2S&appid=wx805e0d1e1ff4c357&secret=a172670e10ca24f85c3f2aa024d8dd99&grant_type=authorization_code");
           // StringEntity params =new StringEntity("{\"action_info\":{\"scene\":{\"scene_id\":\"1\"}},\"action_name\":\"QR_LIMIT_SCENE\"}");
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            //request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    public static void main(String[] args) {
        HTTPSClient client = new HTTPSClient();
        client.testjson();
    }
}
