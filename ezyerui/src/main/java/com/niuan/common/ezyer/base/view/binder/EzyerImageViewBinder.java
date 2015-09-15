package com.niuan.common.ezyer.base.view.binder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Carlos Liu on 2015/8/19.
 */
public class EzyerImageViewBinder extends EzyerViewBinder<ImageView> {
    @Override
    public boolean bindView(Object obj, View view) {
        ImageLoader.getInstance().displayImage(obj.toString(), (ImageView) view);
        return true;
    }

    @Override
    public Class<ImageView> getSupportViewClass() {
        return ImageView.class;
    }
}
