package com.timeshare.dao.impl;

import com.timeshare.controller.ItemDTO;
import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.ItemDAO;
import com.timeshare.domain.Item;
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
 * Created by user on 2016/6/21.
 */
@Repository("ItemDAO")
public class ItemDAOImpl extends BaseDAO implements ItemDAO {

    @Override
    public String saveItem(final Item info) {
        StringBuilder sql = new StringBuilder("insert into t_item (item_id,title,price,score,description,item_type,use_count,create_user_id,opt_time,create_user_name,item_status,recommend,duration)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
                ps.setBoolean(12,info.isRecommend());
                ps.setInt(13,info.getDuration());
            }
        });
        if(result > 0){
            return Contants.SUCCESS;
        }else{
            return "FAILED";
        }
    }

    @Override
    public String modifyItem(Item item) {
        StringBuilder sql = new StringBuilder("update t_item set ");

        if(StringUtils.isNotBlank(item.getTitle())){
            sql.append(" title = '"+item.getTitle()+"',");
        }
        if(item.getPrice() != null && item.getPrice().intValue() != 0){
            sql.append(" price = "+item.getPrice()+",");
        }
        if(item.getDuration() != 0){
            sql.append(" duration = "+item.getDuration()+",");
        }
        if(StringUtils.isNotBlank(item.getItemStatus())){
            sql.append(" item_status = '"+item.getItemStatus()+"',");
        }
        if(item.getScore() != null && item.getScore().intValue() != 0){
            sql.append(" score = "+item.getScore()+",");
        }
        if(StringUtils.isNotBlank(item.getDescription())){
            sql.append(" description = '"+item.getDescription()+"',");
        }
        if(StringUtils.isNotBlank(item.getItemType())){
            sql.append(" item_type = '"+item.getItemType()+"',");
        }
        if(item.getUseCount() != 0){
            sql.append(" use_count = "+item.getUseCount()+",");
        }
        if(item.isRecommend()){
            sql.append(" recommend = "+item.isRecommend()+",");
        }
        if(StringUtils.isNotBlank(item.getNotPassReason())){
            sql.append(" notPassReason = '"+item.getNotPassReason()+"',");
        }
        if (sql.lastIndexOf(",") + 1 == sql.length()) {
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
        sql.append(" where item_id='" + item.getItemId() + "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return Contants.SUCCESS;
        }else{
            return "FAILED";
        }
    }

    @Override
    public Item findItemByItemId(String itemId) {

        StringBuilder reviewSql = new StringBuilder("");
        reviewSql.append("select * from t_item where item_id = ?");
        if(StringUtils.isNotBlank(itemId)){
            List<String> excludeFields = new ArrayList<>();
            excludeFields.add("remindCount");
            List<Item> itemList = getJdbcTemplate().query(reviewSql.toString(),new Object[]{itemId},new ItemMapper(excludeFields));
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
        //TODO sql need optimize ,two full table scan
        reviewSql.append("select i.*,count(r.remind_id) remindCount from t_item i left join t_remind r on i.item_id = r.obj_id where 1=1 ");
        if(item != null){
            if (StringUtils.isNotEmpty(item.getTitle())) {
                reviewSql.append(" and i.title ='"+item.getTitle()+"' ");
            }
            if (StringUtils.isNotEmpty(item.getItemStatus())) {
                reviewSql.append(" and i.item_status = '"+item.getItemStatus()+"' ");
            }
            if (StringUtils.isNotEmpty(item.getUserId())) {
                reviewSql.append(" and i.create_user_id = '"+item.getUserId()+"' ");
            }
        }
        reviewSql.append(" group by i.item_id order by i.opt_time desc limit ?,?");
        List<Item> itemList = getJdbcTemplate().query(reviewSql.toString(),new Object[]{startIndex,loadSize},new ItemMapper());

        return itemList;
    }

    public List<Item> findSellItemListByCondition(String condition, int startIndex, int loadSize){
        StringBuilder sql = new StringBuilder("");

        if(condition.equals("recommend")){//推荐
            sql.append("select * from t_item  " +
                    " where recommend = true and item_status = '"+ Contants.ITEM_STATUS.for_sale+"' order by opt_time desc");
        }else if(condition.equals("new")){//最新
            sql.append("select * from t_item where item_status = '"+ Contants.ITEM_STATUS.for_sale+"' order by opt_time desc");
        }else if(condition.equals("hot")){//最火
            sql.append("SELECT i.*,count(o.order_id) c FROM `t_item` i ,t_order o where i.item_id = o.item_id and i.item_status = '"+ Contants.ITEM_STATUS.for_sale+"' order by c desc");
        }else if(condition.equals("good")){//最优
            sql.append("SELECT i.*,sum(f.rating) c FROM `t_item` i ,t_feed_back f where i.item_id = f.item_id and i.item_status = '"+ Contants.ITEM_STATUS.for_sale+"' order by c desc");
        }

        List<String> excludeFields = new ArrayList<>();
        excludeFields.add("remindCount");
        sql.append(" limit ?,?");
        List<Item> itemList = getJdbcTemplate().query(sql.toString(),new Object[]{startIndex,loadSize},new ItemMapper(excludeFields));
        return itemList;
    }

    @Override
    public List<Item> findItemList(Item item, int startIndex, int loadSize) {
        StringBuilder reviewSql = new StringBuilder("");

        reviewSql.append("select i.* from t_item i where 1=1 ");
        if(item != null){

            if (StringUtils.isNotEmpty(item.getItemStatus())) {
                reviewSql.append(" and i.item_status = '"+item.getItemStatus()+"' ");
            }
            if (StringUtils.isNotEmpty(item.getUserId())) {
                reviewSql.append(" and i.create_user_id = '"+item.getUserId()+"' ");
            }
            if (StringUtils.isNotEmpty(item.getCreateUserName()) && StringUtils.isNotEmpty(item.getTitle())) {
                reviewSql.append(" and (i.create_user_name like '%"+item.getCreateUserName()+"%' or i.title like '%"+item.getTitle()+"%')");
            }
        }
        reviewSql.append("  order by i.opt_time desc limit ?,?");
        List<String> excludeFields = new ArrayList<>();
        excludeFields.add("remindCount");
        List<Item> itemList = getJdbcTemplate().query(reviewSql.toString(),new Object[]{startIndex,loadSize},new ItemMapper(excludeFields));

        return itemList;
    }
    @Override
    public int findItemCount(Item item) {
        StringBuilder countSql = new StringBuilder(
                "select count(*) from t_item where 1=1 ");
        if (StringUtils.isNotEmpty(item.getItemStatus())) {
            countSql.append(" and i.item_status = '"+item.getItemStatus()+"' ");
        }
        return getJdbcTemplate().queryForObject(countSql.toString(), Integer.class);
    }

    public class ItemMapper implements RowMapper<Item>{

        private List<String> excludeField = new ArrayList<>();

        public ItemMapper(){
        }

        public ItemMapper(List<String> excludeFields){
            this.excludeField = excludeFields;
        }

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
            item.setRecommend(rs.getBoolean("recommend"));
            item.setDuration(rs.getInt("duration"));
            item.setNotPassReason(rs.getString("notPassReason"));
            if(!excludeField.contains("remindCount")){
                item.setRemindCount(rs.getInt("remindCount"));
            }

            return item;
        }
    }
    public class ItemDTOMapper implements RowMapper<ItemDTO>{

        @Override
        public ItemDTO mapRow(ResultSet rs, int i) throws SQLException {
            ItemDTO itemDTO = new ItemDTO();
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
            item.setRecommend(rs.getBoolean("recommend"));
            item.setDuration(rs.getInt("duration"));
            item.setNotPassReason(rs.getString("notPassReason"));
            itemDTO.setItem(item);
            itemDTO.setImgPath(rs.getString("image_url"));
            itemDTO.setImgType(rs.getString("image_type"));
            return itemDTO;
        }
    }
}
