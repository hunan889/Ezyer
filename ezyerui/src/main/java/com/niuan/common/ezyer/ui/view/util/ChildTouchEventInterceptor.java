package com.niuan.common.ezyer.ui.view.util;

import android.view.MotionEvent;

public interface ChildTouchEventInterceptor {
    boolean isIntercept(MotionEvent e);
}
