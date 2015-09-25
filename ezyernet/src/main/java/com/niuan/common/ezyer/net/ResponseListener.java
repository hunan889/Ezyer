package com.niuan.common.ezyer.net;

import com.android.volley.Request;
import com.android.volley.VolleyError;

/**
 * Created by Carlos on 2015/9/18.
 */
public abstract class ResponseListener<T> {
    public abstract void onResponse(Request<T> request, T response, boolean fromCache);

    public void onError(Request<T> request, VolleyError error) {
        
    }
}
