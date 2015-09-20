package com.niuan.common.ezyer.ui.view.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yalantis.phoenix.PullToRefreshView;

/**
 * Created by Carlos on 2015/9/20.
 */
public class EzyerPullViewHolder extends EzyerViewHolder {
    private PullToRefreshView mPullToRefreshView;

    public EzyerPullViewHolder(@NonNull LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        super(inflater, parent, attachToParent);
    }

    public EzyerPullViewHolder(View view) {
        super(view);
    }

    @Override
    protected void onInit() {
        super.onInit();
        mPullToRefreshView = findViewById(initPullViewId());

        if (mPullToRefreshView != null) {
            mPullToRefreshView.setCustomHeaderView(initRefreshingView(getView().getContext(), mPullToRefreshView));
        }

    }

    public void setRefreshListener(PullToRefreshView.OnRefreshListener listener) {
        if (mPullToRefreshView != null) {
            mPullToRefreshView.setOnRefreshListener(listener);
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (mPullToRefreshView != null) {
            mPullToRefreshView.setRefreshing(refreshing);
        }
    }

    protected int initPullViewId() {
        return 0;
    }

    protected View initRefreshingView(Context context, PullToRefreshView refreshParentView) {
        return null;
    }

    public PullToRefreshView getPullToRefreshView() {
        return mPullToRefreshView;
    }
}
