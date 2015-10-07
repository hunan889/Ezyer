package com.niuan.common.ezyer.cache.db;

import android.content.Context;
import android.util.Log;

public class DbFactory {

    private static final String LOG_TAG = DbFactory.class.getName();

    public enum DbType {CARD, INNER}

    private static DbCard dbCard;

    private static DbInner dbInner;

    public static Database getInstance(Context context) {
        return getInstance(context, DbType.INNER);
    }

    public static Database getInstance(Context context, DbType type) {
        if (type == DbType.CARD) {
            synchronized (DbCard.class) {
                if (null == dbCard) {
                    dbCard = new DbCard();
                }

                return dbCard;
            }
        }

        synchronized (DbInner.class) {
            if (null == dbInner) {
                dbInner = new DbInner(context);
            }
            return dbInner;
        }
    }

    public static void closeDataBase() {
        try {
            if (null != dbInner) dbInner.close();
            if (null != dbCard) dbCard.close();

            dbInner = null;
            dbCard = null;

        } catch (Exception ex) {
            Log.e(LOG_TAG, "closeDabaBase" + ex);
        }
    }
}
