package com.niuan.common.ezyernet;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Created by Carlos Liu on 2015/9/17.
 */
public abstract class EzyerCacheRequest<T> extends EzyerRequest<T> {
    private long mTtl = TTL_FROM_HTTP_HEADER;
    private long mSoftTtl = TTL_FROM_HTTP_HEADER;

    /**
     * value indicates ttl data is configured by http header
     */
    public static final int TTL_FROM_HTTP_HEADER = -1;

    /**
     * value indicates ttl will never expired
     */
    public static final int TTL_EXPIRED_NEVER = -2;

    /**
     * Never expired ttl offset
     */
    public static final long TTL_OFFSET_NEVER_EXPIRED = 50 * 365 * 24 * 60 * 60 * 1000;

    public EzyerCacheRequest(int method, String url, Response.Listener<T> responseListener,
                             Response.ErrorListener errorListener) {
        super(method, url, responseListener, errorListener);
    }


    @Override
    protected Cache.Entry readCacheEntry(NetworkResponse response) {
        Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);

        long now = System.currentTimeMillis();
        entry.ttl = checkTtl(now, mTtl, entry.ttl);
        entry.softTtl = checkTtl(now, mSoftTtl, entry.softTtl);
        return entry;
    }

    private long checkTtl(long now, long userTtl, long serverTtl) {
        long ttl = serverTtl;
        if (userTtl == TTL_FROM_HTTP_HEADER) {
            ttl = serverTtl;
        } else if (userTtl == TTL_EXPIRED_NEVER) {
            ttl = getNeverExpiredTtl(now);
        } else {
            ttl = userTtl + now;
        }

        return ttl;
    }

    private long getNeverExpiredTtl(long now) {
        return now + TTL_OFFSET_NEVER_EXPIRED;
    }

    public void setTtl(long ttl) {
        mTtl = ttl;
    }

    public void setSoftTtl(long softTtl) {
        mSoftTtl = softTtl;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return super.getBody();
    }
}
