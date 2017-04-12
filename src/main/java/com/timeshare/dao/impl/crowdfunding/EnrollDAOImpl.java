package com.timeshare.dao.impl.crowdfunding;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.crowdfunding.CrowdFundingDAO;
import com.timeshare.dao.crowdfunding.EnrollDAO;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Repository("EnrollDAO")
public class EnrollDAOImpl extends BaseDAO implements EnrollDAO {
    @Override
    public String saveEnroll(Enroll enroll) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_enroll (enroll_id, crowdfunding_id, enroll_user_id, user_name, phone, corp_name, invoice_title, invoice_type,pay_status,pay_amount,user_id,opt_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?);");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,enroll.getCrowdfundingId());
                ps.setString(3,enroll.getEnrollUserId());
                ps.setString(4,enroll.getUserName());
                ps.setString(5,enroll.getPhone());
                ps.setString(6,enroll.getCorpName());
                ps.setString(7,enroll.getInvoiceTitle());
                ps.setString(8,enroll.getInvoiceType());
                ps.setString(9,enroll.getPayStatus());
                ps.setBigDecimal(10,enroll.getPayAmount());
                ps.setString(11,enroll.getUserId());
                ps.setString(12,enroll.getOptTime());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public List<Enroll> findEnrollByOwner(String userId,int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select * from t_enroll where user_id='"+userId+"' limit "+startIndex+","+loadSize);
        return getJdbcTemplate().query(sql.toString(),new Object[]{},new EnrollRowMapper());
    }


    public int findEnrollByOwnerCount(String userId) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_enroll where user_id="+userId);
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
    }



    class EnrollRowMapper implements RowMapper<Enroll>{
        @Override
        public Enroll mapRow(ResultSet rs, int i) throws SQLException {
            Enroll enroll = new Enroll();
            enroll.setEnrollId(rs.getString("enroll_id"));
            enroll.setCrowdfundingId(rs.getString("crowdfunding_id"));
            enroll.setEnrollUserId(rs.getString("enroll_user_id"));
            enroll.setUserName(rs.getString("user_name"));
            enroll.setPhone(rs.getString("phone"));
            enroll.setCorpName(rs.getString("corp_name"));
            enroll.setInvoiceTitle(rs.getString("invoice_title"));
            enroll.setInvoiceType(rs.getString("invoice_type"));
            enroll.setPayStatus(rs.getString("pay_status"));
            enroll.setPayAmount(rs.getBigDecimal("pay_amount"));
            enroll.setUserId(rs.getString("user_id"));
            enroll.setOptTime(rs.getString("opt_time"));
            return enroll;
        }
    }
}