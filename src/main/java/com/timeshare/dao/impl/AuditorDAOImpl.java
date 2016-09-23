package com.timeshare.dao.impl;

import com.timeshare.dao.AuditorDAO;
import com.timeshare.dao.BaseDAO;
import com.timeshare.domain.Auditor;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by user on 2016/9/23.
 */
@Repository("AuditorDAO")
public class AuditorDAOImpl extends BaseDAO implements AuditorDAO {

    @Override
    public String saveAuditor(Auditor auditor) {

        StringBuilder sql = new StringBuilder("insert into t_auditor " +
                "(auditor_id,bid_id,fee,create_user_id,opt_time,create_user_name,last_modify_time)" +
                " values(?,?,?,?,?,?,?)");

        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,auditor.getBidId());
                ps.setBigDecimal(3,auditor.getFee());
                ps.setString(4,auditor.getUserId());
                ps.setString(5,auditor.getOptTime());
                ps.setString(6,auditor.getCreateUserName());
                ps.setString(7,auditor.getLastModifyTime());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }
}
