package com.timeshare.utils;

public interface Contants {

    String APPID = "wxcd8903dd6a9552eb";

    String SECRET = "4fde45504987eb0b54feeca4b9173622";

    String MCHID = "1358876502";

    String KEY = "5bab4e2c09194e6198fe351a04991949";

    String DEVICEINFO = "WEB";

    String CORPID = "wxfb5ee1afc560a3be";

    String CORPSECRET = "y29rsSXWXEShXkmyBx8mskVSNonvSqvqzQWqMWGbRIXtMkiWxbKRVLRDYqykB2tI";

    String ITEM_SHOW_IMG = "ITEM_SHOW_IMG";

    String SUCCESS = "success";

    String DOMAIN = "jk.zhangqidong.cn";

    enum ITEM_STATUS {
        draft,for_sale,
        undercarriage,//下架
        apply_for_online,//待审核
        not_pass//审核未通过
    }
    enum ITEM_TYPE {
        tel,meet
    }
    enum IMAGE_TYPE {
        ITEM_SHOW_IMG,USER_HEAD
    }
    enum ORDER_TYPE {
        ONLINE,OFFLINE
    }
    enum OPT_USER_TYPE {
        buyer,seller
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
        ORDER_BUYER,ORDER_SELLER
    }
}
