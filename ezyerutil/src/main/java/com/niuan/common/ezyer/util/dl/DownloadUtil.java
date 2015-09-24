package com.niuan.common.ezyer.util.dl;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Environment;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;


public class DownloadUtil {
    // private static final String TAG = "DownloadUtil";

    public static interface DownloadCallback {
        public void onDownload(int downloadBytes, int totalBytes);

        public void onFail();
    }

    private static Set<String> downSet = new HashSet<String>();

    public static boolean download(String downloadUrl, String storeFileName,
                                   DownloadCallback callback) {
        InputStream ins = null;
        String tmpStoreFileName = storeFileName + "_bak";
        clearStoreFile(storeFileName, tmpStoreFileName);
        OutputStream out = openStoreFile(tmpStoreFileName);

        if (out == null) {
            notifyFail(callback);
            return false;
        }
        synchronized (downSet) {
            downSet.add(storeFileName);
        }
        try {
            HttpResponse response = fetchResponse(downloadUrl);
            if (response == null) {
                notifyFail(callback);
                return false;
            }
            ins = getInputStream(response);

            if (ins == null) {
                notifyFail(callback);
                return false;
            }

            int fileSize = getFileSize(response);
            int totalRead = 0;
            byte[] buffer = new byte[1024 * 64];
            int lastRatio = 0;
            notifyDownload(callback, fileSize, totalRead);
            while (totalRead < fileSize) {
                int readed = ins.read(buffer);
                if (-1 == readed) {
                    break;
                } else {
                    out.write(buffer, 0, readed);
                    totalRead += readed;
                    if (totalRead >= fileSize) {
                        renameFile(tmpStoreFileName, storeFileName);
                        notifyDownload(callback, fileSize, totalRead);
                    } else {
                        int ratio = (int) (totalRead * 100L / fileSize);
                        if (ratio >= (lastRatio + 1)) {
                            lastRatio = ratio;
                            notifyDownload(callback, fileSize, totalRead);
                        }
                    }

                }
            }
            if (fileSize == 0 || fileSize < totalRead) {
                notifyFail(callback);
                return false;
            }
        } catch (IOException e) {
            notifyFail(callback);
            return false;
        } finally {
            synchronized (downSet) {
                downSet.remove(storeFileName);
            }
            if (null != ins) {
                try {
                    ins.close();
                } catch (IOException e) {
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return true;
    }

    private static void notifyDownload(DownloadCallback callback, int fileSize,
                                       int totalRead) {
        if (callback != null) {
            callback.onDownload(totalRead, fileSize);
        }
    }

    private static void notifyFail(DownloadCallback callback) {
        if (callback != null) {
            callback.onFail();
        }
    }

    private static InputStream getInputStream(HttpResponse response)
            throws IOException {
        if (response == null) {
            return null;
        }
        InputStream ins = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            ins = entity.getContent();
        }
        return ins;
    }

    private static int getFileSize(HttpResponse response) {
        int fileSize = 0;
        Header[] hs = response.getHeaders("Content-Length");
        if ((null != hs) && (hs.length >= 1)) {
            fileSize = Integer.parseInt(hs[0].getValue().trim());
        }
        return fileSize;
    }

    private static HttpResponse fetchResponse(String downloadUrl)
            throws IOException {
        AndroidHttpClient httpClient = AndroidHttpClient
                .newInstance("downloader");
        if (httpClient == null) {
            return null;
        }
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
                30 * 1000);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 30 * 3000);
        HttpResponse response = null;
        for (int i = 0; i < 5; i++) {
            HttpGet httpGet = new HttpGet(downloadUrl);
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                Header[] locHs = response.getHeaders("Location");
                if ((null != locHs) && (locHs.length >= 1)) {
                    downloadUrl = locHs[0].getValue();
                }
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                break;
            } else {
                response = null;
                break;
            }
        }
        return response;
    }

    private static void renameFile(String fromFileName, String toFileName) {
        String baseDir = getBaseDir();
        File from = new File(baseDir + "/" + fromFileName);
        File to = new File(baseDir + "/" + toFileName);
        from.renameTo(to);
    }

    private final static String BASE_PATH = String.format("/%s/",
            "download");
    private final static String SHARE_RECEIVE_PATH = String.format(
            "/%s_receiveFiles/", "download");

    public static String getBaseDir() {
        File baseDir = Environment.getExternalStorageDirectory();
        String basePath = baseDir.getPath() + BASE_PATH;
        File dir = new File(basePath);
        if (false == dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    private static String getShareReceiveBaseDir() {
        File baseDir = Environment.getExternalStorageDirectory();
        String basePath = baseDir.getPath() + SHARE_RECEIVE_PATH;
        File dir = new File(basePath);
        if (false == dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    public static String getShareReceivePath() {
        return SHARE_RECEIVE_PATH;
    }

    private static OutputStream openStoreFile(String fileName) {
        String baseDir = getBaseDir();
        File f = new File(baseDir + "/" + fileName);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            f.mkdirs();
            FileOutputStream fos = new FileOutputStream(f);
            return fos;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void clearStoreFile(String fileName, String tmpFileName) {
        String baseDir = getBaseDir();
        File f = new File(baseDir + "/" + fileName);
        f.delete();
        f = new File(baseDir + "/" + tmpFileName);
        f.delete();
    }

    public static String getFile(String fileName) {
        String baseDir = getBaseDir();
        File f = new File(baseDir + "/" + fileName);
        if (f.exists()) {
            return f.getPath();
        }
        return null;
    }

    public static File getRealFile(String fileName) {
        return new File(getBaseDir(), fileName);
    }

    public static File getRealShareReceiveFile(String fileName) {
        return new File(getShareReceiveBaseDir(), fileName);
    }

    public static String getFileName(int appId, int companyId, int versionCode,
                                     String fileType) {
        return "" + appId + "_" + companyId + "_" + versionCode + "."
                + fileType;
    }

    public static boolean isDowning(String storeFileName) {
        synchronized (downSet) {
            return downSet.contains(storeFileName);
        }
    }

    public static boolean checkDownloaded(Context context, String storeFileName) {
        String fileName = DownloadUtil.getFile(storeFileName);
        if (fileName == null) {
            return false;
        }
        Intent intent = getOpenFileIntent(fileName);
        context.startActivity(intent);
        return true;
    }

    @SuppressLint("InlinedApi")
    private static Intent getOpenFileIntent(String fileName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setDataAndType(Uri.fromFile(new File(fileName)),
                "application/vnd.android.package-archive");
        return intent;
    }

    public static PendingIntent openApk(Context context, String fileName) {
        Intent intent = getOpenFileIntent(fileName);
        return PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
//
//	@SuppressLint("InlinedApi")
//	public static PendingIntent openMain(Context context) {
//		Intent intent = new Intent(context, MainActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//		return PendingIntent.getActivity(context, 0, intent,
//				PendingIntent.FLAG_UPDATE_CURRENT);
//	}
//
//	public static Notification showProgressNotify(Context context, int id,
//												  long startTime, int downloadBytes, int totalBytes, String name) {
//
//		int completeRate = (int) ((float) downloadBytes / (float) totalBytes * 100);
//
//		String content = "";
//		if (downloadBytes == -1) {
//			content = context.getString(R.string.global_download_preparing);
//		} else {
//			content = context.getString(R.string.global_downloading,
//					completeRate);
//		}
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(
//				context).setContentTitle(name).setContentText(content)
//				.setSmallIcon(android.R.drawable.stat_sys_download)
//				.setWhen(startTime)
//				.setProgress(totalBytes, downloadBytes, totalBytes == 0)
//				.setOngoing(true).setContentIntent(openMain(context));
//		NotificationManager mNotificationManager = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//
//		Notification notify = builder.build();
//		mNotificationManager.notify(id, notify);
//		return notify;
//	}
//
//	public static Notification showDoneNotify(Context context, int id,
//											  String title, String storeFileName, boolean success) {
//		String fileName = storeFileName != null ? DownloadUtil
//				.getFile(storeFileName) : null;
//		String content = context.getString(success ? R.string.global_downloaded
//				: R.string.apk_download_failed);
//		PendingIntent intent = success ? openApk(context, fileName)
//				: openMain(context);
//
//		NotificationCompat.Builder builder = new NotificationCompat.Builder(
//				context).setContentTitle(title).setContentText(content)
//				.setSmallIcon(android.R.drawable.stat_sys_download_done)
//				.setAutoCancel(true).setContentIntent(intent);
//		NotificationManager mNotificationManager = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notify = builder.build();
//		mNotificationManager.notify(id, notify);
//		return notify;
//	}
//
//	public static void cancelNotify(Context context, int id) {
//		NotificationManager mNotificationManager = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		mNotificationManager.cancel(id);
//	}
//
//	public static void cancelAllNotifies(Context context) {
//		NotificationManager mNotificationManager = (NotificationManager) context
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		mNotificationManager.cancelAll();
//	}
}
