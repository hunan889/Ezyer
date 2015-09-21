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
public class EzyerPullViewHolder extends EzyerViewHolder implements PullToRefreshView.OnRefreshListener {
    private PullToRefreshView mPullToRefreshView;

    public EzyerPullViewHolder(@NonNull LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        super(inflater, parent, attachToParent);
        init();
    }

    public EzyerPullViewHolder(View view) {
        super(view);
        init();
    }

    private void init() {
        mPullToRefreshView = findViewById(initPullViewId());

        if (mPullToRefreshView != null) {
            mPullToRefreshView.setCustomHeaderView(initRefreshingView(getView().getContext(), mPullToRefreshView));
            mPullToRefreshView.setOnRefreshListener(this);
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

    @Override
    public void onRefresh() {

    }
}
