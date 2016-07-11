package com.timeshare.controller.upload;

import com.timeshare.domain.ImageObj;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.ImgUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @ResponseBody
    @RequestMapping(value = "upload-img")
    public UploadResult uploadImg(MultipartHttpServletRequest request){
        UploadResult result = new UploadResult();
        MultipartFile file = request.getFile("inputFile");
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
        int day = cal.get(Calendar.DATE);
        String fileName = name + ".jpg";
        path += "/" + month + "/" + day;
        File targetFile = new File(path, fileName);

        try {
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);

            //压缩裁剪图片
            ImgUtils.cropImageCenter(targetFile.getAbsolutePath(),path+"/"+name+"_320x240.jpg",320,240);

            result.setSuccess(true);
            result.setThumbUrl(request.getContextPath()+"/images/" + month + "/" + day + "/" +name+"_320x240.jpg");
        } catch (IOException e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }
    public class UploadResult{
        private boolean success;
        private String thumbUrl;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }
    }
}
