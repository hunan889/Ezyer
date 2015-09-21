package com.niuan.common.ezyer.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.niuan.common.ezyer.R;
import com.niuan.common.ezyer.app.net.DishStructRequest;
import com.niuan.common.ezyer.app.net.NetStructRequest;
import com.niuan.common.ezyer.app.pojo.Dish;
import com.niuan.common.ezyer.base.EzyerEntry;
import com.niuan.common.ezyer.base.fragment.EzyerDataViewFragment;
import com.niuan.common.ezyer.data.RefreshType;
import com.niuan.common.ezyer.ui.view.adapter.EzyerBaseListAdapter;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;
import com.yalantis.phoenix.PullToRefreshView;

/**
 * Created by Carlos on 2015/9/14.
 */
public class MainFragment extends EzyerDataViewFragment<MainFragmentViewHolder> {
    @Override
    protected int getResourceId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EzyerEntry.init(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewHolder().setRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest(RefreshType.Update, new DishStructRequest());
            }
        });

        getViewHolder().getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EzyerBaseListAdapter adapter = (EzyerBaseListAdapter) parent.getAdapter();

                Dish dish = (Dish) adapter.getDataSource().get(position);
                Uri uri = Uri.parse(dish.getDetailUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        sendRequest(RefreshType.Replace, new DishStructRequest());
    }

    @Override
    public void requestFinish(RefreshType requestType, Object struct, Object... params) {
        super.requestFinish(requestType, struct, params);
        getViewHolder().setRefreshing(false);
    }

    @Override
    public void requestError(VolleyError error) {
        super.requestError(error);
        getViewHolder().setRefreshing(false);
    }

    @Override
    protected MainFragmentViewHolder initViewHolder() {
        return new MainFragmentViewHolder(getView());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void bindView(RefreshType refreshType, MainFragmentViewHolder holder, Object o, Object... params) {
        super.bindView(refreshType, holder, o, params);
        getDataViewAdapter().bindView(refreshType, holder.getListView(), o);
    }
}
