package com.niuan.common.ezyer.cache.v2;

/**
 * Created by Carlos Liu on 2015/10/7.
 */
public interface EzyerCache {

    byte[] get(String key);

    byte[] get(String key, int start, int end);

    void set(String key, byte[] object, long expireTimeMillis);

    void add(String key, byte[] object, long expireTimeMillis);

    void remove(String key);

    boolean isExpired(String key);

    void clear();
}
