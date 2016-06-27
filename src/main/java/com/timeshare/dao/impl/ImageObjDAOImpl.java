package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.ImageObjDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.utils.CommonStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by adam on 2016/6/28.
 */
@Repository("imageObjDAO")
public class ImageObjDAOImpl extends BaseDAO implements ImageObjDAO {
    @Override
    public ImageObj findImgByObjIdAndType(String objId, String objType) {
        StringBuilder reviewSql = new StringBuilder("");
        reviewSql.append("select * from t_img_obj where obj_id = ? and image_type = ?");
        if(StringUtils.isNotBlank(objId)){
            List<ImageObj> imgObjList = getJdbcTemplate().query(reviewSql.toString(),new Object[]{objId,objType},new ImageObjMapper());
            if(imgObjList != null && imgObjList.size() > 0){
                ImageObj item = imgObjList.get(0);
                return item;
            }
        }
        return null;
    }

    @Override
    public void updateImg(ImageObj obj) {
        StringBuilder reviewSql = new StringBuilder("");
        reviewSql.append("update t_img_obj set image_url = ? where image_id = ? ");
        getJdbcTemplate().update(reviewSql.toString(),new Object[]{obj.getImageUrl(),obj.getImageId()});
    }

    @Override
    public String saveImg(final ImageObj obj) {
        StringBuilder sql = new StringBuilder("insert into t_img_obj (image_id,image_type,obj_id,create_user_id,opt_time,image_url)" +
                " values(?,?,?,?,?,?)");
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, CommonStringUtils.genPK());
                ps.setString(2,obj.getImageType());
                ps.setString(3,obj.getObjId());
                ps.setString(4,obj.getUserId());
                ps.setString(5,obj.getOptTime());
                ps.setString(6,obj.getImageUrl());
            }
        });
        if(result > 0){
            return "SUCCESS";
        }else{
            return "FAILED";
        }
    }

    public class ImageObjMapper implements RowMapper<ImageObj>{

        @Override
        public ImageObj mapRow(ResultSet rs, int i) throws SQLException {
            ImageObj obj = new ImageObj();
            obj.setObjId(rs.getString("image_id"));
            obj.setImageType(rs.getString("image_type"));
            obj.setObjId(rs.getString("obj_id"));
            obj.setUserId(rs.getString("create_user_id"));
            obj.setOptTime(rs.getString("opt_time"));
            obj.setImageUrl(rs.getString("image_url"));
            return obj;
        }
    }
}
