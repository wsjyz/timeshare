package com.timeshare.domain;

import com.timeshare.domain.annotation.Column;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adam on 2016/6/11.
 */
public class BaseDomain implements java.io.Serializable {

    @Column(name="create_user_id",length = 32,comment = "创建人")
    private String userId;
    @Column(name="create_user_name",length = 32,comment = "创建人姓名")
    private String createUserName;
    @Column(name="opt_time",length = 20,comment = "操作时间yyyy-MM-dd HH:mm:ss")
    String optTime;//操作时间

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOptTime() {
        if(optTime == null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            optTime = sdf.format(new Date());
        }
        return optTime;
    }

    public void setOptTime(String optTime) {
        this.optTime = optTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}
