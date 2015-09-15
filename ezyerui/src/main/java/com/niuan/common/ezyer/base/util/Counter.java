package com.niuan.common.ezyer.base.util;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class Counter {
	private Handler mHandler = new Handler();
	private List<CounterItem> mItemList;

	public void setValue(CounterItem... itemList) {
		List<CounterItem> list = new ArrayList<CounterItem>();
		for (CounterItem item : itemList) {

			list.add(item);
		}
		setValue(list);
	}

	public void setValue(List<CounterItem> list) {
		mItemList = list;
	}

	private Runnable mRunnable = new Runnable() {
		public void run() {
			mOnTimerCallback.onUpdate(mItemList);
			boolean needContinue = false;
			for (CounterItem item : mItemList) {
				if (item == null) {
					continue;
				}
				if (!item.isEnd()) {
					needContinue = true;
					item.timing();
				}
			}
			if (needContinue) {
				mHandler.postDelayed(mRunnable, 0);
			} else {
				stop();
			}
		}
	};

	public void start() {
		mIsRunning = true;
		mHandler.post(mRunnable);
	}

	private void checkEnd() {
		if (mOnTimerCallback != null) {
			mOnTimerCallback.onStop();
		}
	}

	private boolean mIsRunning;

	public boolean isStopped() {
		return !mIsRunning;
	}

	public void stop() {
		// goStop();
		checkEnd();
		mIsRunning = false;
	}

	public void pause() {
		// TODO: implement
	}

	public void cancel() {
		// TODO: implement
	}

	public interface OnCounterCallback {
		public void onStart();

		public void onStop();

		public void onPause();

		public void onUpdate(List<CounterItem> list);
	}

	private OnCounterCallback mOnTimerCallback;

	public void setOnTimerCallback(OnCounterCallback onTimerCallback) {
		mOnTimerCallback = onTimerCallback;
	}

}
