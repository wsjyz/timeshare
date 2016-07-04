package com.timeshare.service.impl;

import com.timeshare.dao.RemindDAO;
import com.timeshare.domain.Remind;
import com.timeshare.service.RemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2016/7/4.
 */
@Service("RemindService")
public class RemindServiceImpl implements RemindService {

    @Autowired
    RemindDAO remindDAO;

    @Override
    public String saveRemind(Remind remind) {
        return remindDAO.saveRemind(remind);
    }

    @Override
    public int deleteRemind(String remindId) {
        return remindDAO.deleteRemind(remindId);
    }

    @Override
    public int queryCountByObjIdAndType(String toUserId, String objId, String type) {
        return remindDAO.queryCountByObjIdAndType(toUserId,objId,type);
    }
}
