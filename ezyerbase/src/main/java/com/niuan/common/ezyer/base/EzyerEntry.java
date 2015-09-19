package com.niuan.common.ezyer.base;

import android.content.Context;

import com.niuan.common.ezyer.net.EzyerVolleyManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Carlos Liu on 2015/8/19.
 */
public class EzyerEntry {
    public static DisplayImageOptions DEFAULT_DISPLAYER = getDisplayOptions(0);

    public static DisplayImageOptions getDisplayOptions(int imageDefault) {
        return getDisplayOptions(imageDefault, imageDefault, imageDefault);
    }

    private static DisplayImageOptions getDisplayOptions(int imageOnFail,
                                                         int imageOnLoading, int imageForEmptyUri) {
        return new DisplayImageOptions.Builder().resetViewBeforeLoading(true)
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .showImageOnFail(imageOnFail)
                .showImageOnLoading(imageOnLoading)
                .showImageForEmptyUri(imageForEmptyUri).build();
    }

    public static void init(Context context) {
        File individualCacheDir = StorageUtils
                .getIndividualCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(DEFAULT_DISPLAYER).build();
        ImageLoader.getInstance().init(config);


        EzyerVolleyManager.init(context);
    }
}
