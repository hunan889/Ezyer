package com.niuan.common.ezyer.base.view.binder;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class EzyerViewBinderManager {
    private static Map<Class<?>, EzyerViewBinder> mDefaultBinderMap = new HashMap<>();

    static {
        registerDefault(new EzyerAbsListViewBinder());
        registerDefault(new EzyerTextViewBinder());
        registerDefault(new EzyerCheckBoxBinder());
        registerDefault(new EzyerImageViewBinder());
        registerDefault(new EzyerBannerViewBinder());
    }

    public static void registerDefault(EzyerViewBinder binder) {
        register(binder.getSupportViewClass(), binder);
    }

    public static void register(Class<?> cls, EzyerViewBinder binder) {
        mDefaultBinderMap.put(cls, binder);
    }

    public static boolean bindView(View view, Object fieldValue) {
        return bindView(view, fieldValue, null);
    }

    public static boolean bindView(View view, Object fieldValue, EzyerViewBinder binder) {
        if (binder == null) {
            binder = getBinder(view.getClass());
        }
        if (binder == null) {
            return false;
        }
        return binder.bindView(fieldValue, view);
    }

    public static EzyerViewBinder getBinder(Class<?> cls) {
        EzyerViewBinder binder = mDefaultBinderMap.get(cls);
        if (binder == null) {
            for (Class<?> supportCls : mDefaultBinderMap.keySet()) {
                if (supportCls.isAssignableFrom(cls)) {
                    return mDefaultBinderMap.get(supportCls);
                }
            }
        }
        return binder;
    }
}
