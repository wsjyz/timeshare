package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.ImageObjDAO;
import com.timeshare.dao.UserDAO;
import com.timeshare.domain.ImageObj;
import com.timeshare.domain.OpenPage;
import com.timeshare.domain.UserInfo;
import com.timeshare.utils.CommonStringUtils;
import com.timeshare.utils.Contants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/6/22.
 */
@Repository("UserDAO")
public class UserDAOImpl extends BaseDAO implements UserDAO {

    @Autowired
    ImageObjDAO imageObjDAO;

    @Override
    public String saveUser(UserInfo info) {
        StringBuilder sql = new StringBuilder("insert into t_user_info (user_id,mobile,open_id,user_name,nick_name,income,sum_cost,sell_counts,buy_counts,description,position,corp,industry,city,ageGroup)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, info.getUserId());
                ps.setString(2,info.getMobile());
                ps.setString(3,info.getOpenId());
                ps.setString(4,info.getUserName());
                ps.setString(5,info.getNickName());
                ps.setBigDecimal(6,info.getIncome());
                ps.setBigDecimal(7,info.getSumCost());
                ps.setInt(8,info.getSellCounts());
                ps.setInt(9,info.getBuyCounts());
                ps.setString(10,info.getDescription());
                ps.setString(11,info.getPosition());
                ps.setString(12,info.getCorp());
                ps.setString(13,info.getIndustry());
                ps.setString(14,info.getCity());
                ps.setString(15,info.getAgeGroup());
            }
        });
        ImageObj obj = info.getImageObj();
        if(obj != null){
            obj.setImageId(CommonStringUtils.genPK());
            obj.setObjId(info.getUserId());
            obj.setImageType(Contants.IMAGE_TYPE.USER_HEAD.toString());
            imageObjDAO.saveImg(obj);
        }
        if(result > 0){
            return Contants.SUCCESS;
        }else{
            return "FAILED";
        }
    }

    @Override
    public String modifyUser(UserInfo userInfo) {

        if(userInfo!= null && StringUtils.isNotBlank(userInfo.getUserId())){
            StringBuilder sql = new StringBuilder("update t_user_info set ");

            if(StringUtils.isNotBlank(userInfo.getMobile())){
                sql.append(" mobile = '"+userInfo.getMobile()+"',");
            }
            if(StringUtils.isNotBlank(userInfo.getUserName())){
                sql.append(" user_name = '"+userInfo.getUserName()+"',");
            }
            if(StringUtils.isNotBlank(userInfo.getNickName())){
                sql.append(" nick_name = '"+userInfo.getNickName()+"',");
            }
            if(userInfo.getIncome() != null && userInfo.getIncome().intValue() != 0){
                sql.append(" income = "+userInfo.getIncome()+",");
            }
            if(userInfo.getSumCost() != null && userInfo.getSumCost().intValue() != 0){
                sql.append(" sum_cost = "+userInfo.getSumCost()+",");
            }
            if(userInfo.getSellCounts() != 0){
                sql.append(" sell_counts = "+userInfo.getSellCounts()+",");
            }
            if(userInfo.getBuyCounts() != 0){
                sql.append(" buy_counts = "+userInfo.getBuyCounts()+",");
            }
            if(StringUtils.isNotBlank(userInfo.getDescription())){
                sql.append(" description = '"+userInfo.getDescription()+"',");
            }
            if(StringUtils.isNotBlank(userInfo.getPosition())){
                sql.append(" position = '"+userInfo.getPosition()+"',");
            }
            if(StringUtils.isNotBlank(userInfo.getCorp())){
                sql.append(" corp = '"+userInfo.getCorp()+"',");
            }
            if(StringUtils.isNotBlank(userInfo.getIndustry())){
                sql.append(" industry = '"+userInfo.getIndustry()+"',");
            }
            if(StringUtils.isNotBlank(userInfo.getCity())){
                sql.append(" city = '"+userInfo.getCity()+"',");
            }
            if(StringUtils.isNotBlank(userInfo.getAgeGroup())){
                sql.append(" ageGroup = '"+userInfo.getAgeGroup()+"',");
            }

            if (sql.lastIndexOf(",") + 1 == sql.length()) {
                sql.delete(sql.lastIndexOf(","), sql.length());
            }
            sql.append(" where user_id ='" + userInfo.getUserId() + "'");
            int result = getJdbcTemplate().update(sql.toString());
            if(result > 0){
                return Contants.SUCCESS;
            }else{
                return "FAILED";
            }
        }
        return "FAILED";
    }

    @Override
    public UserInfo findUserByUserId(String userId,ImageObj imageObj) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select u.*,i.image_url from t_user_info u left join t_img_obj i on u.user_id = i.obj_id " +
                " and i.image_type = '"+ imageObj.getImageType()+"'"+
                " where u.user_id = ? " );
        List<UserInfo> auntInfoList = getJdbcTemplate().query(sql.toString(),
                new String[] { userId }, new UserInfoMapper());
        if (!CollectionUtils.isEmpty(auntInfoList)) {
            UserInfo userInfo = auntInfoList.get(0);
            return userInfo;
        }
        return null;
    }

    @Override
    public UserInfo findUserByOpenId(String openId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select u.* from t_user_info u  where open_id = ?" );
        List<String> excludeFields = new ArrayList<>();
        excludeFields.add("image_url");
        List<UserInfo> auntInfoList = getJdbcTemplate().query(sql.toString(),
                new String[] { openId }, new UserInfoMapper(excludeFields));
        if (!CollectionUtils.isEmpty(auntInfoList)) {
            UserInfo userInfo = auntInfoList.get(0);
            return userInfo;
        }
        return null;
    }

    @Override
    public String deleteById(String userId) {
        return null;
    }

    @Override
    public OpenPage<UserInfo> findUserPage(String mobile, String nickName, OpenPage page) {
        return null;
    }

    public class UserInfoMapper implements RowMapper<UserInfo>{

        List<String> excludeFields = new ArrayList<>();

        public UserInfoMapper(){

        }

        public UserInfoMapper(List<String> excludeFields){
            this.excludeFields = excludeFields;
        }
        @Override
        public UserInfo mapRow(ResultSet rs, int i) throws SQLException {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(rs.getString("user_id"));
            userInfo.setBuyCounts(rs.getInt("buy_counts"));
            userInfo.setDescription(rs.getString("description"));
            userInfo.setIncome(rs.getBigDecimal("income"));
            userInfo.setNickName(rs.getString("nick_name"));
            userInfo.setMobile(rs.getString("mobile"));
            userInfo.setSellCounts(rs.getInt("sell_counts"));
            userInfo.setSumCost(rs.getBigDecimal("sum_cost"));
            userInfo.setUserName(rs.getString("user_name"));
            userInfo.setPosition(rs.getString("position"));
            userInfo.setCorp(rs.getString("corp"));
            userInfo.setIndustry(rs.getString("industry"));
            userInfo.setCity(rs.getString("city"));
            userInfo.setAgeGroup(rs.getString("ageGroup"));
            userInfo.setOpenId(rs.getString("open_id"));
            if(!excludeFields.contains("image_url")){
                ImageObj img = new ImageObj();
                img.setImageUrl(rs.getString("image_url"));
                userInfo.setImageObj(img);
            }

            return userInfo;
        }
    }
}
