package com.timeshare.dao.impl.assembly;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.assembly.CommentDAO;
import com.timeshare.dao.assembly.FeeDAO;
import com.timeshare.domain.assembly.Comment;
import com.timeshare.domain.assembly.Fee;
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
@Repository("CommentDAO")
public class CommentDAOImpl extends BaseDAO implements CommentDAO {
    @Override
    public String saveComment(Comment Comment) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_comment (comment_id, content, rating, zan_content, reply_content, obj_type, obj_id, user_id,create_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?);");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setString(2,Comment.getContent());
                ps.setInt(3,Comment.getRating());
                ps.setString(4,Comment.getZanContent());
                ps.setString(5,Comment.getReplyContent());
                ps.setString(6,Comment.getObjType());
                ps.setString(7,Comment.getObjId());
                ps.setString(8,Comment.getUserId());
                ps.setString(9,Comment.getCreateTime());

            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public String modifyComment(Comment Comment) {

        StringBuilder sql = new StringBuilder("update t_comment set ");
        if(StringUtils.isNotEmpty(Comment.getZanContent())){
            sql.append(" zan_content = '"+Comment.getZanContent()+"',");
        }
        if(StringUtils.isNotEmpty(Comment.getReplyContent())){
            sql.append(" reply_content = '"+Comment.getReplyContent()+"',");
        }
        if (sql.lastIndexOf(",") + 1 == sql.length()) {
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
        sql.append(" where comment_id='" + Comment.getCommentId()+ "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return Comment.getCommentId();
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public Comment findCommentById(String CommentId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_comment where comment_id = ?");
        if(StringUtils.isNotBlank(CommentId)){
            List<Comment> commentList = getJdbcTemplate().query(sql.toString(),new Object[]{CommentId},new CommentRowMapper());
            if(commentList != null && commentList.size() > 0){
                Comment Comment = commentList.get(0);
                return Comment;
            }
        }
        return null;
    }
    @Override
    public List<Comment> findCommentList(Comment Comment) {
        StringBuilder sql = new StringBuilder("select * from t_comment where 1=1");
        return getJdbcTemplate().query(sql.toString(),new Object[]{},new CommentRowMapper());
    }

    @Override
    public int findCommentCount(Comment Comment) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_comment where 1=1 ");
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
    }

    @Override
    public List<Comment> findCommentByObjId(String assemblyId) {
        StringBuilder sql = new StringBuilder("select * from t_comment where obj_id=?");
        return getJdbcTemplate().query(sql.toString(),new Object[]{assemblyId},new CommentRowMapper());
    }

    class CommentRowMapper implements RowMapper<Comment>{
        @Override
        public Comment mapRow(ResultSet rs, int i) throws SQLException {
            Comment comment = new Comment();
            comment.setCommentId(rs.getString("comment_id"));
            comment.setContent(rs.getString("content"));
            comment.setRating(rs.getInt("rating"));
            comment.setZanContent(rs.getString("zan_content"));
            comment.setReplyContent(rs.getString("reply_content"));
            comment.setObjType(rs.getString("obj_type"));
            comment.setObjId(rs.getString("obj_id"));
            comment.setUserId(rs.getString("user_id"));
            comment.setCreateTime(rs.getString("create_time"));
            return comment;
        }
    }
}
