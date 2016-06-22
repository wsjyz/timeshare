package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.ItemDAO;
import com.timeshare.domain.Item;
import com.timeshare.domain.OpenPage;
import com.timeshare.utils.CommonStringUtils;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by user on 2016/6/21.
 */
@Repository("ItemDAO")
public class ItemDAOImpl extends BaseDAO implements ItemDAO {

    @Override
    public String saveItem(final Item info) {
        StringBuilder sql = new StringBuilder("insert into t_item (item_id,title,price,score,description,item_type,use_count,create_user_id,opt_time,create_user_name,item_status)" +
                " values(?,?,?,?,?,?,?,?,?,?,?)");
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, CommonStringUtils.genPK());
                ps.setString(2,info.getTitle());
                ps.setBigDecimal(3,info.getPrice());
                ps.setBigDecimal(4,info.getScore());
                ps.setString(5,info.getDescription());
                ps.setString(6,info.getItemType());
                ps.setInt(7,info.getUseCount());
                ps.setString(8,info.getUserId());
                ps.setString(9,info.getOptTime());
                ps.setString(10,info.getCreateUserName());
                ps.setString(11,info.getItemStatus());
            }
        });
        if(result > 0){
            return "SUCCESS";
        }else{
            return "FAILED";
        }
    }

    @Override
    public String modifyItem(Item Item) {
        return null;
    }

    @Override
    public Item findItemByItemId(String ItemId) {
        return null;
    }

    @Override
    public String deleteById(String itemId) {
        return null;
    }

    @Override
    public OpenPage<Item> findItemPage(String mobile, String nickName, OpenPage page) {
        return null;
    }
}
