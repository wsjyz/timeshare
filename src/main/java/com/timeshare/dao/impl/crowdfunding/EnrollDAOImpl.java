package com.timeshare.dao.impl.crowdfunding;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.crowdfunding.CrowdFundingDAO;
import com.timeshare.dao.crowdfunding.EnrollDAO;
import com.timeshare.domain.ItemOrder;
import com.timeshare.domain.crowdfunding.CrowdFunding;
import com.timeshare.domain.crowdfunding.Enroll;
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
@Repository("EnrollDAO")
public class EnrollDAOImpl extends BaseDAO implements EnrollDAO {
    @Override
    public String saveEnroll(Enroll enroll) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_enroll (enroll_id, crowdfunding_id, enroll_user_id, user_name, phone, corp_name, invoice_title, invoice_type,pay_status,pay_amount,user_id,opt_time,pay_trade_no,refund_trade_no,is_transfer_cash_account) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?,?,?,?);");
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
                ps.setString(13,enroll.getPayTradeNo());
                ps.setString(14,enroll.getRefundTradeNo());
                ps.setString(15,enroll.getIsTransferCashAccount());
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

    public List<Enroll> findEnrollById(String enrollId) {
        StringBuilder sql = new StringBuilder("select * from t_enroll where enroll_id='"+enrollId+"'");
        return getJdbcTemplate().query(sql.toString(),new Object[]{},new EnrollRowMapper());
    }
    public List<Enroll> findEnrollByEnrollUserIdAndCrowdFundingId(String enrollUserId,String crowdFundingId) {
        StringBuilder sql = new StringBuilder("select * from t_enroll where enroll_user_id='"+enrollUserId+"' ");
        sql.append("and crowdfunding_id='"+crowdFundingId+"' ");
        sql.append("and pay_status='PAYED' ");

        return getJdbcTemplate().query(sql.toString(),new Object[]{},new EnrollRowMapper());
    }

    public int findEnrollByOwnerCount(String userId) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_enroll where user_id="+userId);
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
    }

    public List<Enroll> findEnrollByCrowdfundingId(String crowdfundingId,int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select * from t_enroll where crowdfunding_id='"+crowdfundingId+"'");
        if(startIndex!=0 || loadSize!=0){
            sql.append("limit "+startIndex+","+loadSize);
        }
        return getJdbcTemplate().query(sql.toString(),new Object[]{},new EnrollRowMapper());
    }


    public String modifyEnroll(Enroll enroll) {
        StringBuilder sql = new StringBuilder("update t_enroll set ");

        if(StringUtils.isNotBlank(enroll.getPayStatus())){
            sql.append(" pay_status = '"+enroll.getPayStatus()+"',");
        }
        if(StringUtils.isNotBlank(enroll.getPayTradeNo())){
            sql.append(" pay_trade_no = '"+enroll.getPayTradeNo()+"',");
        }
        if (sql.lastIndexOf(",") + 1 == sql.length()) {
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
        sql.append(" where enroll_id='" + enroll.getEnrollId() + "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return Contants.SUCCESS;
        }else{
            return "FAILED";
        }
    }

    public List<Enroll> findCrowdfundingByMyEnroll(int startIndex, int loadSize,String userId) {
        StringBuilder sql = new StringBuilder("select c.project_name,c.detail,o.image_url,e.* ");
        sql.append("from t_enroll e ");
        sql.append("left join t_crowdfunding c ");
        sql.append("on e.crowdfunding_id=c.crowdfunding_id ");
        sql.append("left join t_img_obj o ");
        sql.append("on o.obj_id=e.crowdfunding_id ");
        sql.append("and o.image_type='CROWD_FUNDING_IMG' ");

        sql.append("where e.enroll_user_id='"+userId +"'");
        sql.append(" and e.pay_status='PAYED' ");
        sql.append("group by e.crowdfunding_id ");
        sql.append("order by e.opt_time desc ");

        sql.append("limit "+startIndex+","+loadSize);

        return getJdbcTemplate().query(sql.toString(),new Object[]{},new EnrollDAOImpl.EnrollRowMapper());
    }

    public List<Enroll> findCrowdfundingEnrollList(String crowdfundingId,int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select o.image_url,e.* ");
        sql.append("from t_enroll e ");
        sql.append("left join t_crowdfunding c ");
        sql.append("on e.crowdfunding_id=c.crowdfunding_id ");
        sql.append("left join t_user_info u ");
        sql.append("on e.user_id=u.user_id ");
        sql.append("left join t_img_obj o  ");
        sql.append("on u.user_id=o.obj_id ");
        sql.append("and o.image_type='USER_HEAD' ");
        sql.append("where e.crowdfunding_id='"+crowdfundingId+"' ");
        sql.append("and e.pay_status='PAYED' ");

        if(startIndex>0 || loadSize>0){
            sql.append("limit "+startIndex+","+loadSize);
        }

        return getJdbcTemplate().query(sql.toString(),new Object[]{},new EnrollDAOImpl.EnrollRowMapper());
    }

    public List<Enroll> findNeedAotuRefundEnroll() {
        StringBuilder sql = new StringBuilder("select c.min_peoples,c.user_id owner_user_id,e.* ");
        sql.append("from t_crowdfunding c ");
        sql.append("left join t_enroll e ");
        sql.append("on c.crowdfunding_id=e.crowdfunding_id ");
        sql.append("where SYSDATE()>=c.curriculum_end_time ");
        sql.append("and e.pay_status='PAYED' ");
        sql.append("and (e.is_transfer_cash_account!='YES' or e.is_transfer_cash_account is null) ");

        return getJdbcTemplate().query(sql.toString(),new Object[]{},new EnrollDAOImpl.EnrollRowMapper());
    }
    public String autoRefundAfterUpdate(String enrollId,String refundTradeNo) {
        StringBuilder sql = new StringBuilder("update t_enroll set ");
        sql.append(" pay_status ='REFUND',");
        sql.append(" refund_trade_no = 'refundTradeNo'");

        sql.append(" where enroll_id='" + enrollId + "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return Contants.SUCCESS;
        }else{
            return "FAILED";
        }
    }
    public String autoMoneyTransferAfterUpdate(String enrollId) {
        StringBuilder sql = new StringBuilder("update t_enroll set ");
        sql.append(" is_transfer_cash_account ='YES'");
        sql.append(" where enroll_id='" + enrollId + "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return Contants.SUCCESS;
        }else{
            return "FAILED";
        }
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
            enroll.setPayTradeNo(rs.getString("pay_trade_no"));
            enroll.setRefundTradeNo(rs.getString("refund_trade_no"));
            enroll.setIsTransferCashAccount(rs.getString("is_transfer_cash_account"));
            try {
                if (rs.findColumn("project_name") > 0 ) {
                    enroll.setProjectName(rs.getString("project_name"));
                }
            }
            catch (SQLException e) {
            }
            try {
                if (rs.findColumn("detail") > 0 ) {
                    enroll.setDetail(rs.getString("detail"));
                }
            }
            catch (SQLException e) {
            }
            try {
                if (rs.findColumn("image_url") > 0 ) {
                    enroll.setImageUrl(rs.getString("image_url"));
                }
            }
            catch (SQLException e) {
            }
            try {
                if (rs.findColumn("min_peoples") > 0 ) {
                    enroll.setMinPeoples(rs.getString("min_peoples"));
                }
            }
            catch (SQLException e) {
            }
            try {
                if (rs.findColumn("owner_user_id") > 0 ) {
                    enroll.setOwnerUserId(rs.getString("owner_user_id"));
                }
            }
            catch (SQLException e) {
            }


            return enroll;
        }
    }

}