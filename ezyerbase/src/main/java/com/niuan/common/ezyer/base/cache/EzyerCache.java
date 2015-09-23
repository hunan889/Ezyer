package com.niuan.common.ezyer.base.cache;

import java.util.List;

/**
 * Created by Carlos on 2015/9/23.
 */
public class EzyerCache {
    private static final EzyerCache INSTANCE = new EzyerCache();

    public static EzyerCache getInstance() {
        return INSTANCE;
    }

    public <T> T get(String key, T proxy) {
        return null;
    }

    public <T> List<T> get(String key, int page, T proxy) {
        return null;
    }

    public void set(String key, Object data) {

    }

    public void set(String key, List<?> dataList, int page) {

    }
}
