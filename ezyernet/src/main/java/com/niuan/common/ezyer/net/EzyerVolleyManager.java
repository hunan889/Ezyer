package com.niuan.common.ezyer.net;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Carlos on 2015/9/18.
 */
public class EzyerVolleyManager {

    private RequestQueue mRequestQueue;
    private static EzyerVolleyManager sInstance;

    private EzyerVolleyManager(Context context) {

        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static void init(Context context) {
        if (sInstance == null) {
            synchronized (EzyerVolleyManager.class) {
                if (sInstance == null) {
                    sInstance = new EzyerVolleyManager(context.getApplicationContext());
                }
            }
        }
    }

    public static EzyerVolleyManager getInstance() {
        return sInstance;
    }

    /**
     * Get cache for this key
     *
     * @param cacheKey
     * @return cache entry get from request queue
     */
    public Cache.Entry getCacheEntry(String cacheKey) {
        return mRequestQueue.getCache().get(cacheKey);
    }

    /**
     * Set cache key for this key to local file
     *
     * @param cacheKey
     * @param entry    cache entry for this key
     */
    public void setCacheEntry(String cacheKey, Cache.Entry entry) {
        mRequestQueue.getCache().put(cacheKey, entry);
    }

    public void add(Request request) {
        mRequestQueue.add(request);
    }

}
