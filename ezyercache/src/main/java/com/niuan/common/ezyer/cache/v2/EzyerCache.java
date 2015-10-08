package com.niuan.common.ezyer.cache.v2;

import java.util.List;

/**
 * Created by Carlos Liu on 2015/10/7.
 */
public interface EzyerCache {
    class PersistentObject {
        public final byte[] mData;
        public final String mKey;
        public final long mExpireTimeMillis;

        public PersistentObject(byte[] data, String key, long expireTimeMillis) {
            mData = data;
            mKey = key;
            mExpireTimeMillis = expireTimeMillis;
        }
    }

    class ValueObject {
        public final byte[] mData;
        public final String mKey;
        public final boolean mIsExpired;

        public ValueObject(byte[] data, String key, boolean isExpired) {
            mData = data;
            mKey = key;
            mIsExpired = isExpired;
        }
    }

    ValueObject get(String key);

    List<ValueObject> get(String key, int start, int end);

    void set(PersistentObject po);

    void add(PersistentObject po);

    void remove(String key);

    boolean isExpired(String key);

    void clear();
}
