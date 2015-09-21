package com.niuan.common.ezyer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.niuan.common.ezyer.R;
import com.niuan.common.ezyer.app.pojo.NetStruct;
import com.niuan.common.ezyer.ui.view.adapter.EzyerBaseListAdapter;
import com.niuan.common.ezyer.ui.view.holder.EzyerPullListViewHolder;
import com.yalantis.phoenix.PullToRefreshView;

/**
 * Created by Carlos on 2015/9/20.
 */
public class MainFragmentViewHolder extends EzyerPullListViewHolder {

    public MainFragmentViewHolder(View view) {
        super(view);
    }

    @Override
    protected int initListViewId() {
        return R.id.customer_products_1;
    }

    @Override
    protected int initPullViewId() {
        return R.id.pull_to_refresh;
    }

    @Override
    protected EzyerBaseListAdapter initAdapter() {
        return new MainListAdapter(getView().getContext());
    }

    @Override
    protected View initRefreshingView(Context context, PullToRefreshView view) {
        return LayoutInflater.from(context).inflate(R.layout.header_refreshing, view, false);
    }
}
