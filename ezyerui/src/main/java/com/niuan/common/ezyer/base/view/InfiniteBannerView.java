package com.niuan.common.ezyer.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListAdapter;

import com.niuan.common.ezyer.base.view.util.AutoAdjustHelper;

public class InfiniteBannerView extends BaseBannerView {

    private AutoAdjustHelper mHelper;
    // 视图切换的间隔
    private static final int DEFAULT_SCROLL_DELAY = 4000;

    private int mScrollDelay = DEFAULT_SCROLL_DELAY;

    public InfiniteBannerView(Context context) {
        super(context);
    }

    public InfiniteBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        if (mHelper == null) {
            mHelper = new AutoAdjustHelper();
        }
        mHelper.init(context, attrs);
        super.init(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        checkUserTouch(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        checkUserTouch(ev);
        return super.onTouchEvent(ev);
    }

    private void checkUserTouch(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                pauseAnimateInternal();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                animateInternal();
                break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHelper.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(mHelper.getWidthSpec(), mHelper.getHeightSpec());
    }

    @Override
    protected void notifyDataSetChanged() {
        ListAdapter adapter = getListAdapter();
        int count = 0;

        if (adapter != null) {
            count = getListAdapter().getCount();
        }
        setAdapter(null);
        mAdapter.setAdapterSize(count);
        setAdapter(mAdapter);
        super.notifyDataSetChanged();
        setCurrentItem(mAdapter.getFirstItemPos(), false);
        animateInternal();
    }

    @Override
    protected int getFirstItemPos() {
        if (mAdapter == null) {
            return super.getFirstItemPos();
        }
        return mAdapter.getFirstItemPos();
    }

    private InfinitePagerAdapter mAdapter;

    @Override
    public BaseViewPagerAdapter initInternalAdapter(ListAdapter adapter) {
        mAdapter = new InfinitePagerAdapter(adapter);
        return mAdapter;
    }

    /**
     * 是否是用户调用
     */
    public boolean mScrollEnabled;

    public void startScroll() {
        mScrollEnabled = true;
        animateInternal();
    }

    public void pauseScroll() {
        mScrollEnabled = false;
        pauseAnimateInternal();
    }

    public void setDuration(int duration) {
        mScrollDelay = duration;
    }

    private void pauseAnimateInternal() {
        removeCallbacks(mAnimateNextRunnable);
    }

    private void animateInternal() {
        if (mScrollEnabled && mAdapter != null && mAdapter.isScrollable()) {
            removeCallbacks(mAnimateNextRunnable);
            postDelayed(mAnimateNextRunnable, mScrollDelay);
        }
    }

    private Runnable mAnimateNextRunnable = new Runnable() {
        public void run() {
            int next = getCurrentItem() + 1;
            if (next >= mAdapter.getCount()) {
                next = mAdapter.getCount() - 1;
            }

            setCurrentItem(next);
            animateInternal();
        }

        ;
    };
}
