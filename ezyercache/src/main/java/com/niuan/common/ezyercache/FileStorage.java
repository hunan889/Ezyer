package com.niuan.common.ezyercache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.yy.android.gamenews.util.Util;

public abstract class FileStorage {

	private final String LOG_TAG = FileStorage.class.getName();

	public abstract String getRootPath();
	private BitmapFactory.Options opt;

	private boolean creatBasePath(String path) {

		File file = new File(path);

		if (file.exists())
			return true;

		try {
			boolean isOK = file.mkdir();
			return isOK;
		} catch (Exception ex) {
			Log.d(LOG_TAG, Util.getStackTraceString(ex));
			return false;
		}
	}

	public String createPath(String path) {
		String basePath = getTempPath();

		if (basePath == null)
			return null;

		return creatBasePath(basePath + path) ? basePath + path : null;
	}

	public String getTempPath() {
		return getRootPath();
	}

	public boolean fileExsits(String fileName) {
		String path = getTempPath();

		if (path == null) {
			return false;
		}

		File file = new File(path + fileName);

		return file.exists();

	}

	public File getFile(String fileName) {

		fileName = !fileName.equals("") && fileName.startsWith(File.separator) ? fileName.substring(1) : fileName;
		
		String path = getTempPath();

		if (path == null) {
			return null;
		}
		File file = new File(path + fileName);

		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	public File createFile(String filename) {
		String path = getTempPath();

		if (path == null) {
			return null;
		}

		File file = new File(path + filename);
		try {
			if (file.exists()) {
				file.delete();
			}
			if (file.createNewFile() == true) {
				return file;
			} else {
				return null;
			}
		} catch (Exception ex) {
			Log.d(LOG_TAG, "createFile" + ex);
			return null;
		}
	}

	public File CreateFileIfNotFound(String fileName) {
		String path = getTempPath();

		if (path == null) {
			return null;
		}

		File file = getFile(fileName);

		if (null != file)
			return file;

		return createFile(fileName);

	}

	public Bitmap getImage(String fileName) {

		String path = getTempPath();

		if (path == null) {
			return null;
		}

		File file = getFile(fileName);

		if (null == file)
			return null;
		
		
		Bitmap pBitmap = null;
		if(null == opt)
		{
			opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;  
			opt.inInputShareable = true;
		}
		try {
			pBitmap = BitmapFactory.decodeFile(path + fileName,opt);
		} catch( OutOfMemoryError aError ) {
			aError.printStackTrace();
			System.gc();
			pBitmap = null;
		}

		return pBitmap;
	}

	public String saveImage(String fileName, Bitmap bm) {
		File file = createFile(fileName);

		if (null == file)
			return null;
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, output);
			output.flush();

			return file.getPath();
		} catch (Exception ex) {
			Log.d(LOG_TAG, "SaveFile|" + ex);
			return null;
		}finally{
			try {
				if(output != null)
					output.close();
			} catch (IOException e) {
				Log.d(LOG_TAG, Util.getStackTraceString(e));
			}
		}
	}

	public String SaveFile(String fileName, byte[] data) {

		File file = createFile(fileName);

		if (null == file)
			return null;
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file);
			output.write(data, 0, data.length);
			output.flush();
			return file.getPath();
		} catch (Exception ex) {
			Log.d(LOG_TAG, "SaveFile" + ex);
			return null;
		}finally{
			try {
				if(output != null)
					output.close();
			} catch (IOException e) {
				Log.d(LOG_TAG, Util.getStackTraceString(e));
			}
		}
	}

	public InputStream getInputStream(String filename) {
		File file = createFile(filename);
		if (null == file)
			return null;

		try {
			return new FileInputStream(file);
		} catch (Exception ex) {
			Log.d(LOG_TAG, "GetStreamFromFile" + ex);
			return null;
		}
	}

	public InputStream getOutputStream(String filename) {
		File file = createFile(filename);
		if (null == file)
			return null;

		try {
			return new FileInputStream(file);
		} catch (Exception ex) {
			Log.d(LOG_TAG, "GetStreamFromFile" + ex);
			return null;
		}
	}

	public boolean removeFile(String fileName) {

		File file = getFile(fileName);

		if (null != file) {
			try {
				return file.delete();
			} catch (Exception ex) {
				Log.d(LOG_TAG, Util.getStackTraceString(ex));
				return false;
			}
		}

		return true;
	}

	public void removeFolder(String folderPath) {
		try {
			delAllFile(folderPath);
			removeFile(folderPath);
		} catch (Exception ex) {
			Log.d(LOG_TAG, ex + "");
		}
	}

	public void delAllFile(String path) {
		File file = getFile(path);
		if (file == null)
			return;
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = getFile(path + tempList[i]);
			} else {
				temp = getFile(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文�?
				removeFolder(path + "/" + tempList[i]);//再删除空文件�?
			}
		}
	}
}
