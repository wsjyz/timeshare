package com.timeshare.dao.impl;

import com.timeshare.dao.AuditorDAO;
import com.timeshare.dao.BaseDAO;
import com.timeshare.domain.Auditor;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
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
@Repository("AuditorDAO")
public class AuditorDAOImpl extends BaseDAO implements AuditorDAO {

    @Override
    public String saveAuditor(Auditor auditor) {

        StringBuilder sql = new StringBuilder("insert into t_auditor " +
                "(auditor_id,bid_id,fee,create_user_id,opt_time,create_user_name,last_modify_time,wx_trade_no)" +
                " values(?,?,?,?,?,?,?,?)");

        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,auditor.getBidId());
                ps.setInt(3,auditor.getFee());
                ps.setString(4,auditor.getUserId());
                ps.setString(5,auditor.getOptTime());
                ps.setString(6,auditor.getCreateUserName());
                ps.setString(7,auditor.getLastModifyTime());
                ps.setString(8,auditor.getWxTradeNo());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public Auditor findAuditorByBidIdAndUserId(String bidId, String userId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_auditor where bid_id = ? and create_user_id = ?");

        List<Auditor> auditorList = getJdbcTemplate().query(sql.toString(),new Object[]{bidId,userId},new AuditorRowMapper());
        if(auditorList != null && !auditorList.isEmpty()){
            return auditorList.get(0);
        }
        return null;
    }

    @Override
    public List<Auditor> findAuditorList(Auditor auditor, int startIndex, int loadSize) {

        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_auditor i where 1=1 ");
        if(auditor != null){
            if (StringUtils.isNotBlank(auditor.getUserId())) {
                sql.append(" and i.create_user_id ='"+auditor.getUserId()+"' ");
            }
        }
        sql.append(" order by i.opt_time desc limit ?,?");
        List<Auditor> bidUserList = getJdbcTemplate().query(sql.toString(),new Object[]{startIndex,loadSize},new AuditorRowMapper());

        return bidUserList;
    }

    class AuditorRowMapper implements RowMapper<Auditor>{

        @Override
        public Auditor mapRow(ResultSet rs, int i) throws SQLException {
            Auditor auditor = new Auditor();
            auditor.setAuditorId(rs.getString("auditor_id"));
            auditor.setCreateUserName(rs.getString("create_user_name"));
            auditor.setUserId(rs.getString("create_user_id"));
            auditor.setBidId(rs.getString("bid_id"));
            auditor.setFee(rs.getInt("fee"));
            auditor.setOptTime(rs.getString("opt_time"));
            auditor.setLastModifyTime(rs.getString("last_modify_time"));
            auditor.setWxTradeNo(rs.getString("wx_trade_no"));
            return auditor;
        }
    }
}
