package com.timeshare.controller.upload;

import com.timeshare.utils.CommonStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2016/6/24.
 */
@Controller
@RequestMapping(value = "/upload")
public class UploadController {

    @RequestMapping(value = "upload-img")
    public String uploadImg(MultipartHttpServletRequest request){
        MultipartFile file = request.getFile("file");
        if(file == null){
            System.out.println("no file");
            return null;
        }
        String data = "";
        String name = CommonStringUtils.genPK();
        String path = request.getSession().getServletContext()
                .getRealPath("/WEB-INF/images/" );
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date time = cal.getTime();
        int month = cal.get(Calendar.MONTH) + 1;
        String fileName = name + ".jpg";
        path += "/" + month;
        File targetFile = new File(path, fileName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
