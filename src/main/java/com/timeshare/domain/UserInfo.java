package com.timeshare.domain;

import com.timeshare.domain.annotation.Column;
import com.timeshare.domain.annotation.Table;

import java.math.BigDecimal;

/**
 * Created by adam on 2016/6/7.
 */
@Table(name = "t_user_info", comment = "会员")
public class UserInfo {

    @Column(name = "user_id", pk = true, length = 32)
    private String userId;
    @Column(name = "mobile", length = 15, comment = "手机号")
    private String mobile;// 手机号
    @Column(name = "user_name", length = 20, comment = "姓名")
    private String userName;// 姓名
    @Column(name = "nick_name", length = 20, comment = "昵称")
    private String nickName;// 昵称
    @Column(name = "income", length = 8, decimal = 2, comment = "收入")
    private BigDecimal income;
    @Column(name = "income", length = 8, decimal = 2, comment = "支出")
    private BigDecimal sumCost;
    @Column(name = "sell_counts", length = 5, comment = "售卖数量")
    private int sellCounts;
    @Column(name = "buy_counts", length = 5, comment = "购买数量")
    private int buyCounts;
}
