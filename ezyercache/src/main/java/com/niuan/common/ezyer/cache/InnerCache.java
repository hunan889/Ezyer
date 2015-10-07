package com.niuan.common.ezyer.cache;

import android.content.Context;

import java.io.File;


public class InnerCache extends FileStorage {

    private String mRoot;

    public InnerCache(Context context) {
        mRoot = context.getCacheDir() + "/" + Constants.TMPDIRNAME;

        File file = new File(mRoot);

        if (!file.exists()) {
            file.mkdir();
        }
    }

    @Override
    public String getRootPath() {
        return mRoot + "/";
    }
}
