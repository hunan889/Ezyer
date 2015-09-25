package com.niuan.common.ezyer.net;

import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Carlos Liu on 2015/9/17.
 */
public abstract class EzyerRequest<T> extends Request<T> {

    private String mCacheKey;
    private Object[] mParams;
    private ResponseListener<T> mResponseListener;

    private Cache.Entry mCacheBeforeExecuted;
    private boolean mExpiredBeforeExecuted;

    private boolean mIsRunning;

    public EzyerRequest(int method, String url) {
        super(method, url, null);
    }

    public void setResponseListener(ResponseListener<T> listener) {
        mResponseListener = listener;
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        mResponseListener = null;
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        if (mResponseListener != null) {
            mResponseListener.onError(this, error);
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (mResponseListener != null) {
            boolean fromCache = false;
            if (shouldCache()) {
                /*
                 If not expired before execute, means request will be read from cache if 
                 soft ttl not expired, or from net otherwise.
                 
                 If expired, the request will be read from net
                 */
                if (mExpiredBeforeExecuted) {
                    fromCache = false;
                } else {
                    Cache.Entry realCacheEntry = getRealCacheEntry();
                    if (realCacheEntry != null) {
                        /*
                         To check if data from local or net, need to check if these two cache's ttl 
                         and  soft ttl matches.
                         
                         If request from net, the cache's ttl and soft ttl will be updated.
                          */

                        fromCache = mCacheBeforeExecuted.ttl == realCacheEntry.ttl
                                && mCacheBeforeExecuted.softTtl == realCacheEntry.softTtl;
                    }
                }
            }
            mResponseListener.onResponse(this, response, fromCache);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            T obj = readResponse(response);
            Cache.Entry entry = readCacheEntry(response);

            if (mCacheBeforeExecuted != null) {
                if (!(mCacheBeforeExecuted.ttl == entry.ttl
                        && mCacheBeforeExecuted.softTtl == entry.softTtl)) {
                    mergeCache(obj, entry, mCacheBeforeExecuted);
                }
            }


            return Response.success(obj, entry);
        } catch (VolleyError error) {
            return Response.error(error);
        }
    }

    protected Cache.Entry mergeCache(T newData, Cache.Entry newCache, Cache.Entry oldCache) {
        return newCache;
    }

    protected abstract T readResponse(NetworkResponse response) throws VolleyError;

    protected abstract Cache.Entry readCacheEntry(NetworkResponse response);

    public EzyerRequest<T> setCacheKey(String cacheKey) {
        mCacheKey = cacheKey;
        return this;
    }

    @Override
    public String getCacheKey() {
        return TextUtils.isEmpty(mCacheKey) ? super.getCacheKey() : mCacheKey;
    }

    public void execute() {
        mCacheBeforeExecuted = getRealCacheEntry();
        mExpiredBeforeExecuted = (mCacheBeforeExecuted == null || mCacheBeforeExecuted.isExpired());
        getVolleyManager().add(this);
    }

    public EzyerVolleyManager getVolleyManager() {
        return EzyerVolleyManager.getInstance();
    }

    /**
     * Gets cache for this request from Request Queue
     *
     * @return
     */
    protected Cache.Entry getRealCacheEntry() {
        return getVolleyManager().getCacheEntry(getCacheKey());
    }

    /**
     * Sets cache for this request to Request Queue, which will be saved to disk
     *
     * @param entry
     */
    protected void setRealCacheEntry(Cache.Entry entry) {
        getVolleyManager().setCacheEntry(getCacheKey(), entry);
    }

    public void setCustomParams(Object... params) {
        mParams = params;
    }

    public Object[] getCustomParams() {
        return mParams;
    }

    public boolean isRunning() {
        return mIsRunning;
    }

}
