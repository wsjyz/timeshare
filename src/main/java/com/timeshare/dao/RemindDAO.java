package com.timeshare.dao;

import com.timeshare.domain.Remind;

/**
 * Created by user on 2016/7/4.
 */
public interface RemindDAO {

    String saveRemind(Remind remind);

    int deleteRemind(String remindId);

    int deleteRemindByObjIdAndUserId(String objId,String userId);

    int queryCountByObjIdAndType(String toUserId,String objId,String type);

}
