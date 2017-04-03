package com.timeshare.dao.impl.assembly;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.BidDAO;
import com.timeshare.dao.assembly.AssemblyDAO;
import com.timeshare.domain.Bid;
import com.timeshare.domain.assembly.Assembly;
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
@Repository("AssemblyDAO")
public class AssemblyDAOImpl extends BaseDAO implements AssemblyDAO {






    @Override
    public String saveAssembly(Assembly Assembly) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_assembly (assembly_id, title, start_time, end_time, rendezvous, description, type, phone_number, attent_count, comment_count, is_on_index) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,Assembly.getTitle());
                ps.setString(3,Assembly.getStartTime());
                ps.setString(4,Assembly.getEndTime());
                ps.setString(5,Assembly.getRendezvous());
                ps.setString(6,Assembly.getDescription());
                ps.setString(7,Assembly.getType());
                ps.setString(8,Assembly.getPhoneNumber());
                ps.setInt(9,Assembly.getAttentCount());
                ps.setInt(10,Assembly.getCommentCount());
                ps.setString(11,Assembly.getIsOnIndex());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public String modifyAssembly(Assembly Assembly) {

        StringBuilder sql = new StringBuilder("update t_assembly set ");

        if(StringUtils.isNotBlank(Assembly.getTitle())){
            sql.append(" title = '"+Assembly.getTitle()+"',");
        }

        if(StringUtils.isNotBlank(Assembly.getStartTime())){
            sql.append(" start_time = '"+Assembly.getStartTime()+"',");
        }
        if(StringUtils.isNotBlank(Assembly.getStartTime())){
            sql.append(" end_time = '"+Assembly.getEndTime()+"',");
        }
        if(StringUtils.isNotBlank(Assembly.getRendezvous())){
            sql.append(" rendezvous = '"+Assembly.getRendezvous()+"',");
        }
        if(StringUtils.isNotBlank(Assembly.getDescription())){
            sql.append(" description = '"+Assembly.getDescription()+"',");
        }
        if(StringUtils.isNotBlank(Assembly.getType())){
            sql.append(" type = '"+Assembly.getType()+"',");
        }
        if(StringUtils.isNotBlank(Assembly.getPhoneNumber())) {
            sql.append(" phone_number = '" + Assembly.getPhoneNumber() + "',");
        }
        if(Assembly.getAttentCount()>0){
            sql.append(" attent_count = "+Assembly.getAttentCount()+",");
        }if(Assembly.getCommentCount()>0){
            sql.append(" comment_count = "+Assembly.getCommentCount()+",");
        }if(StringUtils.isNotBlank(Assembly.getIsOnIndex())){
            sql.append(" is_on_index = '"+Assembly.getIsOnIndex()+"',");
        }
        if (sql.lastIndexOf(",") + 1 == sql.length()) {
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
        sql.append(" where assembly_id='" + Assembly.getAssemblyId() + "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return Assembly.getAssemblyId();
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public Assembly findAssemblyById(String AssemblyId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_assembly where assembly_id = ?");
        if(StringUtils.isNotBlank(AssemblyId)){

            List<Assembly> AssemblyList = getJdbcTemplate().query(sql.toString(),new Object[]{AssemblyId},new AssemblyRowMapper());
            if(AssemblyList != null && AssemblyList.size() > 0){
                Assembly Assembly = AssemblyList.get(0);
                return Assembly;
            }
        }
        return null;
    }

    @Override
    public List<Assembly> findAssemblyList(Assembly Assembly, int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select * from t_assembly where 1=1");

        sql.append("   limit ?,?");
        return getJdbcTemplate().query(sql.toString(),new Object[]{startIndex,loadSize},new AssemblyRowMapper());
    }

    @Override
    public int findAssemblyCount(Assembly Assembly) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_assembly where 1=1 ");
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
    }

    class AssemblyRowMapper implements RowMapper<Assembly>{

        @Override
        public Assembly mapRow(ResultSet rs, int i) throws SQLException {
            Assembly assembly = new Assembly();
            assembly.setAssemblyId(rs.getString("assembly_id"));
            assembly.setTitle(rs.getString("title"));
            assembly.setStartTime(rs.getString("start_time"));
            assembly.setEndTime(rs.getString("end_time"));
            assembly.setRendezvous(rs.getString("rendezvous"));
            assembly.setDescription(rs.getString("description"));
            assembly.setType(rs.getString("type"));
            assembly.setPhoneNumber(rs.getString("phone_number"));
            assembly.setAttentCount(rs.getInt("attent_count"));
            assembly.setCommentCount(rs.getInt("comment_count"));
            assembly.setIsOnIndex(rs.getString("is_on_index"));
            return assembly;
        }
    }
}
