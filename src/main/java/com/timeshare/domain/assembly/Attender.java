package com.timeshare.domain.assembly;

import com.timeshare.domain.BaseDomain;

/**
 * Created by user on 2017/3/24.
 * 报名的人
 */
public class Attender extends BaseDomain {

    private String attenderId;
    //用户id
    private String userId;
    //所属费用项
    private String feedId;
    private String assemblyId;

    public void setAttenderId(String attenderId) {
        this.attenderId = attenderId;
    }

    public String getAttenderId() {
        return attenderId;
    }

    public String getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }
}
