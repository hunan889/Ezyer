package com.niuan.common.ezyer.base.view;

import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

public abstract class MarkCurrentPagerAdapter extends PagerAdapter {

	private Object mCurrent;
	@Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrent = object;
    }
                                             
    public Object getPrimaryItem() {
        return mCurrent;
    }
}
