package com.niuan.common.ezyer.app.net;

import com.niuan.common.ezyer.net.EzyerParseJsonRequest;

/**
 * Created by Carlos on 2015/9/20.
 */
public class BaseRequest<T> extends EzyerParseJsonRequest<T> {
    public BaseRequest(String url, String requestBody, Class<T> cls) {
        super(url, requestBody, cls);
        setShouldCache(true);
    }
}
