package com.niuan.common.ezyer.cache.v2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.niuan.common.ezyer.cache.Constants;
import com.niuan.common.ezyer.cache.db.Database;
import com.niuan.common.ezyer.util.CollectionUtil;
import com.niuan.common.ezyer.util.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Carlos Liu on 2015/10/7.
 */
public class DbUtil {

    public static SQLiteDatabase getDatabase(Context context, DbDescriptor descriptor) {
        InnerHelper helper = new InnerHelper(context.getApplicationContext(), descriptor);
        return helper.getWritableDatabase();
    }

    public static class DbDescriptor {
        private String mName;
        private int mVersion;
        private List<DbTableDescriptor> mTableDescriptors;

        public DbDescriptor(String name, int version, List<DbTableDescriptor> descriptors) {
            mName = name;
            mVersion = version;
            mTableDescriptors = descriptors;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public List<DbTableDescriptor> getTableDescriptors() {
            return mTableDescriptors;
        }

        public void setTableDescriptors(List<DbTableDescriptor> tableDescriptors) {
            mTableDescriptors = tableDescriptors;
        }

        public int getVersion() {
            return mVersion;
        }

        public void setVersion(int version) {
            mVersion = version;
        }
    }

    public static class DbTableDescriptor {
        private String mName;
        private Map<String, String> mRowMap;

        public DbTableDescriptor(String name, Map<String, String> rowMap) {
            mName = name;
            mRowMap = rowMap;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public Map<String, String> getRowMap() {
            return mRowMap;
        }

        public void setRowMap(Map<String, String> rowMap) {
            mRowMap = rowMap;
        }
    }


    private static class InnerHelper extends SQLiteOpenHelper {
        private static final String LOG_TAG = "InnerHelper";

        private DbDescriptor mDescriptor;

        public InnerHelper(Context context, DbDescriptor descriptor) {
            super(context, descriptor.getName(), null, descriptor.getVersion());
            mDescriptor = descriptor;
        }

        private String getCreateSql(String tableName, Map<String, String> elementMap) {
            StringBuilder sqlBuilder = new StringBuilder("create table if not exists ");
            sqlBuilder.append(tableName);
            sqlBuilder.append("(");

            if (!CollectionUtil.isEmpty(elementMap)) {
                for (String name : elementMap.keySet()) {
                    String attr = elementMap.get(name);
                    sqlBuilder.append(name).append(" ").append(attr).append(",");
                }

                sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
            }
            return sqlBuilder.toString();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            List<DbTableDescriptor> tableDescriptors = mDescriptor.getTableDescriptors();
            for (DbTableDescriptor tableDescriptor : tableDescriptors) {
                String sql = getCreateSql(tableDescriptor.getName(), tableDescriptor.getRowMap());
                try {
                    db.execSQL(sql);
                } catch (Exception ex) {
                    Log.e(LOG_TAG, "onCreate|page_cache|" + LogUtils.getStackTraceString(ex));
                }
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (newVersion > oldVersion) {
                try {
                    db.execSQL("DROP TABLE IF EXISTS t_login");
                } catch (Exception ex) {
                    Log.e(LOG_TAG, "onUpgrade|t_login|" + LogUtils.getStackTraceString(ex));
                }

                onCreate(db);
            }
        }
    }
}
