package com.timeshare.dao.impl.assembly;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.assembly.AttenderDAO;

import com.timeshare.domain.assembly.Attender;
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
@Repository("AttendDAO")
public class AttenderDAOImpl extends BaseDAO implements AttenderDAO {

    @Override
    public String saveAttender(Attender Attender) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_attender (attender_id, user_id, assembly_id, fee_id,create_time,question_answer,user_name,phone,wx,email,company) VALUES (?, ?, ?, ?,?,?,?,?,?,?,?);");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,Attender.getUserId());
                ps.setString(3,Attender.getAssemblyId());
                ps.setString(4,Attender.getFeedId());
                ps.setString(5,Attender.getCreateTime());
                ps.setString(6,Attender.getQuestionAnswer());
                ps.setString(7,Attender.getQuestionAnswer());
                ps.setString(8,Attender.getQuestionAnswer());
                ps.setString(9,Attender.getQuestionAnswer());
                ps.setString(10,Attender.getQuestionAnswer());
                ps.setString(11,Attender.getQuestionAnswer());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public String modifyAttender(Attender Attender) {

        StringBuilder sql = new StringBuilder("update t_attender set ");

        if(StringUtils.isNotBlank(Attender.getUserId())){
            sql.append(" user_id = '"+Attender.getUserId()+"',");
        }
        if(StringUtils.isNotBlank(Attender.getAssemblyId())){
            sql.append(" assembly_id = '"+Attender.getAssemblyId()+"',");
        }
        if(StringUtils.isNotBlank(Attender.getFeedId())){
            sql.append(" fee_id = '"+Attender.getFeedId()+"',");
        }

        if (sql.lastIndexOf(",") + 1 == sql.length()) {
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
        sql.append(" where attender_id='" + Attender.getAttenderId() + "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return Attender.getAttenderId();
        }else{
            return Contants.FAILED;
        }
    }
    @Override
    public Attender findAttenderById(String AttenderId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_attender where attender_id = ?");
        if(StringUtils.isNotBlank(AttenderId)){
            List<Attender> AttenderList = getJdbcTemplate().query(sql.toString(),new Object[]{AttenderId},new AttenderRowMapper());
            if(AttenderList != null && AttenderList.size() > 0){
                Attender Attender = AttenderList.get(0);
                return Attender;
            }
        }
        return null;
    }

    @Override
    public List<Attender> findAttenderList(Attender Attender, int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select * from t_attender where 1=1");
        sql.append("   limit ?,?");
        return getJdbcTemplate().query(sql.toString(),new Object[]{startIndex,loadSize},new AttenderRowMapper());
    }


    @Override
    public int findAttenderCount(Attender Attender) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_attender where 1=1 ");
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
    }

    @Override
    public List<Attender> getListByAssemblyId(String assemblyId) {
        StringBuilder sql = new StringBuilder("select * from t_attender where assembly_id=?");
        return getJdbcTemplate().query(sql.toString(),new Object[]{assemblyId},new AttenderRowMapper());
    }

    class AttenderRowMapper implements RowMapper<Attender>{
        @Override
        public Attender mapRow(ResultSet rs, int i) throws SQLException {
            Attender attender = new Attender();
            attender.setAttenderId(rs.getString("assembly_id"));
            attender.setAssemblyId(rs.getString("assembly_id"));
            attender.setFeedId(rs.getString("fee_id"));
            attender.setUserId(rs.getString("user_id"));
            attender.setCreateTime(rs.getString("create_time"));
            attender.setQuestionAnswer(rs.getString("question_answer"));
            return attender;
        }
    }
}
