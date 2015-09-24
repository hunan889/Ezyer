package com.niuan.common.ezyer.util;

/**
 * Created by Carlos on 2015/9/24.
 */
public class LogUtils {


    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        return android.util.Log.getStackTraceString(tr);

        // return tr.getMessage();
    }
}
