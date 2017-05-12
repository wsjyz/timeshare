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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by user on 2016/9/23.
 */
@Repository("AssemblyDAO")
public class AssemblyDAOImpl extends BaseDAO implements AssemblyDAO {






    @Override
    public String saveAssembly(Assembly Assembly) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_assembly (assembly_id, title, start_time, end_time, rendezvous, description,user_id, type, phone_number, attent_count, comment_count, is_on_index,is_on_apply,show_apply_problem,create_time,status,carousel) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?);");
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
                ps.setString(7,Assembly.getUserId());
                ps.setString(8,Assembly.getType());
                ps.setString(9,Assembly.getPhoneNumber());
                ps.setInt(10,Assembly.getAttentCount());
                ps.setInt(11,Assembly.getCommentCount());
                ps.setString(12,Assembly.getIsOnIndex());
                ps.setString(13,Assembly.getIsOnApply());
                ps.setString(14,Assembly.getShowApplyProblem());
                ps.setString(15,Assembly.getCreateTime());
                ps.setString(16,Assembly.getStatus());
                ps.setString(17,"true");

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
        if(StringUtils.isNotBlank(Assembly.getUserId())){
            sql.append(" user_id = '"+Assembly.getUserId()+"',");
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
        if(StringUtils.isNotBlank(Assembly.getIsOnApply())){
        sql.append(" is_on_apply = '"+Assembly.getIsOnApply()+"',");
        }
        if(StringUtils.isNotBlank(Assembly.getShowApplyProblem())){
            sql.append(" show_apply_problem = '"+Assembly.getShowApplyProblem()+"',");
        }
        if(Assembly.getBrowseTimes()>0){
            sql.append(" browse_times = "+Assembly.getBrowseTimes()+",");
        }
        if(StringUtils.isNotBlank(Assembly.getStatus())){
            sql.append(" status = '"+Assembly.getStatus()+"',");
        }
        if(StringUtils.isNotBlank(Assembly.getResultContent())){
            sql.append(" result_content = '"+Assembly.getResultContent()+"',");
        }
        if(StringUtils.isNotBlank(Assembly.getCarousel())){
            sql.append(" carousel = '"+Assembly.getCarousel()+"',");
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
        StringBuilder sql = new StringBuilder("select * from t_assembly ass  where 1=1");
        List<Object> list=new ArrayList<Object>();
        if (StringUtils.isNotEmpty(Assembly.getType())){
            sql.append(" and ass.type=?");
            list.add(Assembly.getType());
        }
        if (StringUtils.isNotEmpty(Assembly.getStatus())){
            sql.append(" and ass.status=?");
            list.add(Assembly.getStatus());
        }
        if (StringUtils.isNotEmpty(Assembly.getTitle())){
            sql.append(" and ass.title like ?");
            list.add("%"+Assembly.getTitle()+"%");
        }
        if (StringUtils.isNotEmpty(Assembly.getRendezvous())){
            sql.append(" and ass.rendezvous like ?");
            list.add("%"+Assembly.getRendezvous()+"%");
        }
        ;if (StringUtils.isNotEmpty(Assembly.getUserId())){
            sql.append(" and ass.user_id=?");
            list.add(Assembly.getUserId());
        }
        if("true".equals(Assembly.getShowOldTime())){
            sql.append(" and ass.end_time>now()");
        }
        ;if (StringUtils.isNotEmpty(Assembly.getCarousel())){
            sql.append(" and ass.carousel=?");
            list.add(Assembly.getCarousel());
        }
        sql.append(" order by create_time desc  limit ?,?");
        list.add(startIndex);
        list.add(loadSize);
        return getJdbcTemplate().query(sql.toString(),list.toArray(),new AssemblyRowMapper());
    }

    @Override
    public int findAssemblyCount(Assembly Assembly) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_assembly where 1=1 ");
        List<Object> list=new ArrayList<Object>();
        if (StringUtils.isNotEmpty(Assembly.getType())){
            countSql.append(" and ass.type=?");
            list.add(Assembly.getType());
        }
        if (StringUtils.isNotEmpty(Assembly.getTitle())){
            countSql.append(" and ass.title like ?");
            list.add("%"+Assembly.getTitle()+"%");
        }
        return getJdbcTemplate().queryForObject(countSql.toString(),list.toArray(), Integer.class);
    }

    @Override
    public List<Assembly> findSignAssemblyList(Assembly Assembly, String userId, int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select * from t_assembly ass left join t_attender att on att.assembly_id=ass.assembly_id  where 1=1");
        List<Object> list=new ArrayList<Object>();
        if (StringUtils.isNotEmpty(Assembly.getType())){
            sql.append(" and ass.type=?");
            list.add(Assembly.getType());
        }
        if (StringUtils.isNotEmpty(Assembly.getStatus())){
            sql.append(" and ass.status=?");
            list.add(Assembly.getStatus());
        }
        if (StringUtils.isNotEmpty(Assembly.getTitle())){
            sql.append(" and ass.title like ?");
            list.add("%"+Assembly.getTitle()+"%");
        }
        if (StringUtils.isNotEmpty(Assembly.getRendezvous())){
            sql.append(" and ass.rendezvous like ?");
            list.add("%"+Assembly.getRendezvous()+"%");
        }
        ;if (StringUtils.isNotEmpty(Assembly.getUserId())){
            sql.append(" and ass.user_id=?");
            list.add(Assembly.getUserId());
        }
        sql.append(" and att.user_id=? ");
        list.add(userId);
        sql.append(" limit ?,?");
        list.add(startIndex);
        list.add(loadSize);
        return getJdbcTemplate().query(sql.toString(),list.toArray(),new AssemblyRowMapper());
    }

    @Override
    public int deleteAssembly(String assemblyId) {
        return getJdbcTemplate().update("delete from t_assembly where assembly_id=?",new Object[]{assemblyId});
    }

    @Override
    public List<Assembly> findCollectionAssemblyList(Assembly Assembly, String userId, int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select * from t_assembly ass left join t_collection att on att.assembly_id=ass.assembly_id  where 1=1");
        List<Object> list=new ArrayList<Object>();
        if (StringUtils.isNotEmpty(Assembly.getType())){
            sql.append(" and ass.type=?");
            list.add(Assembly.getType());
        }
        if (StringUtils.isNotEmpty(Assembly.getStatus())){
            sql.append(" and ass.status=?");
            list.add(Assembly.getStatus());
        }
        if (StringUtils.isNotEmpty(Assembly.getTitle())){
            sql.append(" and ass.title like ?");
            list.add("%"+Assembly.getTitle()+"%");
        }
        if (StringUtils.isNotEmpty(Assembly.getRendezvous())){
            sql.append(" and ass.rendezvous like ?");
            list.add("%"+Assembly.getRendezvous()+"%");
        }
        ;if (StringUtils.isNotEmpty(Assembly.getUserId())){
            sql.append(" and ass.user_id=?");
            list.add(Assembly.getUserId());
        }
        sql.append(" and att.user_id=? ");
        list.add(userId);
        sql.append(" limit ?,?");
        list.add(startIndex);
        list.add(loadSize);
        return getJdbcTemplate().query(sql.toString(),list.toArray(),new AssemblyRowMapper());
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
            assembly.setUserId(rs.getString("user_id"));
            assembly.setType(rs.getString("type"));
            assembly.setPhoneNumber(rs.getString("phone_number"));
            assembly.setAttentCount(rs.getInt("attent_count"));
            assembly.setCommentCount(rs.getInt("comment_count"));
            assembly.setIsOnIndex(rs.getString("is_on_index"));
            assembly.setIsOnApply(rs.getString("is_on_apply"));
            assembly.setShowApplyProblem(rs.getString("show_apply_problem"));
            assembly.setBrowseTimes(rs.getInt("browse_times"));
            assembly.setCreateTime(rs.getString("create_time"));
            assembly.setStatus(rs.getString("status"));
            assembly.setResultContent(rs.getString("result_content"));
            assembly.setCarousel(rs.getString("carousel"));
            return assembly;
        }
    }
}
