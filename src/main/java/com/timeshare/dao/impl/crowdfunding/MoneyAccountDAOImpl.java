package com.timeshare.dao.impl.crowdfunding;

import com.timeshare.dao.BaseDAO;
import com.timeshare.dao.crowdfunding.MoneyAccountDAO;
import com.timeshare.domain.crowdfunding.MoneyAccount;
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
@Repository("MoneyAccountDAO")
public class MoneyAccountDAOImpl extends BaseDAO implements MoneyAccountDAO {
    @Override
    public String saveMoneyAccount(MoneyAccount MoneyAccount) {
        StringBuilder sql = new StringBuilder("INSERT INTO t_money_account (money_account_id, cash_withdrawal_amount, cash_raised_amount, month_withdrawal_number, user_id) VALUES (?, ?, ?, ?, ?);");
        final String id = CommonStringUtils.genPK();
        int result = getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1,id);
                ps.setBigDecimal(2,MoneyAccount.getCashWithdrawalAmount());
                ps.setBigDecimal(3,MoneyAccount.getCashRaisedAmount());
                ps.setInt(4,MoneyAccount.getMonthWithdrawalNumber());
                ps.setString(5,MoneyAccount.getUserId());
            }
        });
        if(result > 0){
            return id;
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public String updateMoneyAccount(MoneyAccount moneyAccount) {
        StringBuilder sql = new StringBuilder("update t_money_account set ");
        if(moneyAccount.getCashWithdrawalAmount()!=null){
            sql.append(" cash_withdrawal_amount = cash_withdrawal_amount+"+moneyAccount.getCashWithdrawalAmount()+",");
        }
        if(moneyAccount.getCashRaisedAmount()!=null){
            sql.append(" cash_raised_amount = cash_raised_amount+"+moneyAccount.getCashRaisedAmount()+",");
        }
        if(moneyAccount.getMonthWithdrawalNumber()!=null && moneyAccount.getMonthWithdrawalNumber()!=0){
            sql.append(" month_withdrawal_number = month_withdrawal_number+"+moneyAccount.getMonthWithdrawalNumber()+",");
        }
        if (sql.lastIndexOf(",") + 1 == sql.length()) {
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
        sql.append(" where user_id='" + moneyAccount.getUserId()+ "'");
        int result = getJdbcTemplate().update(sql.toString());
        if(result > 0){
            return moneyAccount.getUserId();
        }else{
            return Contants.FAILED;
        }
    }

    @Override
    public List<MoneyAccount> findMoneyAccountByOwner(String userId) {
        StringBuilder sql = new StringBuilder("select * from t_money_account where user_id='"+userId+"'");
        return getJdbcTemplate().query(sql.toString(),new Object[]{},new MoneyAccountRowMapper());
    }


    class MoneyAccountRowMapper implements RowMapper<MoneyAccount>{
        @Override
        public MoneyAccount mapRow(ResultSet rs, int i) throws SQLException {
            MoneyAccount MoneyAccount = new MoneyAccount();
            MoneyAccount.setMoneyAccountId(rs.getString("money_account_id"));
            MoneyAccount.setCashWithdrawalAmount(rs.getBigDecimal("cash_withdrawal_amount"));
            MoneyAccount.setCashRaisedAmount(rs.getBigDecimal("cash_raised_amount"));
            MoneyAccount.setMonthWithdrawalNumber(rs.getInt("month_withdrawal_number"));
            MoneyAccount.setUserId(rs.getString("user_id"));
            return MoneyAccount;
        }
    }
}