package com.timeshare.domain.assembly;

import com.timeshare.domain.BaseDomain;

/**
 * Created by user on 2017/3/24.
 * 报名的人
 */
public class Attender extends BaseDomain {

    //用户id
    private String userId;
    //所属费用项
    private String feedId;

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
