package com.niuan.common.ezyer.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * 接收ListAdapter的viewpager, 使用getView获取Viewpager里面的视图
 *
 * @author liuchaoqun
 *
 */
@SuppressLint("RtlHardcoded")
public class BaseBannerView extends EventInterceptViewPager {

	// 视图切换时的滚动速度
	private static final int SCROLL_SPEED = 1000;

	// private static final String TAG = "Banner";
	private ListAdapter mListAdapter;
	private BaseViewPagerAdapter mAdapter;
	// private ViewPager mViewPager;

	private DataSetObserver mObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			notifyDataSetChanged();
			super.onChanged();
		}

		@Override
		public void onInvalidated() {
			notifyDataSetChanged();
			super.onInvalidated();
		}

	};

	protected void notifyDataSetChanged() {
		mAdapter.notifyDataSetChanged();
	}

	public BaseBannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public BaseBannerView(Context context) {
		super(context);
		init(context, null);
	}

	protected void init(Context context, AttributeSet attrs) {
		setGestureListener(new SimpleOnGestureListener() {

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				if (mOnBannerItemClickListener != null) {
					mOnBannerItemClickListener.onBannerItemClick(
							BaseBannerView.this, mListAdapter,
							mAdapter.getRelativeItemPos(getCurrentItem()));
				}
				return super.onSingleTapConfirmed(e);
			}
		});

		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			FixedSpeedScroller mScroller = new FixedSpeedScroller(getContext(),
					new Interpolator() {
						public float getInterpolation(float t) {
							t -= 1.0f;
							return t * t * t * t * t + 1.0f;
						}
					});
			mField.set(this, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private SimpleOnGestureListener mMouseGestureListener;
	private GestureDetector mGestureDetector;

	public void setGestureListener(SimpleOnGestureListener mouseGestureListener) {
		mMouseGestureListener = mouseGestureListener;
		mGestureDetector = new GestureDetector(getContext(),
				mMouseGestureListener);
	}

	protected BaseViewPagerAdapter initInternalAdapter(ListAdapter adapter) {
		return new BaseViewPagerAdapter(adapter);
	}

	protected BaseViewPagerAdapter getInternalAdapter() {
		return mAdapter;
	}

	private class FixedSpeedScroller extends Scroller {
		private int mDuration = SCROLL_SPEED;

		public FixedSpeedScroller(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy,
								int duration) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			// Ignore received duration, use fixed one instead
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		public void setDuration(int time) {
			mDuration = time;
		}
	}

	public ListAdapter getListAdapter() {
		return mListAdapter;
	}

	public void setListAdapter(ListAdapter adapter) {
		if (mListAdapter != null) {
			mListAdapter.unregisterDataSetObserver(mObserver);
		}
		mListAdapter = adapter;

		if (adapter != null) {
			adapter.registerDataSetObserver(mObserver);
			if (mAdapter == null) {
				mAdapter = initInternalAdapter(mListAdapter);
			}
			mAdapter.setAdapterSize(mListAdapter.getCount());
			super.setAdapter(mAdapter);
			setCurrentItem(getFirstItemPos());
		} else {
			super.setAdapter(null);
		}
	}

	protected int getFirstItemPos() {
		return 0;
	}

	private OnBannerItemClickListener mOnBannerItemClickListener;

	public void setOnItemClickListener(OnBannerItemClickListener listener) {
		// if (mAdapter == null) {
		// return;
		// }
		// mAdapter.setOnItemClickListener(listener);
		mOnBannerItemClickListener = listener;
	}

	public interface OnBannerItemClickListener {
		public void onBannerItemClick(View view, ListAdapter adapter,
									  int position);
	}

	private OnFlingListener mOnFlingListener;

	public void setOnFlingListener(OnFlingListener listener) {
		mOnFlingListener = listener;
	}

	public interface OnFlingListener {
		public void onFling(Direction dir, int curPos);
	}

	public enum Direction {
		UP, DOWN, LEFT, RIGHT, NONE
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean isGestureDetected = false;
		if (mGestureDetector != null) {
			isGestureDetected = mGestureDetector.onTouchEvent(ev);
		}
		if (isGestureDetected) {
			return true;
		}

		switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE: {
				xMove = ev.getRawX();
				yMove = ev.getRawY();
				// 活动的距离
				int distanceX = (int) (xMove - xDown);
				int distanceY = (int) (yDown - yMove);
				// 当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity

				if (super.onInterceptTouchEvent(ev)) {
					Direction dir = Direction.NONE;

					if (Math.abs(distanceX) > Math.abs(distanceY)) {
						if (distanceX > V) {

							dir = Direction.LEFT;
						} else if (distanceX < -V) {

							dir = Direction.RIGHT;
						}
					} else {
						if (distanceY > V) {
							dir = Direction.UP;
						} else if (distanceY < -V) {
							dir = Direction.DOWN;
						}
					}

					if (mOnFlingListener != null) {
						mOnFlingListener.onFling(dir, getCurrentItem());
					}
					return true;
				}

				break;
			}

			case MotionEvent.ACTION_DOWN: {
				xDown = ev.getRawX();
				yDown = ev.getRawY();
				break;
			}

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
			/* Release the drag */
				break;
			case MotionEvent.ACTION_POINTER_UP:
				break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	private static final int V = 8;
	// 记录手指按下时的横坐标。
	private float xDown;
	private float yDown;
	// 记录手指移动时的横坐标。
	private float xMove;
	private float yMove;

}
