package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.ItemDAO;
import com.timeshare.domain.Item;
import com.timeshare.domain.OpenPage;
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
    public Item findItemByItemId(String itemId) {

        StringBuilder reviewSql = new StringBuilder("");
        reviewSql.append("select * from t_item where item_id = ?");
        if(StringUtils.isNotBlank(itemId)){
            List<Item> itemList = getJdbcTemplate().query(reviewSql.toString(),new Object[]{itemId},new ItemMapper());
            if(itemList != null && itemList.size() > 0){
                Item item = itemList.get(0);
                return item;
            }
        }
        return null;
    }

    @Override
    public String deleteById(String itemId) {
        return null;
    }

    @Override
    public List<Item> findItemPage(Item item, int startIndex, int loadSize) {
        StringBuilder reviewSql = new StringBuilder("");
        reviewSql.append("select * from t_item where 1=1");
        if(item != null){
            if (StringUtils.isNotEmpty(item.getTitle())) {
                reviewSql.append(" and title ='"+item.getTitle()+"' ");
            }
            if (StringUtils.isNotEmpty(item.getItemStatus())) {
                reviewSql.append(" and item_status = '"+item.getItemStatus()+"' ");
            }
        }

        reviewSql.append(" limit ?,?");
        List<Item> itemList = getJdbcTemplate().query(reviewSql.toString(),new Object[]{startIndex,loadSize},new ItemMapper());

        return itemList;
    }
    public class ItemMapper implements RowMapper<Item>{

        @Override
        public Item mapRow(ResultSet rs, int i) throws SQLException {
            Item item = new Item();
            item.setItemId(rs.getString("item_id"));
            item.setCreateUserName(rs.getString("create_user_name"));
            item.setUserId(rs.getString("create_user_id"));
            item.setOptTime(rs.getString("opt_time"));
            item.setDescription(rs.getString("description"));
            item.setItemStatus(rs.getString("item_status"));
            item.setPrice(rs.getBigDecimal("price"));
            item.setScore(rs.getBigDecimal("score"));
            item.setItemType(rs.getString("item_type"));
            item.setTitle(rs.getString("title"));
            item.setUseCount(rs.getInt("use_count"));
            return item;
        }
    }
}
