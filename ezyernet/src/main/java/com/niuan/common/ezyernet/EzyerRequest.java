package com.niuan.common.ezyernet;

import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * Created by Carlos Liu on 2015/9/17.
 */
public abstract class EzyerRequest<T> extends Request<T> {

    private String mCacheKey;

    private Response.Listener<T> mResponseListener;

    public EzyerRequest(int method, String url,
                        Response.Listener<T> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        mResponseListener = responseListener;
    }

    @Override
    protected void deliverResponse(T response) {
        if (mResponseListener != null) {
            mResponseListener.onResponse(response);
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(readResponse(response), readCacheEntry(response));
        } catch (VolleyError error) {
            return Response.error(error);
        }
    }

    protected abstract T readResponse(NetworkResponse response) throws VolleyError;

    protected abstract Cache.Entry readCacheEntry(NetworkResponse response);

    public void setCacheKey(String cacheKey) {
        mCacheKey = cacheKey;
    }

    @Override
    public String getCacheKey() {
        return TextUtils.isEmpty(mCacheKey) ? super.getCacheKey() : mCacheKey;
    }
}
