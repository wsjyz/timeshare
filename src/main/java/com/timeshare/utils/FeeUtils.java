package com.timeshare.utils;

import java.math.BigDecimal;

/**
 * Created by user on 2016/10/12.
 */
public class FeeUtils {

    /**
     * 返回最终付款的金额（单位：分）：原始价格减去手续费四舍五入保留2位小数
     * @param originalAmount 原始价格
     * @param commission 费率
     * @return
     */
    public static int payAmount(BigDecimal originalAmount, BigDecimal commission){
        //计算费率
        BigDecimal commissionBig = originalAmount.multiply(commission);
        commissionBig = commissionBig.setScale(2,BigDecimal.ROUND_HALF_UP);
        //计算最终付款
        BigDecimal finalAmountYuan = originalAmount.subtract(commissionBig);
        finalAmountYuan = finalAmountYuan.setScale(2,BigDecimal.ROUND_HALF_UP);
        return finalAmountYuan.multiply(new BigDecimal(100)).intValue();
    }

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal("40.00");
        BigDecimal b = new BigDecimal("10.00").multiply(new BigDecimal("0.3"));
        BigDecimal c = a.add(new BigDecimal("10").multiply(new BigDecimal("0.15")));

        System.out.println(c);
    }

}
