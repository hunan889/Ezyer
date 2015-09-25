package com.niuan.common.ezyercache.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.niuan.common.ezyer.util.LogUtils;
import com.niuan.common.ezyercache.Constants;
import com.niuan.common.ezyercache.SDCache;

import java.util.Date;

public class DbCard extends Database {

    private static final String LOG_TAG = DbCard.class.getName();

    private static boolean HAVECHECKED = false;

    /**
     * 首先得到数据库的存储路径，检查数据库是否存在，如果存在那么就调用checkUpdate()函数检查数据库的版本，如果不存在那么就调用onCreate()函数重新创建
     */
    public void init() {
        if (null != core && core.isOpen()) {
            return;
        }

        SDCache storage = new SDCache();
        String path = storage.getTempPath();//返回的就是getRootPath();

        if (null == path) {
            Log.e(LOG_TAG, "init|getTempPath failed");

            return;
        }

        boolean exists = storage.fileExsits(Constants.SD_DATABASE_NAME);

        core = SQLiteDatabase.openOrCreateDatabase(path + Constants.SD_DATABASE_NAME, null);

        if (exists) {
            checkUpdate();
        } else {
            onCreate();
        }
    }

    /**
     * 创建数据库里面的表，并插入初始数据。
     */
    private void onCreate() {
        try {
            long now = new Date().getTime();
            long exipre = now + 3600 * 1000 * 24 * 365 * 10;
            core.execSQL("create table ifnot exists page_cache(id varchar(50) primary key, content TEXT, row_create_time INTEGER, row_expire_time INTEGER)");
            core.execSQL("insert into page_cache(id, content, row_create_time, row_expire_time) values(?,?,?,?)", new String[]{Constants.SD_DATABASE_VERSION_NAME, String.valueOf(Constants.SD_DATABASE_VERSION), String.valueOf(now), String.valueOf(exipre)});
        } catch (Exception ex) {
            Log.e(LOG_TAG, "onCreate|page_cache|" + LogUtils.getStackTraceString(ex));
        }

    }

    /**
     * 检查当前数据库的版本和系统保存的版本是否一致：如果版本为空，记录错误日志；如果版本不一致那么调用onUpadte()函数，完成数据库里面表的重建。
     */
    private void checkUpdate() {

        if (HAVECHECKED) return;

        HAVECHECKED = true;

        String version = getValue("select content from page_cache where id = ?", new String[]{Constants.SD_DATABASE_VERSION_NAME});

        if (null == version) {
            Log.e(LOG_TAG, "checkUpdate|value is empty" + errMsg);
            return;
        }

        if (Integer.valueOf(version) != Constants.SD_DATABASE_VERSION) {
            onUpdate();
        }

    }

    /**
     * 删除掉版本不一致的数据库里面所有的表，调用onCreate()函数，完成数据库的重新创建。
     */
    private void onUpdate() {
        try {
            core.execSQL("DROP TABLE IF EXISTS page_cache");
        } catch (Exception ex) {
            Log.e(LOG_TAG, "onUpgrade|page_cache|" + LogUtils.getStackTraceString(ex));
            ;
        }
        ;

        onCreate();
    }

}
