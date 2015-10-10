package com.niuan.common.ezyer.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.niuan.common.ezyer.R;
import com.niuan.common.ezyer.app.net.DishStructRequest;
import com.niuan.common.ezyer.app.pojo.Dish;
import com.niuan.common.ezyer.base.EzyerEntry;
import com.niuan.common.ezyer.base.fragment.EzyerDataViewFragment;
import com.niuan.common.ezyer.data.RefreshType;
import com.niuan.common.ezyer.net.EzyerCacheRequest;
import com.niuan.common.ezyer.ui.view.adapter.EzyerBaseListAdapter;
import com.niuan.common.ezyer.ui.view.holder.EzyerPullListViewHolder;

import java.util.ArrayList;

/**
 * Created by Carlos on 2015/9/14.
 */
public class MainFragment extends EzyerDataViewFragment<MainFragmentViewHolder, ArrayList<Dish>> implements EzyerPullListViewHolder.PullListViewListener {
    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EzyerEntry.init(getActivity());
    }

    @Override
    public void onVisibleToUser() {
        if (!hasData()) {
            requestData(RefreshType.Replace, new DishStructRequest().setExecuteType(EzyerCacheRequest.EXE_TYPE_CACHE_THEN_NET));
        }
    }

    @Override
    public void onRequestSuccess(Request<ArrayList<Dish>> request, RefreshType requestType, ArrayList<Dish> struct, boolean fromCache) {
        super.onRequestSuccess(request, requestType, struct, fromCache);
        getViewHolder().setRefreshing(false);
    }

    @Override
    public void onRequestError(VolleyError error) {
        super.onRequestError(error);
        getViewHolder().setRefreshing(false);
    }

    @Override
    protected MainFragmentViewHolder initRootViewHolder() {
        MainFragmentViewHolder holder = new MainFragmentViewHolder(getView());
        holder.setPullListViewListener(this);

        return holder;
    }

    @Override
    public void bindData(Request<ArrayList<Dish>> request, RefreshType refreshType, ArrayList<Dish> data) {
        super.bindData(request, refreshType, data);
        bindView(request, refreshType, getViewHolder().getListView(), data);
    }

    @Override
    protected ArrayList<Dish> mergeWithOldData(RefreshType refreshType, ArrayList<Dish> oldData, ArrayList<Dish> newData) {
        ArrayList<Dish> returnData = oldData;
        if (returnData == null) {
            returnData = new ArrayList<>();
        }
        switch (refreshType) {
            case Load: {
                if (newData != null) {
                    returnData.addAll(newData);
                }
                break;
            }
            case Replace: {
                returnData.clear();
                if (newData != null) {
                    returnData.addAll(newData);
                }
                break;
            }
            case Update: {
                if (newData != null) {
                    returnData.addAll(0, newData);
                }
                break;
            }
        }

        return returnData;
    }

    @Override
    public void onRefresh() {
        requestData(RefreshType.Update, new DishStructRequest());
    }

    @Override
    public void onLoading() {
        requestData(RefreshType.Load, new DishStructRequest());
    }

    @Override
    public void onItemClick(EzyerBaseListAdapter adapter, View view, int position) {
        Dish dish = (Dish) adapter.getDataSource().get(position);
        Uri uri = Uri.parse(dish.getDetailUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
