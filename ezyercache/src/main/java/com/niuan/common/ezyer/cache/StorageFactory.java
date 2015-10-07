package com.niuan.common.ezyer.cache;

import android.content.Context;

import com.niuan.common.ezyer.util.HardwareUtil;

public class StorageFactory {
    public static FileStorage getFileStorage(Context context) {
        return (context == null || HardwareUtil.isSdCardExists()) ? new SDCache() : new InnerCache(context);
    }
}
