package com.niuan.common.ezyer.app.net;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.niuan.common.ezyer.app.pojo.Dish;
import com.niuan.common.ezyer.data.RefreshType;

import java.util.ArrayList;

/**
 * Created by Carlos on 2015/9/20.
 */
public class DishStructRequest extends BaseRequest<ArrayList<Dish>> {
    public DishStructRequest() {
        super(RequestUrlList.URL_DISH_LIST, null, new TypeToken<ArrayList<Dish>>() {
        }.getType());
    }

    private RefreshType mRefreshType = RefreshType.Update;

    public void setRefreshType(RefreshType refreshType) {
        mRefreshType = refreshType;
    }

//    @Override
//    protected Cache.Entry mergeCache(ArrayList<Dish> newData, Cache.Entry newCache, Cache.Entry oldCache) {
//
//        if (oldCache == null) {
//            return newCache;
//        }
//
//        if (newData == null) {
//            return newCache;
//        }
//        switch (mRefreshType) {
//            case Replace:
//                return newCache;
//            case Update: {
//                try {
//                    ArrayList<Dish> oldData = readResponse(oldCache.data, oldCache.responseHeaders);
//                    if (oldData != null) {
//                        oldData.addAll(0, newData);
//                        newCache.data = getBytes(oldData);
//                    }
//                } catch (VolleyError error) {
//                    error.printStackTrace();
//                }
//                break;
//            }
//            case Load:
//                try {
//                    ArrayList<Dish> oldData = readResponse(oldCache.data, oldCache.responseHeaders);
//                    if (oldData != null) {
//                        oldData.addAll(newData);
//                        newCache.data = getBytes(oldData);
//                    }
//                } catch (VolleyError error) {
//                    error.printStackTrace();
//                }
//                break;
//        }
//        return newCache;
//    }
}
