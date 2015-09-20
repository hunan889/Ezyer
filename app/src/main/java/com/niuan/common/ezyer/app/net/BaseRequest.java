package com.niuan.common.ezyer.app.net;

import com.google.gson.reflect.TypeToken;
import com.niuan.common.ezyer.net.EzyerParseJsonRequest;

import java.lang.reflect.Type;

/**
 * Created by Carlos on 2015/9/20.
 */
public class BaseRequest<T> extends EzyerParseJsonRequest<T> {
    public BaseRequest(String url, String requestBody, Type cls) {
        super(url, requestBody, cls);
        setShouldCache(true);
    }
}
