package com.niuan.common.ezyer.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by chaoqun_liu on 2015/5/21.
 */
public class EasyTimer {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mRunnable != null) {
                mRunnable.run();
                sendEmptyMessageDelayed(0, mDuration);
            }
        }
    };
    private int mDuration;
    private Runnable mRunnable;

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setRunnable(Runnable run) {
        mRunnable = run;
    }

    public void stop() {
        mHandler.removeMessages(0);
        mRunnable = null;
    }

    public void start() {
        mHandler.sendEmptyMessage(0);
    }

    public void resetAndStart(int duration, Runnable run) {
        stop();
        setRunnable(run);
        setDuration(duration);
        start();
    }
}
