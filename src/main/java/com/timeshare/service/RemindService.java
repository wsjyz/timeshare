package com.timeshare.service;

import com.timeshare.domain.Remind;

/**
 * Created by user on 2016/7/4.
 */
public interface RemindService {

    String saveRemind(Remind remind);

    int deleteRemind(String remindId);

    int queryCountByObjIdAndType(String toUserId,String objId,String type);

}
