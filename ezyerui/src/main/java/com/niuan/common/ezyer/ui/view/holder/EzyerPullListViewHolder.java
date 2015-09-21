package com.niuan.common.ezyer.ui.view.holder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.niuan.common.ezyer.ui.view.adapter.EzyerBaseListAdapter;

/**
 * Created by Carlos on 2015/9/20.
 */
public abstract class EzyerPullListViewHolder extends EzyerPullViewHolder {
    private ListView mListView;
    private PullListViewListener mPullListViewListener;

    private int mStartY; // 用户手指按下时的位置
    private int mLastY; // 上次event时的位置，用于记录用户是向上还是向下滑
    private int mDirection = DIRECTION_NONE;
    public static final int DIRECTION_UP = -1;
    public static final int DIRECTION_NONE = 0;
    public static final int DIRECTION_DOWN = 1;
    private final static int OFFSET_SCROLL = 0;

    public EzyerPullListViewHolder(@NonNull LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        super(inflater, parent, attachToParent);
        init();
    }

    public EzyerPullListViewHolder(View view) {
        super(view);
        init();
    }

    public interface PullListViewListener {
        void onRefresh();

        void onLoading();

        void onItemClick(EzyerBaseListAdapter adapter, View view, int position);
    }

    protected abstract int initListViewId();

    protected void initListView(ListView listView) {

    }

    protected abstract EzyerBaseListAdapter initAdapter();

    public EzyerBaseListAdapter getListAdapter() {
        return (EzyerBaseListAdapter) mListView.getAdapter();
    }

    private void init() {
        mListView = findViewById(initListViewId());
        if (mListView == null) {
            return;
        }
        initListView(mListView);
        mListView.setAdapter(initAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPullListViewListener != null) {
                    mPullListViewListener.onItemClick(getListAdapter(), view, position);
                }
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE
                        && (isLoadingEnabled())
                        && (view.getLastVisiblePosition() >= view.getCount() - 1)
                        && mDirection == DIRECTION_UP) {
                    if (mPullListViewListener != null) {
                        mPullListViewListener.onLoading();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent ev) {

                int currentY = (int) ev.getY();
                int action = ev.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        mLastY = currentY;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if (mLastY - currentY > OFFSET_SCROLL) {
                            // Log.d(TAG, "mDirection = DIRECTION_UP");
                            mDirection = DIRECTION_UP;
                        } else if (mLastY - currentY < -OFFSET_SCROLL) {
                            // Log.d(TAG, "mDirection = DIRECTION_DOWN");
                            mDirection = DIRECTION_DOWN;
                        }
                        mLastY = currentY;
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }

    public ListView getListView() {
        return mListView;
    }

    public void setPullListViewListener(PullListViewListener listener) {
        mPullListViewListener = listener;
    }

    @Override
    public void onRefresh() {
        if (mPullListViewListener != null) {
            mPullListViewListener.onRefresh();
        }
    }

    protected boolean isLoadingEnabled() {
        return true;
    }

}
