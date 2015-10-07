package com.niuan.common.ezyer.cache;

import android.content.ContentValues;
import android.content.Context;
import android.util.Base64;

import com.niuan.common.ezyer.cache.db.Database;
import com.niuan.common.ezyer.cache.db.DbFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * t_cache_page表结构 t_page_cache( id varchar(50) primary key comment '主键',
 * content TEXT comment '存储的内容，主要的数据 部分', row_create_time INTEGER comment
 * '记录的创建时间', row_expire_time INTEGER comment '过期的时间' )
 * 该类主要完成了从t_page_cache表来得到表中各个记录的数据，以及对content列记录的序列化和反序列化的过程。
 */
public class EzyerPageCache {

    private static final String LOG_TAG = EzyerPageCache.class.getName();

    private static final String TABLE = "t_page_cache";

    private Database mDatabase;
    private Context mContext;

    public EzyerPageCache(Context context, DbFactory.DbType target) {
        mDatabase = DbFactory.getInstance(context, target);
        mContext = context;
    }

    public EzyerPageCache(Context context) {
        this(context, DbFactory.DbType.INNER);
    }
//
//	public boolean setJceObject(String key, Object object, long cacheTime) {
//		JceOutputStream os = v2 JceOutputStream();
//
//		try {
//			os.write(object, 0);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String str = v2 String(Base64.encode(os.toByteArray(), Base64.DEFAULT));
//
//		return set(key, str, cacheTime);
//	}
//
//	@SuppressWarnings("unchecked")
//	public <T> T getJceObject(String key, T t) {
//
//		String str = get(key);
//		if (str == null || "".equals(str)) {
//			return null;
//		}
//		byte[] data = Base64.decode(str.getBytes(), Base64.DEFAULT);
//		JceInputStream is = v2 JceInputStream(data);
//		T returnValue = null;
//
//		try {
//			returnValue = (T) is.read(t, 0, true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return returnValue;
//	}

    /**
     * 将对象序列化，然后生成一个字符类型，调用该类的set()函数来完成对该对象的插入或者更新
     *
     * @param key       :对象存储数据库中的键值
     * @param obj       ：待序列化的对象
     * @param cacheTime
     * @return 对象序列化之后生成的字符串
     */
    public boolean setObject(String key, Object obj, long cacheTime) {

        String str = null;
        ByteArrayOutputStream baops = null;
        ObjectOutputStream oos = null;

        try {
            baops = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baops);
            oos.writeObject(obj);
            str = new String(Base64.encode(baops.toByteArray(), Base64.DEFAULT));
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (baops != null) {
                    baops.close();
                }
            } catch (IOException ex) {
                return false;
            }
        }
        return set(key, str, cacheTime);
    }

    /**
     * 将传入的字符串对象和对应的主键存入数据库中，如果该key已经存在那么就更新对应的记录，如果key不存在那么就将该记录插入到数据库中。
     *
     * @param key       ：待插入字符串的主键
     * @param content   ：待插入的字符串
     * @param cacheTime
     * @return 如果插入插入或者更新成功那么就返回true，否则返回false
     */
    public boolean set(String key, String content, long cacheTime) {
        checkDataBaseState();
        if (null == mDatabase)
            return false;

        String id = mDatabase.getValue("select id from " + TABLE + " where id = ?",
                key);
        if (mDatabase.errCode != 0) {
            return false;
        }

        int affectRows = 0;

        long now = System.currentTimeMillis();// ToolUtil.getCurrentTime();
        long end = cacheTime == 0 ? 0 : (now + cacheTime * 1000);

        ContentValues param = new ContentValues();

        param.put("content", content);
        param.put("row_create_time", now);
        param.put("row_expire_time", end);

        if (null != id) {
            affectRows = mDatabase.update(TABLE, param, "id=?", key);
        } else {
            param.put("id", key);
            affectRows = (int) mDatabase.insert(TABLE, param);
        }

        return affectRows > 0;
    }

    /**
     * 主要是根据主键来从数据库中得到content，然后反序列化成对应的对象。
     *
     * @param key ：数据库中记录的主键
     * @return 根据字符串反序列化生成的对象
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getObject(String key) {
        String body = get(key);

        if (null == body) {
            return null;
        }

        ObjectInputStream ois = null;
        T obj = null;
        try {

            ois = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(
                    body.getBytes(), Base64.DEFAULT)));
            obj = (T) ois.readObject();

        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
            }
        }
        return obj;
    }

    /**
     * 根据记录的主键从数据库中得到记录的content内容，判断数据是否过期，如果已经过期就删除掉,该函数其实应该命名为getContent()更好
     *
     * @param key ：记录的主键
     * @return 字符串类型的content内容
     */
    public String get(String key) {
        checkDataBaseState();
        HashMap<String, String> result = mDatabase.getOneRow(
                "select content, row_expire_time from " + TABLE
                        + " where id = ?", key);

        if (0 != mDatabase.errCode) {
            return null;
        }

        if (null == result) {
            return null;
        }

        // 过期的数据不需要删除
        // long now = System.currentTimeMillis();// .getCurrentTime();
        // long expire = Long.valueOf(result.get("row_expire_time"));
        // if (expire != 0 && expire < now) {
        // mDatabase.execute("delete from " + TABLE + " where id = ?", key);
        // return null;
        // }

        return result.get("content");

    }

    /**
     * 根据传入的主键，获得row_create_time列的信息
     *
     * @param key ：传入的主键
     * @return row_create_time列的信息
     */
    public long getRowCreateTime(String key) {
        checkDataBaseState();
        HashMap<String, String> pResult = mDatabase.getOneRow(
                "select row_create_time from " + TABLE + " where id = ?", key);
        long pNow = System.currentTimeMillis();
        ;

        if (0 != mDatabase.errCode || null == pResult) {
            return pNow;
        }

        long pCreateTime = Long.valueOf(pResult.get("row_create_time"));

        return pCreateTime;
    }

    /**
     * 根据主键来得到content内容，不判断内容是否过期，注意这个函数和该类的get()函数的区别。
     *
     * @param key 记录的主键
     * @return 记录的content内容
     */
    public String getNoDelete(String key) {
        checkDataBaseState();
        HashMap<String, String> result = mDatabase.getOneRow("select content from "
                + TABLE + " where id = ?", key);

        if (0 != mDatabase.errCode) {
            return null;
        }

        if (null == result) {
            return null;
        }

        return result.get("content");
    }

    /*
     * judge whether content of the key is expired or not
     * 
     * @param key
     */
    public boolean isExpire(String key) {
        checkDataBaseState();
        HashMap<String, String> result = mDatabase.getOneRow(
                "select row_expire_time from " + TABLE + " where id = ?", key);

        if (0 != mDatabase.errCode) {
            return true;
        }

        if (null == result) {
            return true;
        }

        long now = System.currentTimeMillis();
        long expire = Long.valueOf(result.get("row_expire_time"));
        if (expire != 0 && expire < now) {
            return true;
        }

        return false;
    }

    public void remove(String key) {
        checkDataBaseState();
        mDatabase.execute("delete from " + TABLE + " where id = ?", key);
    }

    public void removeLeftLike(String key) {
        checkDataBaseState();
        mDatabase.execute("delete from " + TABLE + " where id like ?", key + "%");
    }

    private void checkDataBaseState() {
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = DbFactory.getInstance(mContext, DbFactory.DbType.INNER);
        }
    }
}
