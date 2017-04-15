package com.timeshare.dao.impl.crowdfunding;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.crowdfunding.WithdrawalLogDAO;
import com.timeshare.domain.crowdfunding.WithdrawalLog;
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
@Repository("WithdrawalLogDAO")
public class WithdrawalLogDAOImpl extends BaseDAO implements WithdrawalLogDAO {
    @Override
    public String saveWithdrawalLog(WithdrawalLog withdrawalLog) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_withdrawal_log (withdrawal_log_id, withdrawal_cash, withdrawal_status, withdrawal_time, reply_msg, user_id,withdrawal_trade_no) VALUES (?, ?, ?, ?, ?, ?,?);");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setBigDecimal(2,withdrawalLog.getWithdrawalCash());
                ps.setString(3,withdrawalLog.getWithdrawalStatus());
                ps.setString(4,withdrawalLog.getWithdrawalTime());
                ps.setString(5,withdrawalLog.getReplyMsg());
                ps.setString(6,withdrawalLog.getUserId());
                ps.setString(7,withdrawalLog.getWithdrawalTradeNo());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public List<WithdrawalLog> findWithdrawalLogByOwner(String userId,int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select * from t_withdrawal_log where user_id='"+userId+"' limit "+startIndex+","+loadSize);
        return getJdbcTemplate().query(sql.toString(),new Object[]{},new WithdrawalLogRowMapper());
    }


    public int findWithdrawalLogByOwnerCount(String userId) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_withdrawal_log where user_id="+userId);
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
    }



    class WithdrawalLogRowMapper implements RowMapper<WithdrawalLog>{
        @Override
        public WithdrawalLog mapRow(ResultSet rs, int i) throws SQLException {
            WithdrawalLog WithdrawalLog = new WithdrawalLog();
            WithdrawalLog.setWithdrawalLogId(rs.getString("withdrawal_log_id"));
            WithdrawalLog.setWithdrawalCash(rs.getBigDecimal("withdrawal_cash"));
            WithdrawalLog.setWithdrawalStatus(rs.getString("withdrawal_status"));
            WithdrawalLog.setWithdrawalTime(rs.getString("withdrawal_time"));
            WithdrawalLog.setReplyMsg(rs.getString("reply_msg"));
            WithdrawalLog.setUserId(rs.getString("user_id"));
            WithdrawalLog.setWithdrawalTradeNo(rs.getString("withdrawal_trade_no"));
            return WithdrawalLog;
        }
    }
}