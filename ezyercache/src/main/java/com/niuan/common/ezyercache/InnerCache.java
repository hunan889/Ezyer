package com.niuan.common.ezyercache;

import java.io.File;

import android.content.Context;

import com.yy.android.gamenews.Constants;

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
