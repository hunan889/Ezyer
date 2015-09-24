package com.niuan.common.ezyer.util;

import android.os.Environment;

/**
 * Created by Carlos on 2015/9/24.
 */
public class HardwareUtil {
    public static boolean isSdCardExists() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}
