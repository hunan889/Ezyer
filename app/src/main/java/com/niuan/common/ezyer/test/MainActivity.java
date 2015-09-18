package com.niuan.common.ezyer.test;

import android.support.v4.app.Fragment;

import com.niuan.common.ezyer.base.EzyerSingleFragmentActivity;
import com.niuan.common.ezyernet.EzyerVolleyManager;

/**
 * Created by Carlos on 2015/8/14.
 */
public class MainActivity extends EzyerSingleFragmentActivity {
    @Override
    protected Fragment initFragment() {
        EzyerVolleyManager.init(this);
        return new MainFragment();
    }
}
