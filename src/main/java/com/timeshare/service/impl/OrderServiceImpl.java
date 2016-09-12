package com.timeshare.service.impl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.timeshare.dao.OrderDAO;
import com.timeshare.dao.impl.OrderDTO;
import com.timeshare.domain.*;
import com.timeshare.service.ItemService;
import com.timeshare.service.OrderService;
import com.timeshare.service.RemindService;
import com.timeshare.service.UserService;
import com.timeshare.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by user on 2016/7/1.
 */
@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    protected Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    protected Logger payLogger = LoggerFactory.getLogger("payLogger");

    @Autowired
    OrderDAO orderDAO;
    @Autowired
    ItemService itemService;
    @Autowired
    RemindService remindService;
    @Autowired
    UserService userService;

    @Override
    public String saveOrder(ItemOrder order) {

        String result = "";
        Item item = itemService.findItemByItemId(order.getItemId());
        UserInfo buyer = userService.findUserByUserId(order.getUserId());
        UserInfo seller = userService.findUserByUserId(item.getUserId());
        ItemOrder dbOrder = orderDAO.findOrderByOrderId(order.getOrderId());
        switch (order.getOrderStatus()){
            case "BEGIN":

                order.setPrice(item.getPrice());
                order.setItemTitle(item.getTitle());
                order.setOrderUserId(item.getUserId());
                String orderId = CommonStringUtils.gen18RandomNumber();
                order.setOrderId(orderId);
                Remind remind = new Remind();
                remind.setObjId(order.getOrderId());
                remind.setRemindType(Contants.REMIND_TYPE.ORDER_SELLER.toString());
                remind.setToUserId(item.getUserId());
                remind.setUserId(item.getUserId());

                remindService.saveRemind(remind);
                result = orderDAO.saveOrder(order);


                buyer.setBuyCounts(buyer.getBuyCounts() + 1);
                userService.modifyUser(buyer);
                seller.setSellCounts(seller.getSellCounts() + 1);
                userService.modifyUser(seller);
            break;
            case "SELLER_APPLY":
                Remind sellerApplyRemind = new Remind();
                sellerApplyRemind.setObjId(order.getOrderId());
                sellerApplyRemind.setRemindType(Contants.REMIND_TYPE.ORDER_BUYER.toString());
                sellerApplyRemind.setToUserId(order.getUserId());
                sellerApplyRemind.setUserId(item.getUserId());
                remindService.saveRemind(sellerApplyRemind);
                result = orderDAO.modifyOrder(order);
                break;
            case "BUYER_CONFIRM":

                Remind buyConfirmRemind = new Remind();
                buyConfirmRemind.setObjId(order.getOrderId());
                buyConfirmRemind.setRemindType(Contants.REMIND_TYPE.ORDER_SELLER.toString());
                buyConfirmRemind.setToUserId(item.getUserId());
                buyConfirmRemind.setUserId(order.getUserId());
                remindService.saveRemind(buyConfirmRemind);
                result = orderDAO.modifyOrder(order);
                break;
            case "SELLER_FINISH":
                result = orderDAO.modifyOrder(order);
                Remind sellerApplyRemind1 = new Remind();
                sellerApplyRemind1.setObjId(order.getOrderId());
                sellerApplyRemind1.setRemindType(Contants.REMIND_TYPE.ORDER_BUYER.toString());
                sellerApplyRemind1.setToUserId(order.getUserId());
                sellerApplyRemind1.setUserId(item.getUserId());
                remindService.saveRemind(sellerApplyRemind1);

                break;
            case "BUYLLER_FINISH":
                result = orderDAO.modifyOrder(order);
                Remind buyConfirmRemind2 = new Remind();
                buyConfirmRemind2.setObjId(order.getOrderId());
                buyConfirmRemind2.setRemindType(Contants.REMIND_TYPE.ORDER_SELLER.toString());
                buyConfirmRemind2.setToUserId(item.getUserId());
                buyConfirmRemind2.setUserId(order.getUserId());
                remindService.saveRemind(buyConfirmRemind2);
                if(order.getOptUserType().equals("buyer") && order.isBuyerPayed()){
                    payToSeller(order.getOrderId());//付款
                    //修改收入和支出
                    seller.setIncome(seller.getIncome().add(dbOrder.getPrice()));
                    buyer.setSumCost(buyer.getSumCost().add(dbOrder.getPrice()));
                    userService.modifyUser(buyer);
                    userService.modifyUser(seller);
                }
                break;
            case "FINISH":
                result = orderDAO.modifyOrder(order);
                if(order.getOptUserType().equals("buyer") && order.isBuyerPayed()){
                    payToSeller(order.getOrderId());//付款
                    //修改收入和支出
                    seller.setIncome(seller.getIncome().add(dbOrder.getPrice()));
                    buyer.setSumCost(buyer.getSumCost().add(dbOrder.getPrice()));
                    userService.modifyUser(seller);
                    userService.modifyUser(buyer);
                }
                break;
        }
        if(!order.getOrderStatus().equals("FINISH")){
            sendMms(order);
        }



        return result;
    }

    private void payToSeller(String orderId){

        OrderDTO orderDTO = orderDAO.findPayOrderByOrderId(orderId);
        payLogger.info("处理订单:,"+orderDTO.getOrder().getOrderId()+",付款价格为,"+orderDTO.getOrder().getPrice());
        WxPayParamsBean xmlParams = new WxPayParamsBean();
        SortedMap parameters = new TreeMap<>();
        xmlParams.setMch_appid(Contants.APPID);
        parameters.put("mch_appid",Contants.APPID);

        xmlParams.setMchid(Contants.MCHID);
        parameters.put("mchid",Contants.MCHID);

        String noceStr = CommonStringUtils.genPK();
        xmlParams.setNonce_str(noceStr);
        parameters.put("nonce_str",noceStr);

        xmlParams.setPartner_trade_no(orderDTO.getOrder().getWxTradeNo());
        parameters.put("partner_trade_no",orderDTO.getOrder().getWxTradeNo());

        xmlParams.setOpenid(orderDTO.getOpenId());
        parameters.put("openid",orderDTO.getOpenId());

        xmlParams.setCheck_name("NO_CHECK");
        parameters.put("check_name","NO_CHECK");

        int amount = payAmount(orderDTO.getOrder().getPrice(),new BigDecimal(0.006));
        xmlParams.setAmount(amount);
        parameters.put("amount",amount);

        xmlParams.setDesc("佣金");
        parameters.put("desc","佣金");

        xmlParams.setSpbill_create_ip("115.159.30.163");
        parameters.put("spbill_create_ip","115.159.30.163");

        String sign = WxUtils.createSign(parameters,Contants.KEY);
        xmlParams.setSign(sign);
        XStream xs = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xs.processAnnotations(xmlParams.getClass());
        String xml = xs.toXML(xmlParams);
        logger.info("xml is "+xml);

        HTTPSClient client = new HTTPSClient();
        try {
            client.setBodyParams(new String(xml.getBytes("UTF-8"),"ISO-8859-1"));//狗日的微信，神经病
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String p12FilePath = "/work/cert/apiclient_cert.p12";
        String postUri = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
        String response = client.httpsRequest(p12FilePath,Contants.MCHID,postUri);

        //logger.info("调用返回:"+response);//微信变态啊，这个返回不需要转码

        WxPayResponseBean bean = new WxPayResponseBean();
        xs.processAnnotations(bean.getClass());
        bean = (WxPayResponseBean)xs.fromXML(response);
        if(bean != null){
            if(bean.getReturn_code().equals("SUCCESS") && bean.getResult_code().equals("SUCCESS")){
                //更新数据库
                logger.info(orderDTO.getOrder().getOrderId()+"付款成功");
            }else{
                //打印错误日志
                logger.info(orderDTO.getOrder().getOrderId()+"付款失败："+bean.getErr_code_des());
            }
        }
    }

    @Override
    public String modifyOrder(ItemOrder itemOrder) {
        return null;
    }

    @Override
    public ItemOrder findOrderByOrderId(String orderId) {
        return orderDAO.findOrderByOrderId(orderId);
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
        return orderDAO.findItemPage(order,startIndex,loadSize);
    }

    @Override
    public BigDecimal findUsersMoneyByType(String userId, String type) {
        return orderDAO.findUsersMoneyByType(userId,type);
    }
    public void sendMms(ItemOrder order){
//        卖家收到：买家[李四]预约了您的项目，请进入服务号[邂逅时刻]查看
//        买家收到：卖家[张三]答复了您的邀请，请进入服务号[邂逅时刻]查看
//        卖家收到：买家[李四]确认了邂逅时间，具体沟通时间为[2016-12-10 12:00]，请进入服务号[邂逅时刻]查看
//        买家收到：卖家[张三]已经确认完成双方邀约，请进入服务号[邂逅时刻]查看
//        卖家收到：买家[李四]已经确认完成双方邀约，项目款项已入账，请进入服务号[邂逅时刻]查收
        UserInfo seller = userService.findUserByUserId(order.getOrderUserId());
        UserInfo buyer = userService.findUserByUserId(order.getUserId());
        SmsContentBean bean = new SmsContentBean();
        switch (order.getOrderStatus()){
            case "BEGIN":
                bean.setTemplateCode("SMS_14730465");
                bean.setToMobile(seller.getMobile());
                bean.setContent("{\"name\":\""+buyer.getNickName()+"\"}");
                System.out.println("向卖家"+seller.getMobile()+"发短信："+"买家["+buyer.getNickName()+"]预约了您的项目，请进入服务号[邂逅时刻]查看");
                break;
            case "SELLER_APPLY":
                bean.setTemplateCode("SMS_14740368");
                bean.setToMobile(buyer.getMobile());
                bean.setContent("{\"name\":\""+seller.getNickName()+"\"}");
                System.out.println("向买家"+buyer.getMobile()+"发短信："+"卖家["+seller.getNickName()+"]答复了您的邀请，请进入服务号[邂逅时刻]查看");
                break;
            case "BUYER_CONFIRM":
                bean.setTemplateCode("SMS_14725333");
                bean.setToMobile(seller.getMobile());
                SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日 HH点mm");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = null;
                try {
                    date = sdf1.parse(order.getFinalAppointmentTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String finalAppointmentTime = sdf.format(date);
                bean.setContent("{\"name\":\""+buyer.getNickName()+"\",\"time\":\""+finalAppointmentTime+"\"}");
                System.out.println("向卖家"+seller.getMobile()+"发短信："+"买家["+buyer.getNickName()+"]确认了邂逅时间，具体沟通时间为["+finalAppointmentTime+"]，请进入服务号[邂逅时刻]查看");
                break;
            case "SELLER_FINISH":
                bean.setTemplateCode("SMS_14760340");
                bean.setToMobile(buyer.getMobile());
                bean.setContent("{\"name\":\""+seller.getNickName()+"\"}");
                System.out.println("向买家"+buyer.getMobile()+"发短信："+"卖家["+seller.getNickName()+"]已经确认完成双方邀约，请进入服务号[邂逅时刻]查看");
                break;
            case "BUYLLER_FINISH":
                bean.setTemplateCode("SMS_14735327");
                bean.setToMobile(seller.getMobile());
                bean.setContent("{\"name\":\""+buyer.getNickName()+"\"}");
                System.out.println("向卖家"+seller.getMobile()+"发短信："+"买家["+buyer.getNickName()+"]已经确认完成双方邀约，项目款项已入账，请进入服务号[邂逅时刻]查收");
                break;


        }
        String response = SmsUtils.senMessage(bean);
        if(response.indexOf("error_response") != -1){
            logger.error(response);
        }
    }
    /**
     * 返回最终付款的金额（单位：分）：原始价格减去手续费四舍五入保留2位小数
     * @param originalAmount 原始价格
     * @param commission 手续费率
     * @return
     */
    public int payAmount(BigDecimal originalAmount,BigDecimal commission){
        //计算手续费
        BigDecimal commissionBig = originalAmount.multiply(commission);
        commissionBig = commissionBig.setScale(2,BigDecimal.ROUND_HALF_UP);
        //计算最终付款
        BigDecimal finalAmountYuan = originalAmount.subtract(commissionBig);
        finalAmountYuan = finalAmountYuan.setScale(2,BigDecimal.ROUND_HALF_UP);
        payLogger.info("扣除手续费,"+commissionBig.toString()+",元最终付款,"+finalAmountYuan.toString());
        return finalAmountYuan.multiply(new BigDecimal(100)).intValue();
    }
}

