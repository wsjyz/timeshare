package com.timeshare.domain;

import com.timeshare.domain.annotation.Column;
import com.timeshare.domain.annotation.Table;

import java.math.BigDecimal;
import java.util.List;

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
    private String description;
    private String position;//职位
    private String corp;//任职机构
    private String industry;//行业
    private String city;
    private String ageGroup;//年龄段
    /**
     * 展示图片
     */
    private ImageObj imageObj;

    //数据传输用
    private List<Item> userItemList;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCorp() {
        return corp;
    }

    public void setCorp(String corp) {
        this.corp = corp;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public List<Item> getUserItemList() {
        return userItemList;
    }

    public void setUserItemList(List<Item> userItemList) {
        this.userItemList = userItemList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageObj getImageObj() {
        return imageObj;
    }

    public void setImageObj(ImageObj imageObj) {
        this.imageObj = imageObj;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getSumCost() {
        return sumCost;
    }

    public void setSumCost(BigDecimal sumCost) {
        this.sumCost = sumCost;
    }

    public int getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(int sellCounts) {
        this.sellCounts = sellCounts;
    }

    public int getBuyCounts() {
        return buyCounts;
    }

    public void setBuyCounts(int buyCounts) {
        this.buyCounts = buyCounts;
    }
}
