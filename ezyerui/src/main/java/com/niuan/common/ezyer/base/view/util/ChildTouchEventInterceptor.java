package com.niuan.common.ezyer.base.view.util;

import android.view.MotionEvent;

public interface ChildTouchEventInterceptor {
    boolean isIntercept(MotionEvent e);
}
