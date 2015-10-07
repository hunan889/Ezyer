package com.niuan.common.ezyer.cache.v2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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


    public interface Db {
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
                    }));
        }};
    }


    @Override
    public byte[] get(String key) {
        return new byte[0];
    }

    @Override
    public byte[] get(String key, int start, int end) {
        return new byte[0];
    }

    @Override
    public void set(String key, byte[] object, long expireTimeMillis) {
        ContentValues values = new ContentValues();

        //TODO: don't save expire time into the same table with data
        values.put(Db.CacheDataTable.Row.DATA.first, object);
        values.put(Db.CacheDataTable.Row.KEY.first, key);
        values.put(Db.CacheDataTable.Row.DATA.first, object);
        values.put(Db.CacheDataTable.Row.DATA.first, object);
        mDatabase.replace(Db.CacheDataTable.NAME, "", values);
    }

    @Override
    public void add(String key, byte[] object, long expireTimeMillis) {
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public boolean isExpired(String key) {
        return false;
    }

    @Override
    public void clear() {

    }
}
