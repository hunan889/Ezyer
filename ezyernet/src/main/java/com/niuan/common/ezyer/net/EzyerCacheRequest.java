package com.niuan.common.ezyer.net;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Created by Carlos Liu on 2015/9/17.
 */
public abstract class EzyerCacheRequest<T> extends EzyerRequest<T> {
    private long mTtl = TTL_FROM_HTTP_HEADER;
    private long mSoftTtl = TTL_FROM_HTTP_HEADER;
    private Cache.Entry mConfiguredEntry; // entry configured from server or local

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
    private static final long TTL_OFFSET_NEVER_EXPIRED = 50 * 365 * 24 * 60 * 60 * 1000;
    /**
     * Try get data from cache, if cache doesn't exist, get from net
     */
    public static final int EXE_TYPE_CACHE_IF_EXIST = 1001;
    /**
     * Gets data from net, and refreshes cache if {@link #shouldCache()} returns true
     */
    public static final int EXE_TYPE_NET_ONLY = 1002;
    /**
     * Gets data from cache, then refresh data from net
     */
    public static final int EXE_TYPE_CACHE_THEN_NET = 1003;
    /**
     * Default execution type, data cache strategy obeys server config or the value set from{@link #setSoftTtl(long)} and {@link #setTtl(long)}
     */
    public static final int EXE_TYPE_AS_CONFIG = 1004;

    public EzyerCacheRequest(int method, String url, ResponseListener<T> listener) {
        super(method, url, listener);
    }


    @Override
    protected Cache.Entry readCacheEntry(NetworkResponse response) {
        Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);

        long now = System.currentTimeMillis();
        entry.ttl = checkTtl(now, mTtl, entry.ttl);
        entry.softTtl = checkTtl(now, mSoftTtl, entry.softTtl);
        mConfiguredEntry = entry;
        return entry;
    }

    @Override
    protected void deliverResponse(T response) {
        super.deliverResponse(response);
        // reset configured entry to cache due to cache entry maybe set during execute method
        if (shouldCache()) {
            setRealCacheEntry(mConfiguredEntry);
        }
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

    private long getExpiredTtl(long now) {
        return now - 1;
    }

    private long getNeverExpiredTtl(long now) {
        return now + TTL_OFFSET_NEVER_EXPIRED;
    }

    public EzyerCacheRequest<T> setTtl(long ttl) {
        mTtl = ttl;
        return this;
    }

    public EzyerCacheRequest<T> setSoftTtl(long softTtl) {
        mSoftTtl = softTtl;
        return this;
    }

    /**
     * Adds current request to Request Queue using {@link EzyerVolleyManager#add(Request)}<br/> 
     */
    public void execute() {
        execute(EXE_TYPE_AS_CONFIG);
    }

    /**
     * Adds current request to Request Queue using {@link EzyerVolleyManager#add(Request)}<br/>
     * User can customize where the data from by below types:
     * <pre>
     * {@link #EXE_TYPE_AS_CONFIG}
     * {@link #EXE_TYPE_CACHE_IF_EXIST}
     * {@link #EXE_TYPE_CACHE_THEN_NET}
     * {@link #EXE_TYPE_NET_ONLY}
     * </pre>
     *
     * @param type where data should be read from
     */
    public void execute(int type) {

        if (type != EXE_TYPE_AS_CONFIG) {
            Cache.Entry entry = getRealCacheEntry();
            if (entry != null) {
                configEntry(entry, type);
                setRealCacheEntry(entry);
            }
        }
        super.execute();
    }

    private void configEntry(Cache.Entry entry, int exeType) {
        long now = System.currentTimeMillis();

        switch (exeType) {
            case EXE_TYPE_NET_ONLY: {
                entry.ttl = getExpiredTtl(now); // expired
                break;
            }
            case EXE_TYPE_CACHE_IF_EXIST: {
                entry.ttl = getNeverExpiredTtl(now);
                entry.softTtl = getNeverExpiredTtl(now);
                break;
            }
            case EXE_TYPE_CACHE_THEN_NET: {
                entry.ttl = getNeverExpiredTtl(now);
                entry.softTtl = getExpiredTtl(now);
                break;
            }
            case EXE_TYPE_AS_CONFIG:
            default: {
                // default do not config ttl and soft ttl so it's empty here
                break;
            }
        }
    }
}
