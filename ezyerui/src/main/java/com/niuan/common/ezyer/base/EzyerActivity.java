package com.niuan.common.ezyer.base;

import android.support.v4.app.FragmentActivity;

/**
 * Created by chaoqun_liu on 2015/8/13.
 */
public class EzyerActivity extends FragmentActivity {

    private <T> T quickFindViewById(int resId) {
        return (T) findViewById(resId);
    }
}
