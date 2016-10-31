package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.BidSubmitDAO;
import com.timeshare.domain.BidSubmit;
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
@Repository("BidSubmitDAO")
public class BidSubmitDAOImpl extends BaseDAO implements BidSubmitDAO {
    @Override
    public String saveBidSubmit(BidSubmit submit) {
        StringBuilder sql = new StringBuilder("insert into t_bid_submit " +
                "(bid_submit_id,bid_submit_text,bid_submit_type,bid_id,wx_server_id,server_path,create_user_id,opt_time,create_user_name,last_modify_time,other_user_id)" +
                " values(?,?,?,?,?,?,?,?,?,?,?)");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,submit.getBidSubmitText());
                ps.setString(3,submit.getBidSubmitType());
                ps.setString(4,submit.getBidId());
                ps.setString(5,submit.getWxServerId());
                ps.setString(6,submit.getServerPath());
                ps.setString(7,submit.getUserId());
                ps.setString(8,submit.getOptTime());
                ps.setString(9,submit.getCreateUserName());
                ps.setString(10,submit.getLastModifyTime());
                ps.setString(11,submit.getOtherUserId());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public String deleteBidSubmit(String submitId) {
        StringBuilder sql = new StringBuilder("delete from t_bid_submit where bid_submit_id = ? ");
        int result = getJdbcTemplate().update(sql.toString(),new String[]{submitId});
        if(result > 0){
            return submitId;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public List<BidSubmit> findSubmitList(BidSubmit submit, int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select * from t_bid_submit i where 1=1 ");
        if (StringUtils.isNotEmpty(submit.getUserId())) {
            sql.append(" and (i.create_user_id = '"+submit.getUserId()+"' or (i.create_user_id = '"+submit.getBidCreateUser()+"' and i.other_user_id = '"+submit.getOtherUserId()+"')) ");
        }
        if (StringUtils.isNotEmpty(submit.getBidId())) {
            sql.append(" and i.bid_id = '"+submit.getBidId()+"' ");
        }
        sql.append("  order by i.opt_time ");
        if(startIndex != 0 && loadSize != 0){
            sql.append(" limit ?,?");
            return getJdbcTemplate().query(sql.toString(),new Object[]{startIndex,loadSize},new BidSubmitRowMapper());
        }else{
            return getJdbcTemplate().query(sql.toString(),new BidSubmitRowMapper());
        }

    }

    public BidSubmit findPreviouSubmit(BidSubmit submit){
        StringBuilder sql = new StringBuilder("select * from t_bid_submit i where 1=1 ");
        if (StringUtils.isNotBlank(submit.getBidId())) {
            sql.append(" and i.bid_id = '"+submit.getBidId()+"' ");
        }
        if (StringUtils.isNotBlank(submit.getUserId())) {
            sql.append(" and i.create_user_id = '"+submit.getUserId()+"'");
        }
        if (StringUtils.isNotBlank(submit.getOtherUserId())) {
            sql.append(" and i.other_user_id = '"+submit.getOtherUserId()+"'");
        }
        sql.append("  order by i.opt_time desc limit 0,1");
        List<BidSubmit> bidSubmits = getJdbcTemplate().query(sql.toString(),new BidSubmitRowMapper());
        if(bidSubmits != null && bidSubmits.size() > 0){
            return bidSubmits.get(0);
        }
        return null;
    }

    class BidSubmitRowMapper implements RowMapper<BidSubmit>{

        @Override
        public BidSubmit mapRow(ResultSet rs, int i) throws SQLException {
            BidSubmit submit = new BidSubmit();
            submit.setBidSubmitId(rs.getString("bid_submit_id"));
            submit.setBidSubmitText(rs.getString("bid_submit_text"));
            submit.setBidSubmitType(rs.getString("bid_submit_type"));
            submit.setBidId(rs.getString("bid_id"));
            submit.setWxServerId(rs.getString("wx_server_id"));
            submit.setServerPath(rs.getString("server_path"));
            submit.setCreateUserName(rs.getString("create_user_name"));
            submit.setUserId(rs.getString("create_user_id"));
            submit.setOptTime(rs.getString("opt_time"));
            submit.setLastModifyTime(rs.getString("last_modify_time"));
            submit.setOtherUserId(rs.getString("other_user_id"));
            return submit;
        }
    }

}
