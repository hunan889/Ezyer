package com.niuan.common.ezyer.test;

import android.support.v4.app.Fragment;

import com.niuan.common.ezyer.base.EzyerSingleFragmentActivity;

/**
 * Created by Carlos on 2015/8/14.
 */
public class MainActivity extends EzyerSingleFragmentActivity {
    @Override
    protected Fragment initFragment() {
        return new MainFragment();
    }
}
