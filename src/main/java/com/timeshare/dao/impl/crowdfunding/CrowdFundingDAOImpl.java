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
        StringBuilder sql = new StringBuilder("INSERT INTO t_crowdfunding (crowdfunding_id, project_name, curriculum_start_time, curriculum_end_time, sponsor_city, detail, cost_type, cost_total,min_peoples,max_peoples,reservation_cost,crowdfunding_index_img_path,is_show,crowdfunding_status,off_shelve_reason,user_id,create_user_name,create_time,opt_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?, ?, ?,?,?);");
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
                ps.setString(12,crowdFunding.getCrowdfundingIndexImgPath());
                ps.setString(13,crowdFunding.getIsShow());
                ps.setString(14,crowdFunding.getCrowdfundingStatus());
                ps.setString(15,crowdFunding.getOffShelveReason());
                ps.setString(16,crowdFunding.getUserId());
                ps.setString(17,crowdFunding.getCreateUserName());
                ps.setString(18,crowdFunding.getCreateTime());
                ps.setString(19,crowdFunding.getOptTime());
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
        StringBuilder sql = new StringBuilder("select * from t_crowdfunding where user_id='"+userId+"' limit "+startIndex+","+loadSize);
        return getJdbcTemplate().query(sql.toString(),new Object[]{},new CrowdFundingRowMapper());
    }


    public int findCrowdFundingByOwnerCount(String userId) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_crowdfunding where user_id="+userId);
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
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
            crowdFunding.setCrowdfundingIndexImgPath(rs.getString("crowdfunding_index_img_path"));
            crowdFunding.setIsShow(rs.getString("is_show"));
            crowdFunding.setCrowdfundingStatus(rs.getString("crowdfunding_status"));
            crowdFunding.setOffShelveReason(rs.getString("off_shelve_reason"));
            crowdFunding.setUserId(rs.getString("user_id"));
            crowdFunding.setCreateUserName(rs.getString("create_user_name"));
            crowdFunding.setCreateTime(rs.getString("create_time"));
            crowdFunding.setOptTime(rs.getString("opt_time"));
            return crowdFunding;
        }
    }
}
