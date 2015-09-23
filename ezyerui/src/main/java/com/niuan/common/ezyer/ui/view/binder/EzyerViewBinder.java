package com.niuan.common.ezyer.ui.view.binder;

import android.view.View;

import com.niuan.common.ezyer.data.RefreshType;


/**
 * Created by Carlos Liu on 2015/8/15.
 */
public abstract class EzyerViewBinder<VIEW extends View, DATA> {
    /**
     *
     * @param refreshType
     * @param obj  the data binds to the view
     * @param view view to bind
     * @return true if bind succeed, false otherwise
     */
    public abstract boolean bindView(RefreshType refreshType, DATA obj, VIEW view);

    public abstract DATA getData(VIEW view);
}
