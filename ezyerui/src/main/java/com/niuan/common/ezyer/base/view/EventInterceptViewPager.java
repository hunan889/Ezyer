package com.niuan.common.ezyer.base.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.niuan.common.ezyer.base.view.util.ChildTouchEventInterceptor;

public class EventInterceptViewPager extends ViewPager {

    public EventInterceptViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventInterceptViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        if (isChildIntercept(ev)) {
            return false;
        }
        try {
            isIntercept = super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean ret = false;
        try {
            ret = super.onTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private boolean isChildIntercept(MotionEvent ev) {
        MarkCurrentPagerAdapter adapter = getAdapter();
        if (adapter == null) {
            return false;
        }
        Object current = adapter.getPrimaryItem();
        return isIntercept(ev, current);
    }

    @Override
    public MarkCurrentPagerAdapter getAdapter() {
        return (MarkCurrentPagerAdapter) super.getAdapter();
    }

    private boolean isIntercept(MotionEvent ev, Object object) {
        boolean isIntercept = false;

        if (object instanceof ChildTouchEventInterceptor) {
            ChildTouchEventInterceptor intercept = (ChildTouchEventInterceptor) object;
            if (intercept.isIntercept(ev)) {
                return true;
            }
        }
        if (object instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) object;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                isIntercept = isIntercept || isIntercept(ev, child);
                if (isIntercept) {
                    break;
                }
            }
        }

        return isIntercept;
    }

}
