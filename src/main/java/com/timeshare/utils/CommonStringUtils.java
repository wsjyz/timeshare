package com.timeshare.utils;

import java.util.UUID;

/**
 * Created by user on 2016/6/21.
 */
public class CommonStringUtils {

    public static String genPK() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
