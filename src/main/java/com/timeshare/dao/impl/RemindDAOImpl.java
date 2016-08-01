package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.RemindDAO;
import com.timeshare.domain.Remind;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by user on 2016/7/4.
 */
@Repository("RemindDAO")
public class RemindDAOImpl extends BaseDAO implements RemindDAO {
    @Override
    public String saveRemind(final Remind remind) {
        StringBuilder sql = new StringBuilder("insert into t_remind (remind_id,obj_id,remind_type,to_user_id, create_user_id,opt_time)" +
                " values(?,?,?,?,?,?)");
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, CommonStringUtils.genPK());
                ps.setString(2,remind.getObjId());
                ps.setString(3,remind.getRemindType());
                ps.setString(4,remind.getToUserId());
                ps.setString(5,remind.getUserId());
                ps.setString(6,remind.getOptTime());

            }
        });
        if(result > 0){
            return Contants.SUCCESS;
        }else{
            return "FAILED";
        }
    }

    @Override
    public int deleteRemind(String remindId) {
        StringBuilder reviewSql = new StringBuilder("");
        reviewSql.append("delete from t_remind where remind_id = ? ");
        return getJdbcTemplate().update(reviewSql.toString(),new Object[]{remindId});
    }
    @Override
    public int deleteRemindByObjIdAndUserId(String objId,String userId) {
        StringBuilder reviewSql = new StringBuilder("");
        reviewSql.append("delete from t_remind where obj_id = ? and to_user_id = ?");
        return getJdbcTemplate().update(reviewSql.toString(),new Object[]{objId,userId});
    }

    @Override
    public int queryCountByObjIdAndType(String toUserId,String objId, String type) {
        StringBuilder countSql = new StringBuilder("");
        countSql.append("select count(*) from t_remind where 1=1");
        if (StringUtils.isNotEmpty(toUserId)) {
            countSql.append(" and to_user_id = '"+toUserId+"' ");
        }
        if (StringUtils.isNotEmpty(objId)) {
            countSql.append("  and obj_id = '"+objId+"' ");
        }
        if (StringUtils.isNotEmpty(type)) {
            countSql.append("  and remind_type = '"+type+"' ");
        }
        Integer count = getJdbcTemplate().queryForObject(countSql.toString(),Integer.class);
        return count;
    }
}
