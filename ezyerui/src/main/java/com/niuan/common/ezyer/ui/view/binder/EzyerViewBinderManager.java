package com.niuan.common.ezyer.ui.view.binder;

import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuan.common.ezyer.data.RefreshType;
import com.niuan.common.ezyer.ui.view.BaseBannerView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class EzyerViewBinderManager {
    private static Map<Class<?>, EzyerViewBinder> mDefaultBinderMap = new HashMap<>();

    static {
        register(AbsListView.class, new EzyerAbsListViewBinder());
        register(TextView.class, new EzyerTextViewBinder());
        register(CheckBox.class, new EzyerCheckBoxBinder());
        register(ImageView.class, new EzyerImageViewBinder());
        register(BaseBannerView.class, new EzyerBannerViewBinder());
    }

//    public static void registerDefault(EzyerViewBinder binder) {
//        register(binder.getSupportViewClass(), binder);
//    }

    public static void register(Class<?> cls, EzyerViewBinder binder) {
        mDefaultBinderMap.put(cls, binder);
    }

    public static boolean bindView(RefreshType refreshType, View view, Object fieldValue) {
        return bindView(refreshType, view, fieldValue, null);
    }

    public static <V extends View, D> boolean bindView(RefreshType refreshType, V view, D fieldValue, EzyerViewBinder<V, D> binder) {
        if (binder == null) {
            binder = getBinder(view.getClass());
        }
        if (binder == null) {
            return false;
        }
        return binder.bindView(refreshType, fieldValue, view);
    }

    public static <V extends View, D> D getDataFromView(V view, EzyerViewBinder<V, D> binder) {
        if (binder == null) {
            binder = getBinder(view.getClass());
        }
        if (binder == null) {
            return null;
        }
        return binder.getData(view);
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
