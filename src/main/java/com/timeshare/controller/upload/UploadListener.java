package com.timeshare.controller.upload;

import org.apache.commons.fileupload.ProgressListener;

import javax.servlet.http.HttpSession;

/**
 * Created by user on 2016/6/24.
 */
public class UploadListener implements ProgressListener {

    private HttpSession session;

    public UploadListener(HttpSession session){
        this.session = session;
        ProgressEntity ps = new ProgressEntity();
        session.setAttribute("upload_ps", ps);
    }

    @Override
    public void update(long pBytesRead, long pContentLength, int pItems) {
        ProgressEntity ps = (ProgressEntity) session.getAttribute("upload_ps");
        ps.setpBytesRead(pBytesRead);
        ps.setpContentLength(pContentLength);
        ps.setpItems(pItems);
        //更新
        //System.out.println(ps.toString());
        session.setAttribute("upload_ps", ps);
    }
}
