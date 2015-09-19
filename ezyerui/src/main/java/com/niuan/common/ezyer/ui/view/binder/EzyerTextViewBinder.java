package com.niuan.common.ezyer.ui.view.binder;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class EzyerTextViewBinder extends EzyerViewBinder<TextView> {
    @Override
    public boolean bindView(Object obj, View view) {
        if (view == null) {
            return false;
        }
        if (!(view instanceof TextView)) {
            return false;
        }

        TextView textView = (TextView) view;
        if (obj instanceof CharSequence) {
            textView.setText((CharSequence) obj);
        } else {
            textView.setText(obj == null ? "" : obj.toString());
        }
        return true;
    }

    @Override
    public Class<TextView> getSupportViewClass() {
        return TextView.class;
    }
}