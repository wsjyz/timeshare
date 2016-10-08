package com.timeshare.utils;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/9/20.
 */
public class OkhttpClient {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final MediaType HTML
            = MediaType.parse("text/html; charset=utf-8");

    public static final String UserAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.9.2.1000 Chrome/39.0.2146.0 Safari/537.36";

    OkHttpClient client = new OkHttpClient();

    private RequestBody packageFormBody(Map<String,Object> formParams){
        FormBody.Builder builder = new FormBody.Builder();

        if (null != formParams) {
            for (Map.Entry<String, Object> entry : formParams.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = "
                        + entry.getValue());
                builder.add(entry.getKey(), entry.getValue().toString());

            }
        }
        RequestBody body = builder.build();
        return body;
    }

    private String request(String url,RequestBody body){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent",UserAgent)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String responseResult = null;
        try {
            responseResult = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    public String post(String url, String bodyParams){
        return post(url,bodyParams,HTML);
    }

    public String post(String url, String bodyParams,MediaType requestContentType){

        RequestBody body = RequestBody.create(requestContentType, bodyParams);

        return request(url,body);
    }

    public String post(String url, Map<String,Object> params)  {

        RequestBody body = packageFormBody(params);

        return request(url,body);

    }

    public void asyncDownloadFile(String fileUrl,String savePath,HttpClientCallback callback){

        Request request = new Request.Builder().addHeader("User-Agent",UserAgent).url(fileUrl).build();

        Call dwCall = client.newCall(request);
        dwCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    String contentDisposition = response.header("Content-disposition");
                    FileBean fileBean = new FileBean();
                    String fileName = "";
                    if(StringUtils.isNotBlank(contentDisposition) && contentDisposition.indexOf(".") != -1){
                        fileName = contentDisposition.substring(contentDisposition.lastIndexOf("filename=\"") + 10,contentDisposition.lastIndexOf("\""));
                        fileBean.setFileName(fileName);
                    }
                    File parentPath = new File(savePath);
                    if(!parentPath.exists()){
                        parentPath.mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(new File(savePath+fileName));
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();

                    callback.call(fileBean);

                } catch (IOException e) {

                    e.printStackTrace();
                }finally {
                    try {
                        inputStream.close();
                        response.body().close();
                        if(fileOutputStream != null){
                            fileOutputStream.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }
        });

    }

    public class FileBean{
        private String fileName;
        private long fileSize;
        private String contentType;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public long getFileSize() {
            return fileSize;
        }

        public void setFileSize(long fileSize) {
            this.fileSize = fileSize;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
    }




}
