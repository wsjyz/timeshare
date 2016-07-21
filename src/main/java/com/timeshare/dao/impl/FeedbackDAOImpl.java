package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.FeedbackDAO;
import com.timeshare.domain.Feedback;
import com.timeshare.domain.OpenPage;
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
 * Created by user on 2016/7/20.
 */
@Repository("FeedbackDAO")
public class FeedbackDAOImpl extends BaseDAO implements FeedbackDAO {
    @Override
    public String saveFeedback(Feedback info) {
        StringBuilder sql = new StringBuilder("insert into t_feed_back (feedback_id,title,content,item_id,item_title,to_user_id,create_user_name,create_user_id,opt_time,rating)" +
                " values(?,?,?,?,?,?,?,?,?,?)");
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, CommonStringUtils.genPK());
                ps.setString(2,info.getTitle());
                ps.setString(3,info.getContent());
                ps.setString(4,info.getItemId());
                ps.setString(5,info.getItemTitle());
                ps.setString(6,info.getToUserId());
                ps.setString(7,info.getCreateUserName());
                ps.setString(8,info.getUserId());
                ps.setString(9,info.getOptTime());
                ps.setInt(10,info.getRating());
            }
        });
        if(result > 0){
            return Contants.SUCCESS;
        }else{
            return "FAILED";
        }
    }

    @Override
    public String modifyFeedback(Feedback itemFeedback) {
        return null;
    }

    @Override
    public Feedback findFeedbackByFeedbackId(String FeedbackId) {
        return null;
    }

    @Override
    public String deleteById(String FeedbackId) {
        return null;
    }

    @Override
    public OpenPage<Feedback> findFeedbackPage(String mobile, String nickName, OpenPage page) {
        return null;
    }

    @Override
    public List<Feedback> findFeedbackListByToUserId(String toUserId) {
        StringBuilder reviewSql = new StringBuilder("");
        List<Feedback> feedbackList = new ArrayList<>();
        reviewSql.append("select * from t_feed_back where to_user_id = ?");
        if(StringUtils.isNotBlank(toUserId)){

            feedbackList = getJdbcTemplate().query(reviewSql.toString(),new Object[]{toUserId},new FeedbackMapper());

        }
        return feedbackList;
    }

    @Override
    public List<Feedback> findFeedbackListByItemId(String itemId) {
        StringBuilder reviewSql = new StringBuilder("");
        List<Feedback> feedbackList = new ArrayList<>();
        reviewSql.append("select * from t_feed_back where item_id = ?");
        if(StringUtils.isNotBlank(itemId)){

            feedbackList = getJdbcTemplate().query(reviewSql.toString(),new Object[]{itemId},new FeedbackMapper());

        }
        return feedbackList;
    }

    @Override
    public Feedback findFeedBackByCreateUserId(String createUserId,String itemId) {
        StringBuilder reviewSql = new StringBuilder("");
        reviewSql.append("select * from t_feed_back where item_id = ? and create_user_id = ?");
        if(StringUtils.isNotBlank(createUserId)){

            List<Feedback> feedbackList = getJdbcTemplate().query(reviewSql.toString(),new Object[]{itemId,createUserId},new FeedbackMapper());
            if(feedbackList != null && feedbackList.size() > 0){
                Feedback feedback = feedbackList.get(0);
                return feedback;
            }
        }
        return null;
    }

    private class FeedbackMapper implements RowMapper<Feedback>{

        @Override
        public Feedback mapRow(ResultSet rs, int i) throws SQLException {
            Feedback f = new Feedback();
            f.setCreateUserName(rs.getString("create_user_name"));
            f.setUserId(rs.getString("create_user_id"));
            f.setFeedBackId(rs.getString("feedback_id"));
            f.setTitle(rs.getString("title"));
            f.setContent(rs.getString("content"));
            f.setItemId(rs.getString("item_id"));
            f.setToUserId(rs.getString("to_user_id"));
            f.setItemTitle(rs.getString("item_title"));
            f.setOptTime(rs.getString("opt_time"));
            f.setRating(rs.getInt("rating"));
            return f;
        }
    }
}
