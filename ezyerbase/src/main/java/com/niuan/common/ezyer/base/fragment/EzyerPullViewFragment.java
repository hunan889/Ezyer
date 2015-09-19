package com.niuan.common.ezyer.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;
import com.yalantis.phoenix.PullToRefreshView;

/**
 * Created by Carlos Liu on 2015/9/19.
 */
public abstract class EzyerPullViewFragment<T extends EzyerViewHolder> extends EzyerSimpleFragment<T>
        implements PullToRefreshView.OnRefreshListener {

    private PullToRefreshView mPullToRefreshView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPullToRefreshView = findViewById(getPullViewId());

        if (mPullToRefreshView != null) {
            mPullToRefreshView.setOnRefreshListener(this);
            mPullToRefreshView.setCustomHeaderView(initRefreshingView(getActivity()));
        }
    }

    protected int getPullViewId() {
        return 0;
    }

    protected abstract View initRefreshingView(Context context);

    @Override
    public void onRefresh() {

    }
}
