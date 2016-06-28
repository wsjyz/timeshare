package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.UserDAO;
import com.timeshare.domain.OpenPage;
import com.timeshare.domain.UserInfo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by user on 2016/6/22.
 */
@Repository("UserDAO")
public class UserDAOImpl extends BaseDAO implements UserDAO {

    @Override
    public void saveUser(UserInfo info) {

    }

    @Override
    public String modifyUser(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo findUserByUserId(String userId) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_user_info where user_id = ?");
        List<UserInfo> auntInfoList = getJdbcTemplate().query(sql.toString(),
                new String[] { userId }, new UserInfoMapper());
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
            return userInfo;
        }
    }
}
