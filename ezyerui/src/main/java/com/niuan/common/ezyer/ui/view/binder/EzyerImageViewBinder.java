package com.niuan.common.ezyer.ui.view.binder;

import android.widget.ImageView;

import com.niuan.common.ezyer.data.RefreshType;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Carlos Liu on 2015/8/19.
 */
public class EzyerImageViewBinder extends EzyerViewBinder<ImageView, String> {
    @Override
    public boolean bindView(RefreshType refreshType, String obj, ImageView view) {
        ImageLoader.getInstance().displayImage(obj.toString(), view);
        return true;
    }
}
