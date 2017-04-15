package com.timeshare.dao.impl.crowdfunding;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.assembly.CommentDAO;
import com.timeshare.dao.crowdfunding.CrowdFundingDAO;
import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.crowdfunding.CrowdFunding;
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
@Repository("CrowdFundingDAO")
public class CrowdFundingDAOImpl extends BaseDAO implements CrowdFundingDAO {
    @Override
    public String saveCrowdFunding(CrowdFunding crowdFunding) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_crowdfunding (crowdfunding_id, project_name, curriculum_start_time, curriculum_end_time, sponsor_city, detail, cost_type, cost_total,min_peoples,max_peoples,reservation_cost,is_show,crowdfunding_status,off_shelve_reason,user_id,create_user_name,create_time,opt_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?, ?, ?,?,?);");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,crowdFunding.getProjectName());
                ps.setString(3,crowdFunding.getCurriculumStartTime());
                ps.setString(4,crowdFunding.getCurriculumEndTime());
                ps.setString(5,crowdFunding.getSponsorCity());
                ps.setString(6,crowdFunding.getDetail());
                ps.setString(7,crowdFunding.getCostType());
                ps.setBigDecimal(8,crowdFunding.getCostTotal());
                ps.setInt(9,crowdFunding.getMinPeoples());
                ps.setInt(10,crowdFunding.getMaxPeoples());
                ps.setBigDecimal(11,crowdFunding.getReservationCost());
                ps.setString(12,crowdFunding.getIsShow());
                ps.setString(13,crowdFunding.getCrowdfundingStatus());
                ps.setString(14,crowdFunding.getOffShelveReason());
                ps.setString(15,crowdFunding.getUserId());
                ps.setString(16,crowdFunding.getCreateUserName());
                ps.setString(17,crowdFunding.getCreateTime());
                ps.setString(18,crowdFunding.getOptTime());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public List<CrowdFunding> findCrowdFundingByOwner(String userId,int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select * from t_crowdfunding ");
        if(StringUtils.isNotBlank(userId)){
            sql.append("where user_id='"+userId+"'");
        }
        sql.append(" limit "+startIndex+","+loadSize);

        return getJdbcTemplate().query(sql.toString(),new Object[]{},new CrowdFundingRowMapper());
    }

    @Override
    public List<CrowdFunding> findCrowdFundingById(String crowdFundingById) {
        StringBuilder sql = new StringBuilder("select * from t_crowdfunding ");
        if(StringUtils.isNotBlank(crowdFundingById)){
            sql.append("where crowdfunding_id='"+crowdFundingById+"'");
        }
        return getJdbcTemplate().query(sql.toString(),new Object[]{},new CrowdFundingRowMapper());
    }


    public int findCrowdFundingByOwnerCount(String userId) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_crowdfunding where user_id="+userId);
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
    }

    public List<CrowdFunding> findCrowdFundingToIndex(int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("select count(e.enroll_id) enroll_count,o.image_url,c.* ");
        sql.append("from t_crowdfunding c ");
        sql.append("left join t_enroll e ");
        sql.append("on c.crowdfunding_id=e.crowdfunding_id ");
        sql.append("left join t_img_obj o ");
        sql.append("on c.crowdfunding_id=o.obj_id ");
        sql.append("and o.image_type='CROWD_FUNDING_IMG' ");

        sql.append("where 1=1 ");
        sql.append("and e.pay_status='PAYED' ");
        sql.append("and e.enroll_id is not null ");
        sql.append("and c.crowdfunding_status='RELEASED' ");
        sql.append("and c.curriculum_end_time>=SYSDATE() ");
        sql.append("and c.curriculum_start_time<=SYSDATE() ");

        sql.append("group by c.crowdfunding_id ");
        sql.append("order by c.create_time desc ");

        sql.append("limit "+startIndex+","+loadSize);


        return getJdbcTemplate().query(sql.toString(),new Object[]{},new CrowdFundingRowMapper());
    }

    class CrowdFundingRowMapper implements RowMapper<CrowdFunding>{
        @Override
        public CrowdFunding mapRow(ResultSet rs, int i) throws SQLException {
            CrowdFunding crowdFunding = new CrowdFunding();
            crowdFunding.setCrowdfundingId(rs.getString("crowdfunding_id"));
            crowdFunding.setProjectName(rs.getString("project_name"));
            crowdFunding.setCurriculumStartTime(rs.getString("curriculum_start_time"));
            crowdFunding.setCurriculumEndTime(rs.getString("curriculum_end_time"));
            crowdFunding.setSponsorCity(rs.getString("sponsor_city"));
            crowdFunding.setDetail(rs.getString("detail"));
            crowdFunding.setCostType(rs.getString("cost_type"));
            crowdFunding.setCostTotal(rs.getBigDecimal("cost_total"));
            crowdFunding.setMinPeoples(rs.getInt("min_peoples"));
            crowdFunding.setMaxPeoples(rs.getInt("max_peoples"));
            crowdFunding.setReservationCost(rs.getBigDecimal("reservation_cost"));
            crowdFunding.setImageUrl(rs.getString("image_url"));
            crowdFunding.setIsShow(rs.getString("is_show"));
            crowdFunding.setCrowdfundingStatus(rs.getString("crowdfunding_status"));
            crowdFunding.setOffShelveReason(rs.getString("off_shelve_reason"));
            crowdFunding.setUserId(rs.getString("user_id"));
            crowdFunding.setCreateUserName(rs.getString("create_user_name"));
            crowdFunding.setCreateTime(rs.getString("create_time"));
            crowdFunding.setOptTime(rs.getString("opt_time"));
            crowdFunding.setEnrollCount(rs.getInt("enroll_count"));
            return crowdFunding;
        }
    }
}
