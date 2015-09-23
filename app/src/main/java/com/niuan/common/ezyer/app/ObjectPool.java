package com.niuan.common.ezyer.app;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Carlos on 2015/9/22.
 */
public class ObjectPool {

    public static class CacheableObject<T> {
        public T mObject;
        public boolean mIsBusy;
    }

    public Map<Class<?>, CacheableObject> sMap = new HashMap<>();

    public void add(Object tag, Object object) {

    }

    public void remove(Object tag) {

    }

    public void get(Object tag) {

    }

    public Object addReference() {
        return null;
    }

    public void releaseReference() {

    }
}
