package com.niuan.common.ezyer.ui.view.binder;

import android.widget.ImageView;

import com.niuan.common.ezyer.data.RefreshType;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Carlos Liu on 2015/8/19.
 */
public class EzyerImageViewBinder extends EzyerViewBinder<ImageView, String> {
    private int TAG_IMG_URL = 1001;

    @Override
    public boolean bindView(RefreshType refreshType, String obj, ImageView view) {
        ImageLoader.getInstance().displayImage(obj, view);
        view.setTag(TAG_IMG_URL, obj);
        return true;
    }

    @Override
    public String getData(ImageView view) {
        String tag = (String) view.getTag(TAG_IMG_URL);
        return tag == null ? "" : tag;
    }
}
