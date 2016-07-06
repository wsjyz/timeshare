package com.timeshare.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by user on 2016/7/6.
 */
public class WxUtils {

    public static String createSign(SortedMap parameters,String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = entry.getValue().toString();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);


        System.out.println("sign "+sb.toString());
        String sign = CommonStringUtils.MD5(sb.toString(),"utf-8");

        return sign;

    }
}
