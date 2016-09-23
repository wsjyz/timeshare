package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.BidUserDAO;
import com.timeshare.domain.BidUser;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by user on 2016/9/23.
 */
@Repository("BidUserDAO")
public class BidUserDAOImpl extends BaseDAO implements BidUserDAO {

    @Override
    public String saveBidUser(BidUser bidUser) {

        StringBuilder sql = new StringBuilder("insert into t_bid_user " +
                "(bid_user_id,bid_id,win_the_bid,income_fee,create_user_id,opt_time,create_user_name,last_modify_time)" +
                " values(?,?,?,?,?,?,?,?)");

        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,bidUser.getBidId());
                ps.setString(3,bidUser.getWinTheBid());
                ps.setBigDecimal(4,bidUser.getIncomeFee());
                ps.setString(5,bidUser.getUserId());
                ps.setString(6,bidUser.getOptTime());
                ps.setString(7,bidUser.getCreateUserName());
                ps.setString(8,bidUser.getLastModifyTime());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }
}
