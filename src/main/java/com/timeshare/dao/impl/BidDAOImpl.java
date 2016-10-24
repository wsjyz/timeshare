package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.BidDAO;
import com.timeshare.domain.Bid;
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
@Repository("BidDAO")
public class BidDAOImpl extends BaseDAO implements BidDAO {

    @Override
    public String saveBid(Bid bid) {
        StringBuilder sql = new StringBuilder("insert into t_bid " +
                "(bid_id,title,content,price,end_time,bid_status,can_audit,click_rate,submit_count,create_user_id,opt_time,create_user_name,last_modify_time,bid_catalog,stop_reason)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,bid.getTitle());
                ps.setString(3,bid.getContent());
                ps.setBigDecimal(4,bid.getPrice());
                ps.setString(5,bid.getEndTime());
                ps.setString(6,bid.getBidStatus());
                ps.setString(7,bid.getCanAudit());
                ps.setInt(8,bid.getClickRate());
                ps.setInt(9,bid.getSubmitCount());
                ps.setString(10,bid.getUserId());
                ps.setString(11,bid.getOptTime());
                ps.setString(12,bid.getCreateUserName());
                ps.setString(13,bid.getLastModifyTime());
                ps.setString(14,bid.getBidCatalog());
                ps.setString(15,bid.getStopReason());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public String modifyBid(Bid bid) {

        StringBuilder sql = new StringBuilder("update t_bid set ");

        if(StringUtils.isNotBlank(bid.getTitle())){
            sql.append(" title = '"+bid.getTitle()+"',");
        }
        if(bid.getPrice() != null){
            sql.append(" price = "+bid.getPrice()+",");
        }
        if(bid.getScore() != null ){
            sql.append(" score = "+bid.getScore()+",");
        }
        if(StringUtils.isNotBlank(bid.getContent())){
            sql.append(" content = '"+bid.getContent()+"',");
        }
        if(StringUtils.isNotBlank(bid.getBidStatus())){
            sql.append(" bid_status = '"+bid.getBidStatus()+"',");
        }
        if(StringUtils.isNotBlank(bid.getCanAudit())){
            sql.append(" can_audit = '"+bid.getCanAudit()+"',");
        }
        if(StringUtils.isNotBlank(bid.getBidCatalog())){
            sql.append(" bid_catalog = '"+bid.getBidCatalog()+"',");
        }
        if(StringUtils.isNotBlank(bid.getStopReason())){
            sql.append(" stop_reason = '"+bid.getStopReason()+"',");
        }
        if(bid.getClickRate() != 0){
            sql.append(" click_rate = "+bid.getClickRate()+",");
        }
        if(bid.getSubmitCount() != 0){
            sql.append(" submit_count = "+bid.getSubmitCount()+",");
        }
        if (sql.lastIndexOf(",") + 1 == sql.length()) {
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
        sql.append(" where bid_id='" + bid.getBidId() + "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return bid.getBidId();
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public Bid findBidById(String bidId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_bid where bid_id = ?");
        if(StringUtils.isNotBlank(bidId)){

            List<Bid> bidList = getJdbcTemplate().query(sql.toString(),new Object[]{bidId},new BidRowMapper());
            if(bidList != null && bidList.size() > 0){
                Bid bid = bidList.get(0);
                return bid;
            }
        }
        return null;
    }

    @Override
    public List<Bid> findBidList(Bid bid, int startIndex, int loadSize) {
        StringBuilder sql = null;
        if(StringUtils.isNotBlank(bid.getPageContentType())
                && bid.getPageContentType().equals(Contants.PAGE_CONTENT_TYPE.mybid.toString())){
            sql = new StringBuilder("select * from t_bid i where 1=1 ");
        }else if(StringUtils.isNotBlank(bid.getPageContentType())
                && bid.getPageContentType().equals(Contants.PAGE_CONTENT_TYPE.mysubmit.toString())){
            sql = new StringBuilder("select i.* from t_bid i ,t_bid_user b where i.bid_id = b.bid_id ");
        }else if(StringUtils.isNotBlank(bid.getPageContentType())
                && bid.getPageContentType().equals(Contants.PAGE_CONTENT_TYPE.myaudit.toString())){
            sql = new StringBuilder("select i.* from t_bid i,t_auditor a where i.bid_id = a.bid_id ");
        }else{
            sql = new StringBuilder("select * from t_bid i where 1=1 ");//飚首页
        }
        if (StringUtils.isNotEmpty(bid.getBidStatus())) {
            sql.append(" and i.bid_status = '"+bid.getBidStatus()+"' ");
        }
        if (StringUtils.isNotEmpty(bid.getCanAudit())) {
            sql.append(" and i.can_audit = '"+bid.getCanAudit()+"' ");
        }
        if(StringUtils.isNotBlank(bid.getUserId())){
            sql.append(" and i.create_user_id = '"+bid.getUserId()+"' ");
        }
        if(StringUtils.isNotBlank(bid.getPageContentType())
                && bid.getPageContentType().equals(Contants.PAGE_CONTENT_TYPE.mysubmit.toString())
                && StringUtils.isNotBlank(bid.getBidUserId())){
            sql.append(" and b.create_user_id = '"+bid.getBidUserId()+"'");
        }
        if(StringUtils.isNotBlank(bid.getPageContentType())
                && bid.getPageContentType().equals(Contants.PAGE_CONTENT_TYPE.myaudit.toString())
                && StringUtils.isNotBlank(bid.getAuditUserId())){
            sql.append(" and a.create_user_id = '"+bid.getAuditUserId()+"'");
        }
        if(StringUtils.isBlank(bid.getPageContentType())){
            sql.append(" and i.end_time >= CURRENT_TIMESTAMP() ");
        }

//        if(StringUtils.isBlank(bid.getBidStatus())){
//            sql.append(" and i.bid_status = '"+Contants.BID_STATUS.ongoing.toString()+"' ");
//        }
        sql.append("  order by i.opt_time desc limit ?,?");
        return getJdbcTemplate().query(sql.toString(),new Object[]{startIndex,loadSize},new BidRowMapper());
    }

    @Override
    public List<Bid> searchBidList(Bid bid, int startIndex, int loadSize) {
        StringBuilder sql =  new StringBuilder("select * from t_bid i where 1=1 ");//飚首页

        sql.append(" and (i.bid_status = '"+Contants.BID_STATUS.finish+"' or i.bid_status = '"+Contants.BID_STATUS.ongoing+"')");
        if (StringUtils.isNotEmpty(bid.getCanAudit())) {
            sql.append(" and i.can_audit = '"+bid.getCanAudit()+"' ");
        }
        if(StringUtils.isNotBlank(bid.getUserId())){
            sql.append(" and i.create_user_id = '"+bid.getUserId()+"' ");
        }
        sql.append(" and (i.title like '%"+bid.getTitle()+"%' or i.create_user_name like '%"+bid.getCreateUserName()+"%')");

        sql.append(" and i.end_time >= CURRENT_TIMESTAMP() ");

        sql.append("  order by i.opt_time desc limit ?,?");
        return getJdbcTemplate().query(sql.toString(),new Object[]{startIndex,loadSize},new BidRowMapper());
    }

    @Override
    public int findBidCount(Bid bid) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_bid where 1=1 ");
        if (StringUtils.isNotEmpty(bid.getBidStatus())) {
            countSql.append(" and bid_status = '"+bid.getBidStatus()+"' ");
        }
        if (StringUtils.isNotEmpty(bid.getCanAudit())) {
            countSql.append(" and i.can_audit = '"+bid.getCanAudit()+"' ");
        }
        if(StringUtils.isNotBlank(bid.getUserId())){
            countSql.append(" and i.create_user_id = '"+bid.getUserId()+"' ");
        }
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
    }

    class BidRowMapper implements RowMapper<Bid>{

        @Override
        public Bid mapRow(ResultSet rs, int i) throws SQLException {
            Bid bid = new Bid();
            bid.setBidId(rs.getString("bid_id"));
            bid.setTitle(rs.getString("title"));
            bid.setContent(rs.getString("content"));
            bid.setPrice(rs.getBigDecimal("price"));
            bid.setEndTime(rs.getString("end_time"));
            bid.setBidStatus(rs.getString("bid_status"));
            bid.setCanAudit(rs.getString("can_audit"));
            bid.setClickRate(rs.getInt("click_rate"));
            bid.setSubmitCount(rs.getInt("submit_count"));
            bid.setCreateUserName(rs.getString("create_user_name"));
            bid.setUserId(rs.getString("create_user_id"));
            bid.setOptTime(rs.getString("opt_time"));
            bid.setLastModifyTime(rs.getString("last_modify_time"));
            bid.setScore(rs.getBigDecimal("score"));
            bid.setBidCatalog(rs.getString("bid_catalog"));
            bid.setStopReason(rs.getString("stop_reason"));
            return bid;
        }
    }
}
