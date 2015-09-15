package com.niuan.common.ezyer.base.util.dl.thread;

import android.util.Log;

import java.util.ArrayList;

public abstract class LimitedThreadBackgroundTask<PARAMS, PROGRESS, RESULT>
		extends BackgroundTask<PARAMS, PROGRESS, RESULT> {

	private static final String TAG = "LTBT";
	private static int sThreadLimit = 10;
	private static ArrayList<Integer> sThreadIdList = new ArrayList<Integer>();

	public static void setThreadLimit(int limit) {
		sThreadLimit = limit;
	}

	private static int sPrevId;

	private static int initNextExecutorId() {

		int id = getCurrentFreeExecutorId();

		if (id == -1) { // 如果当前没有空闲的
			if (isUnderLimit()) { // 若已生成的执行器未超限制数，则生成新的
				id = TaskExecutor.generateNewId();
				sThreadIdList.add(id);
			} else { // 否则使用正在忙碌的执行器进行排队等候
				int prevIndex = sThreadIdList.indexOf(sPrevId);
				if (isLast(prevIndex)) {
					id = sThreadIdList.get(0);
				} else {
					id = sThreadIdList.get(prevIndex + 1);
				}
			}
		}
		Log.d(TAG, "[initNextExecutorId], id =  " + id);
		sPrevId = id;

		return id;
	}

	private static boolean isUnderLimit() {
		Log.d(TAG,
				"[isUnderLimit], sThreadIdList.size() = "
						+ sThreadIdList.size() + ", sThreadLimit = "
						+ sThreadLimit);
		return sThreadIdList.size() < sThreadLimit;
	}

	private static boolean isLast(int lastIndex) {
		return lastIndex == lastIndex();
	}

	private static int lastIndex() {
		return sThreadLimit - 1;
	}

	/**
	 * 获取当前已保存的id中是否有空闲的执行器id
	 *
	 * @return 空闲的执行器id, 如果没有则返回-1
	 */
	private static int getCurrentFreeExecutorId() {
		int id = -1;

		/*
		 * 检查是否有空闲的执行器
		 */

		for (int i : sThreadIdList) {
			if (!TaskExecutor.isBusy(i)) {
				id = i;
				break;
			}
		}
		Log.d(TAG, "[getFreeExecutorId], id = " + id);
		return id;
	}

	public static int getThreadPoolSize() {
		return sThreadIdList.size();
	}

	public LimitedThreadBackgroundTask() {
		int id = initNextExecutorId();
		setExecutorId(id);
	}

}
