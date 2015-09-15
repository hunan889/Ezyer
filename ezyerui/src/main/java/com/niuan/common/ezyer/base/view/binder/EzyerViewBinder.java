package com.niuan.common.ezyer.base.view.binder;

import android.view.View;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
public abstract class EzyerViewBinder<VIEW> {
    /**
     * @param obj  the data binds to the view
     * @param view view to bind
     * @return true if bind succeed, false otherwise
     */
    public abstract boolean bindView(Object obj, View view);

    public abstract Class<VIEW> getSupportViewClass();
}
