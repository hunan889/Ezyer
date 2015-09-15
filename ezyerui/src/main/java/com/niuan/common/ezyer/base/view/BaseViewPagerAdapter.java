package com.niuan.common.ezyer.base.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class BaseViewPagerAdapter extends MarkCurrentPagerAdapter {

	public static final int CACHE_VIEW_SIZE = 4;

	private List<ItemInfo> mViewList = new ArrayList<ItemInfo>(CACHE_VIEW_SIZE);
	private int mPageSize;
	private int mStartPos;

	public int getPageSize() {
		return mPageSize;
	}

	public void setPageSize(int pageSize) {
		this.mPageSize = pageSize;
	}

	public int getStartPos() {
		return mStartPos;
	}

	public void setStartPos(int startPos) {
		this.mStartPos = startPos;
	}

	private ListAdapter mListAdapter;
	private BaseBannerView.OnBannerItemClickListener mOnItemClickListener;

	public BaseViewPagerAdapter(ListAdapter adapter) {
		mListAdapter = adapter;
	}

	public void setOnItemClickListener(
			BaseBannerView.OnBannerItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}

	public void setAdapterSize(int size) {
		mPageSize = size;

	}

	@Override
	public void notifyDataSetChanged() {
		for (ItemInfo info : mViewList) {
			if (info != null) {
				info.position = POSITION_NONE;
			}
		}

		setAdapterSize(mListAdapter.getCount());
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mPageSize;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	private int getViewPos(int position) {
		return getRelativePos(position, CACHE_VIEW_SIZE);
	}

	public int getRelativeItemPos(int position) {
		if (mListAdapter == null) {
			return 0;
		}
		int count = mListAdapter.getCount();
		if (count <= 0) {
			return 0;
		}
		return getRelativePos(position, count);
	}

	private int getRelativePos(int position, int size) {

		int relativePos = position - mStartPos;
		int returnPos = 0;
		if (relativePos >= 0) {
			returnPos = relativePos % size;
		} else {
			returnPos = (size + relativePos % size) % size;
		}
		// Log.d(TAG, "[getRelativePos] position = " + position
		// + ", returnPos = " + returnPos + ", size = " + size);

		return returnPos;
	}

	private View getCurrentView(int position) {
		int viewPos = getViewPos(position);
		View view = null;
		if (mViewList.size() > viewPos) {
			ItemInfo info = mViewList.get(viewPos);
			if (info != null) {
				view = info.obj;
			}
		}
		return view;
	}

	private void cacheCurrentView(int position, View view) {

		int viewPos = getViewPos(position);

		if (viewPos >= mViewList.size()) {
			ItemInfo info = new ItemInfo();
			info.obj = view;
			info.position = position;
			for (int i = mViewList.size(); i < viewPos; i++) {
				mViewList.add(new ItemInfo());
			}
			mViewList.add(info);
		} else {
			ItemInfo info = mViewList.get(viewPos);
			if (info == null) {
				info = new ItemInfo();
				mViewList.set(viewPos, info);
			}
			if (info.position == position && view.equals(info.obj)) {
				info.position = POSITION_UNCHANGED;
			}
			info.obj = view;
		}
	}

	@Override
	public int getItemPosition(Object object) {
		int position = POSITION_NONE;
		for (ItemInfo info : mViewList) {

			if (object.equals(info.obj)) {
				position = info.position;
			}
		}
		return position;
	}

	@Override
	public float getPageWidth(int position) {
		// TODO Auto-generated method stub
		return super.getPageWidth(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	/**
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View returnView = getView(container, position);
		container.addView(returnView);

		// getView(container, position++);
		return returnView;
	}

	/**
	 * TODO: Bug，不支持多种类型的view type，即getItemViewType无法回传正确的convertView
	 *
	 * @param parent
	 * @param position
	 * @return
	 */
	private View getView(ViewGroup parent, int position) {
		View convertView = null;//getCurrentView(position);
		final int itemPos = getRelativeItemPos(position);
		View returnView = mListAdapter.getView(itemPos, convertView, parent);
		if (returnView.getParent() != null) {
			((ViewGroup) returnView.getParent()).removeView(returnView);
		}

//		cacheCurrentView(position, returnView);

		returnView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mOnItemClickListener != null) {
					mOnItemClickListener.onBannerItemClick(v, mListAdapter,
							itemPos);
				}
			}
		});
		return returnView;
	}

	private class ItemInfo {
		View obj;
		int position;
	}
}
