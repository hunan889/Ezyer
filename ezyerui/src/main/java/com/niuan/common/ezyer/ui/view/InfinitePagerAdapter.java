package com.niuan.common.ezyer.ui.view;

import android.widget.ListAdapter;

public class InfinitePagerAdapter extends BaseViewPagerAdapter {
	public static final int SCROLLABLE_PAGER_SIZE = Integer.MAX_VALUE;
	public static final int SINGLE_PAGE_SIZE = 1;

	public InfinitePagerAdapter(ListAdapter adapter) {
		super(adapter);
	}

	private int mActualSize;

	public void setAdapterSize(int size) {
		mActualSize = size;
		if (size > 1) {

			setPageSize(SCROLLABLE_PAGER_SIZE);
		} else if (size == 1) {
			setPageSize(SINGLE_PAGE_SIZE);
		} else {
			setPageSize(0);
		}

		int pageSize = getPageSize();
		setStartPos(pageSize / 2);

	}

	public boolean isScrollable() {
		return getPageSize() == SCROLLABLE_PAGER_SIZE;
	}

	public int getFirstItemPos() {
		return getStartPos();
	}
}