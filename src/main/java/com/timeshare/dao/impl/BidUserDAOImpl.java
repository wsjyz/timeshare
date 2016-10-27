package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.BidUserDAO;
import com.timeshare.domain.BidUser;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public BidUser findBidUserByBidIdAndUserId(String bidId,String userId){
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_bid_user where bid_id = ? and create_user_id = ?");

        List<BidUser> bidUserList = getJdbcTemplate().query(sql.toString(),new Object[]{bidId,userId},new BidUserMapper());
        if(bidUserList != null && !bidUserList.isEmpty()){
            return bidUserList.get(0);
        }
        return null;
    }

    @Override
    public String modifyBidUser(BidUser bidUser) {

        StringBuilder sql = new StringBuilder("update t_bid_user set ");

        if(StringUtils.isNotBlank(bidUser.getWinTheBid())){
            sql.append(" win_the_bid = '"+bidUser.getWinTheBid()+"',");
        }
        if(bidUser.getIncomeFee() != null){
            sql.append(" income_fee = "+bidUser.getIncomeFee()+",");
        }
        if(bidUser.getRating() != 0){
            sql.append(" rating = "+bidUser.getRating()+",");
        }
        if(StringUtils.isNotBlank(bidUser.getLastModifyTime())){
            sql.append(" last_modify_time = '"+bidUser.getLastModifyTime()+"',");
        }
        if(StringUtils.isNotBlank(bidUser.getWxTradeNo())){
            sql.append(" wx_trade_no = '"+bidUser.getWxTradeNo()+"',");
        }
        if (sql.lastIndexOf(",") + 1 == sql.length()) {
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
        sql.append(" where bid_user_id='" + bidUser.getBidUserId() + "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return bidUser.getBidUserId();
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public List<BidUser> findBidUserList(BidUser bidUser, int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_bid_user i where 1=1 ");
        if(bidUser != null){
            if (StringUtils.isNotBlank(bidUser.getUserId())) {
                sql.append(" and i.create_user_id ='"+bidUser.getUserId()+"' ");
            }
            if (StringUtils.isNotBlank(bidUser.getBidId())) {
                sql.append(" and i.bid_id ='"+bidUser.getBidId()+"' ");
            }
            if (StringUtils.isNotBlank(bidUser.getWinTheBid())) {
                sql.append(" and i.win_the_bid ='"+bidUser.getWinTheBid()+"' ");
            }
        }
        sql.append("  and i.create_user_id != '"+bidUser.getCurrentUserId()+"'");
        sql.append("  order by i.last_modify_time desc limit ?,?");
        List<BidUser> bidUserList = getJdbcTemplate().query(sql.toString(),new Object[]{startIndex,loadSize},new BidUserMapper());

        return bidUserList;
    }

    class BidUserMapper implements RowMapper<BidUser>{

        @Override
        public BidUser mapRow(ResultSet rs, int i) throws SQLException {
            BidUser bidUser = new BidUser();
            bidUser.setBidUserId(rs.getString("bid_user_id"));
            bidUser.setBidId(rs.getString("bid_id"));
            bidUser.setWinTheBid(rs.getString("win_the_bid"));
            bidUser.setIncomeFee(rs.getBigDecimal("income_fee"));
            bidUser.setCreateUserName(rs.getString("create_user_name"));
            bidUser.setUserId(rs.getString("create_user_id"));
            bidUser.setRating(rs.getInt("rating"));
            bidUser.setOptTime(rs.getString("opt_time"));
            bidUser.setLastModifyTime(rs.getString("last_modify_time"));
            bidUser.setWxTradeNo(rs.getString("wx_trade_no"));
            return bidUser;
        }
    }
}
