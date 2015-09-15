package com.niuan.common.ezyer.base.util.dl.thread;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 以队列的方式执行任务，会调用任务的doInBackground方法用于执行后台任务，当doInBackground执行完成之后，
 * 会调用onPostExecuted方法 可以使用pause方法暂停后台线程的执行，需要时调用resume来恢复运行
 *
 * 每个Executor都有唯一的线程，Task在相同id的executor里执行会运行在相同的线程中。
 *
 * @author carlosliu
 *
 */
public class TaskExecutor {
	public enum Status {
		RESUME, PAUSE,
	}

	private Handler mNonUiHandler;
	private HandlerThread mNonUiThread;
	private Handler mUiHandler;
	public static final String TAG = "TaskExecutor";

	private static class UiHandler extends Handler {
		private WeakReference<TaskExecutor> mRef;

		public UiHandler(TaskExecutor executor) {
			super(Looper.getMainLooper());
			mRef = new WeakReference<TaskExecutor>(executor);
		}

		@Override
		public void handleMessage(Message msg) {
			TaskExecutor executor = mRef.get();
			if (executor == null) {
				return;
			}

			switch (msg.what) {
				case MSG_EXECUTE_TASK: {
					executor.uiHandlerExecute((BackgroundTask<?, ?, ?>) msg.obj);

					break;
				}
				case MSG_QUIT: {
					executor.doQuit();
					break;
				}
			}
		}
	}

	private static class NonUiHandler extends Handler {
		private WeakReference<TaskExecutor> mRef;

		public NonUiHandler(TaskExecutor executor, Looper looper) {
			super(looper);
			mRef = new WeakReference<TaskExecutor>(executor);
		}

		@Override
		public void handleMessage(Message msg) {
			TaskExecutor executor = mRef.get();
			if (executor == null) {
				return;
			}

			switch (msg.what) {
				case MSG_EXECUTE_TASK: {
					executor.nonUiHandlerExecute((BackgroundTask<?, ?, ?>) msg.obj);
					break;
				}
			}
		}
	}

	private static final int MSG_EXECUTE_TASK = 1001;
	private static final int MSG_QUIT = 1002;

	private static final int DURATION_AUTO_QUIT = 1 * 60 * 1000; // 没有操作时自动退出的间隔

	@SuppressLint("UseSparseArrays")
	private static Map<Integer, TaskExecutor> mExecutorPool = new HashMap<Integer, TaskExecutor>();
	private static final String THREAD_NAME = "AsyncTaskExecutorThread";

	private int mId;
	private ThreadSwitcher mSyncObj;

	public synchronized static int generateNewId() {
		int id = 2000;

		while (mExecutorPool.get(id) != null) {
			id++;
		}
		return id;
	}

	public synchronized static boolean isBusy(int id) {
		if (mExecutorPool.containsKey(id)) {
			if (getInstance(id).isBusy()) {
				return true;
			}
		}
		return false;
	}

	public synchronized static TaskExecutor getFreeOrGenerate() {
		TaskExecutor executor = getFree();
		if (executor == null) {
			int id = generateNewId();
			executor = getInstance(id);
		}

		return executor;
	}

	public synchronized static TaskExecutor getFree() {
		for (int id : mExecutorPool.keySet()) {
			TaskExecutor executor = mExecutorPool.get(id);
			if (!executor.isBusy()) {
				return executor;
			}
		}

		return null;
	}

	public synchronized static void removeExecutor(int id) {
		mExecutorPool.remove(id);
	}

	private TaskExecutor(int id) {
		setId(id);
		initHandler(id);
	}

	private void initHandler(int id) {
		initNonUiHandler(id);
		initUiHandler();
	}

	private void checkInitHandler() {
		if (mNonUiThread.isAlive()) {
			return;
		}

		initHandler(mId);
	}

	private void uiHandlerExecute(BackgroundTask<?, ?, ?> task) {

		markTaskFinish();
		if (task != null) {
			onPostExecute(task);
		}
		checkQuit(); // 每个任务执行完成之后检查是否还有任务，如果在一段时间内没有需要执行的任务，则自动退出线程
	}

