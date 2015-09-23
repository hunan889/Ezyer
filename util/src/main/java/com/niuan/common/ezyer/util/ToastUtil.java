package com.niuan.common.ezyer.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast mToast = null;
    private static Context sContext;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(sContext, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(sContext, resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

}
