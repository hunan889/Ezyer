package com.niuan.common.ezyer.ui.view.binder;

import android.widget.TextView;

import com.niuan.common.ezyer.data.RefreshType;


/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class EzyerTextViewBinder extends EzyerViewBinder<TextView, Object> {
    private static final int TAG_TEXT_CONTENT = 1001;

    @Override
    public boolean bindView(RefreshType refreshType, Object obj, TextView view) {
        if (view == null) {
            return false;
        }

        if (obj instanceof CharSequence) {
            view.setText((CharSequence) obj);
        } else {
            view.setText(obj == null ? "" : obj.toString());
        }
        return true;
    }
}