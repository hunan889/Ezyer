package com.niuan.common.ezyer.cache.v2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Carlos Liu on 2015/10/7.
 */
public class EzyerDbCache implements EzyerCache {

    private volatile SQLiteDatabase mDatabase;

    public EzyerDbCache(Context context) {
        DbUtil.DbDescriptor descriptor = createDbDescriptor();
        mDatabase = DbUtil.getDatabase(context, descriptor);
    }


    private interface Db {
        String NAME = "ezyer_cache.db";
        int VERSION = 1;

        interface CacheDataTable {
            String NAME = "cache_data_info";

            interface Row {
                Pair<String, String> ID = new Pair<>("id", "integer primary key");
                Pair<String, String> KEY = new Pair<>("key", "varchar(50)");
                Pair<String, String> DATA = new Pair<>("data", "BLOB");
                Pair<String, String> CREATE_TIME = new Pair<>("create_time", "long");
                Pair<String, String> EXPIRE_TIME = new Pair<>("expire_time", "long");
            }

            interface Index {
                String NAME = "";
                List<String> INDEX_COLS = new ArrayList<String>() {{
                    add(Row.KEY.first);
                    add(Row.DATA.first);
                }};
            }
        }
    }

    public DbUtil.DbDescriptor createDbDescriptor() {
        return new DbUtil.DbDescriptor(Db.NAME, Db.VERSION, createDbTableDescriptors());
    }

    public List<DbUtil.DbTableDescriptor> createDbTableDescriptors() {
        return new ArrayList<DbUtil.DbTableDescriptor>() {{
            add(new DbUtil.DbTableDescriptor(Db.CacheDataTable.NAME,
                    new HashMap<String, String>() {
                        {
                            putPair(Db.CacheDataTable.Row.ID);
                            putPair(Db.CacheDataTable.Row.KEY);
                            putPair(Db.CacheDataTable.Row.DATA);
                            putPair(Db.CacheDataTable.Row.EXPIRE_TIME);
                            putPair(Db.CacheDataTable.Row.CREATE_TIME);
                        }

                        void putPair(Pair<String, String> pair) {
                            put(pair.first, pair.second);
                        }
                    },
                    new HashMap<String, List<String>>() {{
                        put(Db.CacheDataTable.Index.NAME, Db.CacheDataTable.Index.INDEX_COLS);
                    }}));
        }};
    }


    @Override
    public ValueObject get(String key) {
        String[] columns = new String[]{
                Db.CacheDataTable.Row.EXPIRE_TIME.first,
                Db.CacheDataTable.Row.KEY.first,
                Db.CacheDataTable.Row.DATA.first
        };
        String selection = Db.CacheDataTable.Row.KEY.first + " = ?";
        String[] args = new String[]{key};
        android.database.Cursor cursor = mDatabase.query(Db.CacheDataTable.NAME, columns, selection, args, null, null, null, "1");

        ValueObject vo = null;
        if (cursor != null && cursor.moveToFirst()) {
            byte[] data = cursor.getBlob(cursor.getColumnIndex(Db.CacheDataTable.Row.DATA.first));
            boolean isExpired = cursor.getLong(cursor.getColumnIndex(Db.CacheDataTable.Row.EXPIRE_TIME.first)) < System.currentTimeMillis();
            vo = new ValueObject(data, key, isExpired);
        }
        return vo;
    }

    @Override
    public List<ValueObject> get(String key, int start, int end) {
        List<ValueObject> voList = new ArrayList<>();

        String[] columns = new String[]{
                Db.CacheDataTable.Row.EXPIRE_TIME.first,
                Db.CacheDataTable.Row.KEY.first,
                Db.CacheDataTable.Row.DATA.first
        };
        String selection = Db.CacheDataTable.Row.KEY.first + " = ?";
        String[] args = new String[]{key};
        String limit = (end - start) + " offset " + start;
        android.database.Cursor cursor = mDatabase.query(Db.CacheDataTable.NAME, columns, selection, args, null, null, null, limit);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                byte[] data = cursor.getBlob(cursor.getColumnIndex(Db.CacheDataTable.Row.DATA.first));
                boolean isExpired = cursor.getLong(cursor.getColumnIndex(Db.CacheDataTable.Row.EXPIRE_TIME.first)) < System.currentTimeMillis();
                ValueObject vo = new ValueObject(data, key, isExpired);
                voList.add(vo);
            }
        }


        return voList;
    }

    @Override
    public void set(PersistentObject po) {
        ContentValues values = new ContentValues();
        long currentTime = SystemClock.uptimeMillis();
        values.put(Db.CacheDataTable.Row.KEY.first, po.mKey);
        values.put(Db.CacheDataTable.Row.DATA.first, po.mData);
        values.put(Db.CacheDataTable.Row.CREATE_TIME.first, currentTime);
        values.put(Db.CacheDataTable.Row.EXPIRE_TIME.first, currentTime + po.mExpireTimeMillis);
        mDatabase.replace(Db.CacheDataTable.NAME, Db.CacheDataTable.Row.KEY.first, values);
    }

    @Override
    public void add(PersistentObject po) {
        ContentValues values = new ContentValues();

        long currentTime = SystemClock.uptimeMillis();
        values.put(Db.CacheDataTable.Row.KEY.first, po.mKey);
        values.put(Db.CacheDataTable.Row.DATA.first, po.mData);
        values.put(Db.CacheDataTable.Row.CREATE_TIME.first, currentTime);
        values.put(Db.CacheDataTable.Row.EXPIRE_TIME.first, currentTime + po.mExpireTimeMillis);
        mDatabase.insert(Db.CacheDataTable.NAME, Db.CacheDataTable.Row.KEY.first, values);
    }

    @Override
    public void remove(String key) {
        String selection = Db.CacheDataTable.Row.KEY.first + " = ?";
        String[] args = new String[]{key};
        mDatabase.delete(Db.CacheDataTable.NAME, selection, args);
    }

    @Override
    public boolean isExpired(String key) {
        String[] columns = new String[]{Db.CacheDataTable.Row.EXPIRE_TIME.first};
        String selection = Db.CacheDataTable.Row.KEY.first + " = ?";
        String[] args = new String[]{key};
        android.database.Cursor cursor = mDatabase.query(Db.CacheDataTable.NAME, columns, selection, args, null, null, null, "1");

        long expireTime = 0;
        if (cursor != null && cursor.moveToFirst()) {
            expireTime = cursor.getLong(cursor.getColumnIndex(Db.CacheDataTable.Row.EXPIRE_TIME.first));
        }

        return expireTime < SystemClock.uptimeMillis();
    }

    @Override
    public void clear() {
        mDatabase.delete(Db.CacheDataTable.NAME, null, null);
    }
}
