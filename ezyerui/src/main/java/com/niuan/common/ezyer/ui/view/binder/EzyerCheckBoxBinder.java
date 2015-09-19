package com.niuan.common.ezyer.ui.view.binder;

import android.view.View;
import android.widget.CheckBox;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class EzyerCheckBoxBinder extends EzyerViewBinder<CheckBox> {
    @Override
    public boolean bindView(Object obj, View view) {
        if (view == null) {
            return false;
        }
        if (!(view instanceof CheckBox)) {
            return false;
        }

        CheckBox checkBox = (CheckBox) view;

        boolean value = Boolean.parseBoolean(obj.toString());
        checkBox.setChecked(value);
        return true;
    }

    @Override
    public Class<CheckBox> getSupportViewClass() {
        return CheckBox.class;
    }
}