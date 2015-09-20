package com.niuan.common.ezyer.net;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by Carlos on 2015/9/18.
 */
public class JsonParser {
    private static Gson sGson = new Gson();

    public static <T> T parseJson(String json, Type cls) {
        return sGson.fromJson(json, cls);
    }
}
