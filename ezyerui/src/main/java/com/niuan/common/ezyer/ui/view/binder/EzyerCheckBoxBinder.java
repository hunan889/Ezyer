package com.niuan.common.ezyer.ui.view.binder;

import android.widget.CheckBox;

import com.niuan.common.ezyer.data.RefreshType;


/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class EzyerCheckBoxBinder extends EzyerViewBinder<CheckBox, Boolean> {
    @Override
    public boolean bindView(RefreshType refreshType, Boolean obj, CheckBox view) {
        if (view == null) {
            return false;
        }
        view.setChecked(obj);
        return true;
    }
}