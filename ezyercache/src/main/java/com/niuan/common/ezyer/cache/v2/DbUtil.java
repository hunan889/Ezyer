package com.niuan.common.ezyer.cache.v2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.niuan.common.ezyer.util.CollectionUtil;
import com.niuan.common.ezyer.util.LogUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
        private Map<String, String> mColumnMap;
        private Map<String, List<String>> mIndexMap;

        public DbTableDescriptor(String name, Map<String, String> columnMap, Map<String, List<String>> sqlIndexMap) {
            mName = name;
            mColumnMap = columnMap;
            mIndexMap = sqlIndexMap;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public Map<String, String> getColumnMap() {
            return mColumnMap;
        }

        public void setColumnMap(Map<String, String> columnMap) {
            mColumnMap = columnMap;
        }

        public Map<String, List<String>> getIndexMap() {
            return mIndexMap;
        }

        public void setIndexMap(Map<String, List<String>> indexMap) {
            mIndexMap = indexMap;
        }
    }


    private static class InnerHelper extends SQLiteOpenHelper {
        private static final String LOG_TAG = "InnerHelper";

        private DbDescriptor mDescriptor;

        public InnerHelper(Context context, DbDescriptor descriptor) {
            super(context, descriptor.getName(), null, descriptor.getVersion());
            mDescriptor = descriptor;
        }

        private String getCreateSql(String tableName, Map<String, String> columnMap) {
            StringBuilder sqlBuilder = new StringBuilder("create table if not exists ");
            sqlBuilder.append(tableName);

            if (!CollectionUtil.isEmpty(columnMap)) {
                sqlBuilder.append("(");
                Set<String> keys = columnMap.keySet();
                for (String name : keys) {
                    String attr = columnMap.get(name);
                    sqlBuilder.append(name).append(" ").append(attr).append(",");
                }
                sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
                sqlBuilder.append(");");
            }
            return sqlBuilder.toString();
        }

        private String getIndexSql(String tableName, String indexName, List<String> columnList) {
            StringBuilder sqlBuilder = new StringBuilder("create index ");
            sqlBuilder.append(indexName).append(" on ")
                    .append(tableName)
                    .append("(");

            for (int i = 0; i < columnList.size(); i++) {
                sqlBuilder.append(i == 0 ? "" : ",").append(columnList.get(i));
            }
            sqlBuilder.append(");");
            return sqlBuilder.toString();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            List<DbTableDescriptor> tableDescriptors = mDescriptor.getTableDescriptors();
            for (DbTableDescriptor tableDescriptor : tableDescriptors) {
                String tableName = tableDescriptor.getName();
                String createSql = getCreateSql(tableName, tableDescriptor.getColumnMap());
                try {
                    db.execSQL(createSql);

                    Map<String, List<String>> indexMap = tableDescriptor.getIndexMap();
                    if (indexMap != null) {
                        for (String indexName : indexMap.keySet()) {
                            List<String> indexColumnList = indexMap.get(indexName);
                            String indexSql = getIndexSql(tableName, indexName, indexColumnList);
                            db.execSQL(indexSql);
                        }
                    }

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
