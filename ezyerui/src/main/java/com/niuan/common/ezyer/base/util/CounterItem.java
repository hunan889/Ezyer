package com.niuan.common.ezyer.base.util;

import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class CounterItem {

    public static final int DEFAULT_DURATION = 200;
    private float mStart;
    private float mEnd;
    private float mValue;
    private int mDuration;
    private long mStartTime;

    public float getStart() {
        return mStart;
    }

    public float getEnd() {
        return mEnd;
    }

    public float getValue() {
        return mValue;
    }

    public int getDuration() {
        return mDuration;
    }

    public void timing() {
        long currentTime = System.currentTimeMillis();
        if (mStartTime == 0) {
            mStartTime = currentTime;
        }
        timingTo(currentTime);
    }

    public void timingTo(long currentTime) {

        if (currentTime >= mStartTime + mDuration) {
            mValue = mEnd;
            return;
        }

        int timePassed = (int) (currentTime - mStartTime);
        float floatAverg = (float) timePassed / (float) mDuration;
        float t1 = mInterpolator.getInterpolation(floatAverg);
        mValue = mStart - (mStart - mEnd) * t1;
    }

    public boolean isEnd() {
        return mValue == mEnd;
    }

    private static final String TAG = "TimerItem";

    private Interpolator mInterpolator;
    private static final Interpolator DEFAULT_INTERPOLATOR = new DecelerateInterpolator();

    public CounterItem(float start, float end, int duration,
                       Interpolator interpolator) {
        if (interpolator == null) {
            interpolator = DEFAULT_INTERPOLATOR; // 默认使用减速效果
        }
        mInterpolator = interpolator;
        mStart = start;
        mEnd = end;
        mDuration = duration;
        mValue = mStart;

        Log.d(TAG, "start = " + mStart + ", mEnd = " + mEnd);
    }

    public CounterItem(float start, float end) {
        this(start, end, null);
    }

    public CounterItem(float start, float end, Interpolator interpolator) {
        this(start, end, DEFAULT_DURATION, interpolator);
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }
}
