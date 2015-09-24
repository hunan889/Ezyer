package com.niuan.common.ezyercache;

import java.io.File;

import android.os.Environment;
import android.util.Log;

import com.yy.android.gamenews.Constants;
import com.yy.android.gamenews.util.Util;

public class SDCache extends FileStorage {

	private final String LOG_TAG = SDCache.class.getName();

	private String mRoot;

	/**
	 * 该构造函数首先检查了sd card是否存在，如果不存在抛出�?��非检查�?异常。如果存在，那么在sd card的目录下创建�?��目录Config.TMPDIRNAME�?
	 */
	public SDCache() {
		if (!Util.isSDExists()) {
			RuntimeException ex = new RuntimeException("sd card is not exists.");
			Log.e(LOG_TAG, ex + "");
			throw ex;
		}

		mRoot = Environment.getExternalStorageDirectory() + "/" + Constants.TMPDIRNAME;

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
