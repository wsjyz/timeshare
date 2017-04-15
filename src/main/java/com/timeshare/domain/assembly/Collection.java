package com.timeshare.domain.assembly;

import com.timeshare.domain.BaseDomain;

/**
 * Created by user on 2017/3/24.
 * 报名的人
 */
public class Collection extends BaseDomain {

    private String collectionId;
    //用户id
    private String userId;
    //所属费用项
    private String assemblyId;
    private String createTime;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
