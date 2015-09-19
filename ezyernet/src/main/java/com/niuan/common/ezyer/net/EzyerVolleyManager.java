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

    public Cache.Entry getCacheEntry(Request request) {
        return mRequestQueue.getCache().get(request.getCacheKey());
    }

    public void setCacheEntry(Request request, Cache.Entry entry) {
        mRequestQueue.getCache().put(request.getCacheKey(), entry);
    }

    public void add(Request request) {
        mRequestQueue.add(request);
    }

}
