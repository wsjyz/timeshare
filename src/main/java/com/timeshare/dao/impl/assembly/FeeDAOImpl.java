package com.timeshare.dao.impl.assembly;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.assembly.FeeDAO;
import com.timeshare.domain.assembly.Assembly;
import com.timeshare.domain.assembly.Fee;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Repository("FeeDAO")
public class FeeDAOImpl extends BaseDAO implements FeeDAO {



    @Override
    public String saveFee(Fee Fee) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_fee (fee_id, fee, fee_title, quota, assembly_id) VALUES (?, ?, ?, ?, ?);");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setBigDecimal(2,Fee.getFee());
                ps.setString(3,Fee.getFeeTitle());
                ps.setInt(4,Fee.getQuota());
                ps.setString(5,Fee.getAssemblyId());

            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public String modifyFee(Fee Fee) {

        StringBuilder sql = new StringBuilder("update t_fee set ");

        if(Fee.getFee()!=null){
            sql.append(" fee = "+Fee.getFee()+",");
        }
        if(StringUtils.isNotEmpty(Fee.getFeeTitle())){
            sql.append(" fee_title = '"+Fee.getFeeTitle()+"',");
        }
        if(Fee.getQuota()>0){
            sql.append(" quota = "+Fee.getQuota()+",");
        }
        if(StringUtils.isNotEmpty(Fee.getAssemblyId())){
            sql.append(" assembly_id = '"+Fee.getAssemblyId()+"',");
        }
        if (sql.lastIndexOf(",") + 1 == sql.length()) {
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
        sql.append(" where fee_id='" + Fee.getFeeId() + "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return Fee.getFeeId();
        }else{
            return Contants.FAILED;
        }

    }

    @Override
    public Fee findFeeById(String FeeId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_fee where fee_id = ?");
        if(StringUtils.isNotBlank(FeeId)){

            List<Fee> FeeList = getJdbcTemplate().query(sql.toString(),new Object[]{FeeId},new FeeRowMapper());
            if(FeeList != null && FeeList.size() > 0){
                Fee Fee = FeeList.get(0);
                return Fee;
            }
        }
        return null;
    }

    @Override
    public List<Fee> findFeeList(Fee Fee, int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select * from t_fee where 1=1");

        sql.append("   limit ?,?");
        return getJdbcTemplate().query(sql.toString(),new Object[]{startIndex,loadSize},new FeeRowMapper());
    }

    @Override
    public int findFeeCount(Fee Fee) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_fee where 1=1 ");
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
    }

    @Override
    public List<Fee> findFeeByAssemblyId(String assemblyId) {
        StringBuilder sql = new StringBuilder("select * from t_fee where assembly_id=?");
        return getJdbcTemplate().query(sql.toString(),new Object[]{assemblyId},new FeeRowMapper());
    }

    @Override
    public void delete(String feeId) {
        int result = getJdbcTemplate().update("delete from t_fee where fee_id=?",feeId);
    }

    class FeeRowMapper implements RowMapper<Fee>{
        @Override
        public Fee mapRow(ResultSet rs, int i) throws SQLException {
            Fee fee = new Fee();
            fee.setFeeId(rs.getString("fee_id"));
            fee.setFee(rs.getBigDecimal("fee"));
            fee.setFeeTitle(rs.getString("fee_title"));
            fee.setQuota(rs.getInt("quota"));
            fee.setAssemblyId(rs.getString("assembly_id"));
            return fee;
        }
    }
}