	private void nonUiHandlerExecute(BackgroundTask<?, ?, ?> task) {
		if (task == null) {
			return;
		}
		try {
			Log.v("TestOnStart", "taskExecutorId : " + mId);
			doInBackground(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Message uiMessage = mUiHandler.obtainMessage(MSG_EXECUTE_TASK);
		uiMessage.obj = task;
		uiMessage.setTarget(mUiHandler);
		uiMessage.sendToTarget();
	}

	private void checkQuit() {
		if (!isBusy()) {
			Log.d(TAG,
					"[checkQuit], current executor is free now, MSG_QUIT will be executed in "
							+ DURATION_AUTO_QUIT
							+ "ms if no messages sent again");
			mUiHandler.sendEmptyMessageDelayed(MSG_QUIT, DURATION_AUTO_QUIT);
		}
	}

	private void cancelQuit() {
		if (mUiHandler.hasMessages(MSG_QUIT)) {
			Log.d(TAG,
					"[cancelQuit], remove MSG_QUIT for somebody called cancelQuit");
			mUiHandler.removeMessages(MSG_QUIT);
		}
	}

	private void doQuit() {
		Log.d(TAG, "[doQuit] enter");
		synchronized (this) {
			if (!isBusy()) {
				Log.d(TAG,
						"[doQuit] execute indeed, thread:"
								+ mNonUiThread.getName() + " will be quited!!");
				mNonUiThread.quit();

				removeExecutor(getId());
			}
		}
	}

	private void initUiHandler() {
		mUiHandler = new UiHandler(this);
	}

	private void initNonUiHandler(int id) {
		mSyncObj = new ThreadSwitcher();

		mNonUiThread = new HandlerThread(THREAD_NAME + "#" + id);
		mNonUiThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
		mNonUiThread.start();
		mNonUiHandler = new NonUiHandler(this, mNonUiThread.getLooper());
	}

	/**
	 * 通过executorId来获取executor
	 *
	 * @param executorId
	 * @return
	 */
	public static TaskExecutor getInstance(int executorId) {

		TaskExecutor executor = null;
		synchronized (TaskExecutor.class) {
			executor = mExecutorPool.get(executorId);
		}
		if (executor == null) {
			synchronized (TaskExecutor.class) {
				if (executor == null) {
					executor = new TaskExecutor(executorId);
					mExecutorPool.put(executorId, executor);
				}
			}
		}

		return executor;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	private Message getExecuteTaskMsg(BackgroundTask<?, ?, ?> task) {
		Message msg = mNonUiHandler.obtainMessage(MSG_EXECUTE_TASK);
		msg.obj = task;
		return msg;
	}

	public void executeNow(BackgroundTask<?, ?, ?> task) {
		execute(task, true);
	}

	public void execute(BackgroundTask<?, ?, ?> task) {
		execute(task, false);
	}

	public void execute(BackgroundTask<?, ?, ?> task, boolean front) {
		cancelQuit();
		checkInitHandler();
		resume();
		queue(task, front);
		markTaskAdded();
	}

	public void queue(BackgroundTask<?, ?, ?> task, boolean front) {
		if (task != null) {
			task.onPreExecute(isBusy());
		}
		Message msg = getExecuteTaskMsg(task);
		if (front) {
			mNonUiHandler.sendMessageAtFrontOfQueue(msg);
		} else {
			mNonUiHandler.sendMessage(msg);
		}
	}

	private volatile int mTaskCount;

	private synchronized void markTaskAdded() {
		mTaskCount++;
	}

	private synchronized void markTaskFinish() {
		mTaskCount--;
	}

	/**
	 *
	 * @return 是否有任务在执行
	 */
	public synchronized boolean isBusy() {
		boolean isBusy = mTaskCount != 0;
		Log.d(TAG, "[isBusy] executor id = " + getId() + ", isBusy = " + isBusy);
		return isBusy;
	}

	public void pause() {
		synchronized (this) {
			mStatus = Status.PAUSE;
		}
	}

	public void resume() {
		synchronized (this) {
			if (mStatus == Status.RESUME) {
				return;
			}
			mStatus = Status.RESUME;
			doResume();
		}
	}

	public void remove(BackgroundTask<?, ?, ?> task) {
		if (task != null) {
			mNonUiHandler.removeMessages(MSG_EXECUTE_TASK, task);
		}
	}

	private void doPause() {
		boolean needPause = false;
		synchronized (this) {
			if (mStatus == Status.PAUSE) {
				needPause = true;
			}
		}

		if (needPause) {
			mSyncObj.pause();
		}

	}

	private void doResume() {
		mSyncObj.resume();
	}

	/**
	 * 检查是否需要暂停，当用户调用了pause或者resume时，会对executor的状态进行改变。
	 * 该方法会在状态为PAUSE的时候对线程进行阻塞，用户需要调用resume来唤起线程继续工作
	 */
	private void checkStatus() {
		Status status;
		synchronized (this) {
			status = mStatus;
		}
		switch (status) {
			case RESUME: {
				break;
			}
			case PAUSE: {
				doPause();
				break;
			}
		}
	}

	private Status mStatus = Status.PAUSE;

	private void doInBackground(BackgroundTask<?, ?, ?> task)
			throws InterruptedException {
		checkStatus();

		if (task != null) {
			task.callbackFromBackgroundThread();
		}
	}

	private void onPostExecute(BackgroundTask<?, ?, ?> task) {
		if (task != null) {
			task.onPostExecute();
		}
	}

	public void runOnUiThread(Runnable runnable) {
		mUiHandler.post(runnable);
	}

	public void runOnBgThread(Runnable runnable) {
		mNonUiHandler.post(runnable);
	}
}
