package com.timeshare.utils;

public interface Contants {

    String APPID = "wxcd8903dd6a9552eb";

    String SECRET = "a172670e10ca24f85c3f2aa024d8dd99";

    String CORPID = "wxfb5ee1afc560a3be";

    String CORPSECRET = "y29rsSXWXEShXkmyBx8mskVSNonvSqvqzQWqMWGbRIXtMkiWxbKRVLRDYqykB2tI";

    String ITEM_SHOW_IMG = "ITEM_SHOW_IMG";

    String SUCCESS = "success";

    enum ITEM_STATUS {
        draft,for_sale,undercarriage
    }
    enum ITEM_TYPE {
        tel,meet
    }
    enum IMAGE_TYPE {
        ITEM_SHOW_IMG
    }
    enum ORDER_TYPE {
        ONLINE,OFFLINE
    }
    enum ORDER_STATUS {
        BEGIN,//买家填写问题，发出邀请
        SELLER_APPLY,//卖家答复，填写建议时间
        BUYER_CONFIRM,//买家确认，选择付款方式，选择最终见面时间
        SELLER_CONFIRM,//卖家确认
        SELLER_FINISH,//卖家完成
        BUYLLER_FINISH,//买家完成
        ONLINE_PAID,//已在线支付
        NEED_RATED,//待评价
        FINISH,//已完成
    }
    enum REMIND_TYPE {
        ORDER
    }
}
