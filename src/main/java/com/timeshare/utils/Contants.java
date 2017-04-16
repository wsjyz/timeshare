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

    String FAILED = "failed";

    String DOMAIN = "jk.zhangqidong.cn";

    String FILE_SAVE_PATH = "/work/appfile";

    //月最大提现次数
    int MONTH_WITHDRAWAL_MAX_NUMBER=4;

    enum ITEM_STATUS {
        draft,for_sale,
        undercarriage,//下架
        apply_for_online,//待审核
        not_pass//审核未通过
    }
    enum BID_STATUS {
        draft,ongoing,
        stopped,//后台终止
        finish,//已完成
    }
    enum BID_SUBMIT_TYPE {
        IMAGE,VOICE,TEXT
    }
    enum ITEM_TYPE {
        tel,meet
    }
    enum IMAGE_TYPE {
        ITEM_SHOW_IMG,USER_HEAD,
        ASSEMBLY_SHOW_IMG,//活动封面
        ASSEMBLY_CONTENT_IMG,//活动内容图片
        ASSEMBLY_COMMENT_IMG,
        ASSEMBLY_CONSULTATION_IMG,
        CROWD_FUNDING_IMG//众筹项目 众筹封面
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
    enum PAGE_CONTENT_TYPE {
        mysubmit,//我接的飚
        mybid,//我发的飚
        myaudit//我旁听的
    }
    enum ACOUNT_TYPE {
        VIP
    }
    //众筹 费用类型
    enum COST_TYPE {
        AA,//费用均摊
        FIXED_PRICE//一口价
    }
    //众筹 是否在轮播图展示
    enum CROWD_FUNDING_IS_SHOW {
        YES,
        NO
    }
    //众筹项目 状态
    enum CROWD_FUNDING_STATUS {
        SKETCH,//草稿
        RELEASED,//已发布
        OFF_SHELVE,//已下架
    }
    //预约 发票类型
    enum ENROLL_INVOICE_TYPE {
        VAT_INVOICE,//增税普票
        VAT_SPECIAL_INVOICE//增税专票
    }
    //预约 支付状态
    enum ENROLL_PAY_STATUS {
        WAIT_PAY,//未支付
        PAYED,//已支付
        REFUND//已退款
    }
    //现金提现 状态
    enum WITHDRAWAL_STATUS {
        PENDING,//审批中
        SUCCESS,//已发放
        REJECT//已拒绝
    }
    //是否已经转移到了现金账户
    enum IS_TRANSFER_CASH_ACCOUNT {
        YES,
        NO
    }
    //评论对象类型
    enum COMMENT_OBJ_TYPE {
        ASSEMBLY,
        CROWDFUNDING
    }


}
