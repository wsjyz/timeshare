package com.timeshare.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by user on 2016/10/9.
 */
public class VoiceFileUtils {

    protected static Logger logger = LoggerFactory.getLogger(VoiceFileUtils.class);

    public static void amrToMp3(String amrFilePath){

        String mp3FilePath = amrFilePath.substring(0,amrFilePath.lastIndexOf(".")) + ".mp3";

        Runtime run = null;
        Process p = null;
        try {

            run = Runtime.getRuntime();

            long start=System.currentTimeMillis();

            p = run.exec("ffmpeg -i "+amrFilePath+" -vol 300 "+mp3FilePath);//执行ffmpeg.exe,前面是ffmpeg.exe的地址，中间是需要转换的文件地址，后面是转换后的文件地址。-i是转换方式，意思是可编码解码，mp3编码方式采用的是libmp3lame
            logger.info("ffmpeg -i "+amrFilePath+" -vol 300 "+mp3FilePath);
            //释放进程
            InputStream stdout = p.getErrorStream ();
            BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
            String line;
            while ((line = reader.readLine ()) != null) {
                System.out.println ("Stdout: " + line);
            }
//            p.getOutputStream().close();
//
//            p.getInputStream().close();
//
//            p.getErrorStream().close();

            //p.waitFor();

            long end=System.currentTimeMillis();


        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //run调用lame解码器最后释放内存

            //run.freeMemory();
            try {
                p.getOutputStream().close();
                p.getInputStream().close();
                p.getErrorStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
