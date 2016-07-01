package com.timeshare.dao.impl;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.OrderDAO;
import com.timeshare.domain.ItemOrder;
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
import java.util.List;

/**
 * Created by user on 2016/7/1.
 */
@Repository("OrderDAO")
public class OrderDAOImpl extends BaseDAO implements OrderDAO {

    @Override
    public String saveOrder(final ItemOrder info) {
        StringBuilder sql = new StringBuilder("insert into t_order (order_id,order_user_id,order_user_name,order_problem," +
                "order_user_description,suggest_appointment_time,final_appointment_time,seller_phone,price,pay_type," +
                "paid_money,order_status,create_user_id,opt_time,item_id,item_title)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, CommonStringUtils.genPK());
                ps.setString(2,info.getOrderUserId());
                ps.setString(3,info.getOrderUserName());
                ps.setString(4,info.getOrderProblem());
                ps.setString(5,info.getOrderUserDescription());
                ps.setString(6,info.getSuggestAppointmentTime());
                ps.setString(7,info.getFinalAppointmentTime());
                ps.setString(8,info.getSellerPhone());
                ps.setBigDecimal(9,info.getPrice());
                ps.setString(10,info.getPayType());
                ps.setBigDecimal(11,info.getPaidMoney());
                ps.setString(12,info.getOrderStatus());
                ps.setString(13,info.getUserId());
                ps.setString(14,info.getOptTime());
                ps.setString(15,info.getItemId());
                ps.setString(16,info.getItemTitle());
            }
        });
        if(result > 0){
            return Contants.SUCCESS;
        }else{
            return "FAILED";
        }
    }

    @Override
    public String modifyOrder(ItemOrder itemOrder) {
        return null;
    }

    @Override
    public ItemOrder findOrderByOrderId(String OrderId) {
        return null;
    }

    @Override
    public String deleteById(String OrderId) {
        return null;
    }

    @Override
    public OpenPage<ItemOrder> findOrderPage(String mobile, String nickName, OpenPage page) {
        return null;
    }
    @Override
    public List<ItemOrder> findItemPage(ItemOrder order, int startIndex, int loadSize) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from t_order where 1=1");
        if(order != null){
            if (StringUtils.isNotEmpty(order.getOrderStatus())) {
                if(order.getOrderStatus().equals("ongoing")){
                    sql.append(" and order_status !='"+Contants.ORDER_STATUS.FINISH+"' or order_status !='"+Contants.ORDER_STATUS.NEED_RATED+"'");
                }
                sql.append(" and order_status ='"+order.getOrderStatus()+"' ");
            }
            if (StringUtils.isNotEmpty(order.getOrderUserId())) {
                sql.append(" and order_user_id = '"+order.getOrderUserId()+"' ");
            }

        }
        sql.append(" order by opt_time desc limit ?,?");
        List<ItemOrder> itemList = getJdbcTemplate().query(sql.toString(),new Object[]{startIndex,loadSize},new OrderMapper());

        return itemList;
    }

    private class OrderMapper implements RowMapper<ItemOrder>{

        @Override
        public ItemOrder mapRow(ResultSet rs, int i) throws SQLException {
            ItemOrder order = new ItemOrder();
            order.setOrderId(rs.getString("order_id"));
            order.setOrderUserId(rs.getString("order_user_id"));
            order.setOrderUserName(rs.getString("order_user_name"));
            order.setOrderProblem(rs.getString("order_problem"));
            order.setOrderUserDescription(rs.getString("order_user_description"));
            order.setSuggestAppointmentTime(rs.getString("suggest_appointment_time"));
            order.setFinalAppointmentTime(rs.getString("final_appointment_time"));
            order.setSellerPhone(rs.getString("seller_phone"));
            order.setPrice(rs.getBigDecimal("price"));
            order.setPayType(rs.getString("pay_type"));
            order.setPaidMoney(rs.getBigDecimal("paid_money"));
            order.setOrderStatus(rs.getString("order_status"));
            order.setUserId(rs.getString("create_user_id"));
            order.setOptTime(rs.getString("opt_time"));
            order.setItemId(rs.getString("item_id"));
            order.setItemTitle(rs.getString("item_title"));
            return order;
        }
    }
}
