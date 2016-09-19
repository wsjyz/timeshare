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
        StringBuilder sql = new StringBuilder("insert into t_item " +
                "(item_id,title,price,score,description,item_type,use_count,create_user_id,opt_time,create_user_name,item_status,recommend,duration,practice_description,practice_time,item_to_object,item_value,item_catalog)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, id);
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
                ps.setString(12,info.getRecommend());
                ps.setInt(13,info.getDuration());
                ps.setString(14,info.getPracticeDescription());
                ps.setString(15,info.getPracticeTime());
                ps.setString(16,info.getItemToObject());
                ps.setString(17,info.getItemValue());
                ps.setString(18,info.getItemCatalog());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
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
        if(StringUtils.isNotBlank(item.getRecommend())){
            sql.append(" recommend = '"+item.getRecommend()+"',");
        }
        if(StringUtils.isNotBlank(item.getNotPassReason())){
            sql.append(" notPassReason = '"+item.getNotPassReason()+"',");
        }
        if(StringUtils.isNotBlank(item.getPracticeDescription())){
            sql.append(" practice_description = '"+item.getPracticeDescription()+"',");
        }
        if(StringUtils.isNotBlank(item.getPracticeTime())){
            sql.append(" practice_time = '"+item.getPracticeTime()+"',");
        }
        if(StringUtils.isNotBlank(item.getItemToObject())){
            sql.append(" item_to_object = '"+item.getItemToObject()+"',");
        }
        if(StringUtils.isNotBlank(item.getItemValue())){
            sql.append(" item_value = '"+item.getItemValue()+"',");
        }
        if(StringUtils.isNotBlank(item.getItemCatalog())){
            sql.append(" item_catalog = '"+item.getItemCatalog()+"',");
        }
        if (sql.lastIndexOf(",") + 1 == sql.length()) {
            sql.delete(sql.lastIndexOf(","), sql.length());
        }

        sql.append(" where item_id='" + item.getItemId() + "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return item.getItemId();
        }else{
            return Contants.FAILED;
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

    /**
     * 我卖的
     * @param item
     * @param startIndex
     * @param loadSize
     * @return
     */
    @Override
    public List<Item> findItemPage(Item item, int startIndex, int loadSize) {
        StringBuilder reviewSql = new StringBuilder("");
        //TODO sql need optimize ,two full table scan
        reviewSql.append("select " +
                "i.item_id,i.title,i.price,i.score,i.item_type,i.duration,i.item_status,i.use_count,i.recommend,i.create_user_id,i.opt_time,i.create_user_name,count(r.remind_id) remindCount " +
                "from t_item i left join t_remind r on i.item_id = r.obj_id where 1=1 ");
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

    /**
     * 首页的tab页用到的
     * @param condition
     * @param startIndex
     * @param loadSize
     * @return
     */
    public List<Item> findSellItemListByCondition(String condition, int startIndex, int loadSize){
        StringBuilder sql = new StringBuilder("");

        if(condition.equals("recommend")){//推荐
            sql.append("select item_id,title,price,score,item_type,duration,item_status,use_count,recommend,create_user_id,opt_time,create_user_name from t_item  " +
                    " where recommend = 'true' and item_status = '"+ Contants.ITEM_STATUS.for_sale+"' order by opt_time desc");
        }else if(condition.equals("new")){//最新
            sql.append("select item_id,title,price,score,item_type,duration,item_status,use_count,recommend,create_user_id,opt_time,create_user_name from t_item where item_status = '"+ Contants.ITEM_STATUS.for_sale+"' order by opt_time desc");
        }else if(condition.equals("hot")){//最火
            sql.append("SELECT item_id,title,price,score,item_type,duration,item_status,use_count,recommend,create_user_id,opt_time,create_user_name FROM `t_item`  where  item_status = '"+ Contants.ITEM_STATUS.for_sale+"' and use_count > 0 order by use_count desc");
        }else if(condition.equals("good")){//最优
            sql.append("SELECT item_id,title,price,score,item_type,duration,item_status,use_count,recommend,create_user_id,opt_time,create_user_name FROM `t_item`  where  item_status = '"+ Contants.ITEM_STATUS.for_sale+"' and score > 0 order by score desc");
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

        reviewSql.append("select i.item_id,i.title,i.price,i.score,i.item_type,i.duration,i.item_status,i.use_count,i.recommend,i.create_user_id,i.opt_time,i.create_user_name from t_item i where 1=1 ");
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
            countSql.append(" and item_status = '"+item.getItemStatus()+"' ");
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
            if(doesColumnExist("description",rs)){
                item.setDescription(rs.getString("description"));
            }
            item.setItemStatus(rs.getString("item_status"));
            item.setPrice(rs.getBigDecimal("price"));
            item.setScore(rs.getBigDecimal("score"));
            item.setItemType(rs.getString("item_type"));
            item.setTitle(rs.getString("title"));
            item.setUseCount(rs.getInt("use_count"));
            item.setRecommend(rs.getString("recommend"));
            item.setDuration(rs.getInt("duration"));
            if(doesColumnExist("notPassReason",rs)){
                item.setNotPassReason(rs.getString("notPassReason"));
            }
            if(doesColumnExist("practice_description",rs)){
                item.setPracticeDescription(rs.getString("practice_description"));
            }

            if(doesColumnExist("practice_time",rs)){
                item.setPracticeTime(rs.getString("practice_time"));
            }

            if(doesColumnExist("item_to_object",rs)){
                item.setItemToObject(rs.getString("item_to_object"));
            }

            if(doesColumnExist("item_value",rs)){
                item.setItemValue(rs.getString("item_value"));
            }

            if(doesColumnExist("item_catalog",rs)){
                item.setItemCatalog(rs.getString("item_catalog"));
            }

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
            if(doesColumnExist("description",rs)){
                item.setDescription(rs.getString("description"));
            }
            item.setItemStatus(rs.getString("item_status"));
            item.setPrice(rs.getBigDecimal("price"));
            item.setScore(rs.getBigDecimal("score"));
            item.setItemType(rs.getString("item_type"));
            item.setTitle(rs.getString("title"));
            item.setUseCount(rs.getInt("use_count"));
            item.setRecommend(rs.getString("recommend"));
            item.setDuration(rs.getInt("duration"));
            if(doesColumnExist("notPassReason",rs)){
                item.setNotPassReason(rs.getString("notPassReason"));
            }

            if(doesColumnExist("practice_description",rs)){
                item.setPracticeDescription(rs.getString("practice_description"));
            }

            if(doesColumnExist("practice_time",rs)){
                item.setPracticeTime(rs.getString("practice_time"));
            }

            if(doesColumnExist("item_to_object",rs)){
                item.setItemToObject(rs.getString("item_to_object"));
            }

            if(doesColumnExist("item_value",rs)){
                item.setItemValue(rs.getString("item_value"));
            }

            if(doesColumnExist("item_catalog",rs)){
                item.setItemCatalog(rs.getString("item_catalog"));
            }
            itemDTO.setItem(item);
            itemDTO.setImgPath(rs.getString("image_url"));
            itemDTO.setImgType(rs.getString("image_type"));
            return itemDTO;
        }
    }
}
