package com.niuan.common.ezyer.test;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.niuan.common.ezyer.R;
import com.niuan.common.ezyer.base.EzyerEntry;
import com.niuan.common.ezyer.base.fragment.EzyerPullListFragment;
import com.niuan.common.ezyer.base.fragment.EzyerSimpleFragment;
import com.niuan.common.ezyer.net.EzyerParseJsonRequest;
import com.niuan.common.ezyer.net.ResponseListener;
import com.niuan.common.ezyer.test.pojo.NetStruct;
import com.niuan.common.ezyer.ui.annotation.EzyerView;
import com.niuan.common.ezyer.ui.view.adapter.EzyerBaseListAdapter;
import com.niuan.common.ezyer.ui.view.adapter.EzyerDataViewAdapter;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

/**
 * Created by Carlos on 2015/9/14.
 */
public class MainFragment extends EzyerPullListFragment<MainFragment.TestFragmentViewHolder, NetStruct.Data> {
//    private static class EzyerViewHolder1 extends EzyerViewHolder {
//        @EzyerView(resourceId = R.id.main_banner, dataId = ItemList.ID_LIST_LIST)
//        InfiniteBannerView mBannerView;
//
//        public EzyerViewHolder1(View view) {
//            super(view);
//        }
//
//        @Override
//        protected void onInit() {
//            pair(NetStruct.ID_CODE, R.id.customer_item_list_id_1);
//            pair(ItemList.ID_LIST_NAME, R.id.customer_item_list_name_1);
//            pair(ItemList.ID_LIST_LIST, R.id.customer_products_1);
//            ListView list1 = findViewById(R.id.customer_products_1);
//            list1.setAdapter(new MainListAdapter(getView().getContext()));
//
//            mBannerView.setListAdapter(new MainListAdapter(getView().getContext()));
//            mBannerView.setDuration(10000);
//            mBannerView.startScroll();
//        }
//    }

    public static class TestFragmentViewHolder extends EzyerViewHolder {

        @EzyerView(resourceId = R.id.customer_products_1)
        ListView mListView;

        @EzyerView(resourceId = R.id.pull_to_refresh)
        PullToRefreshView mRefreshView;

        public TestFragmentViewHolder(View view) {
            super(view);
        }

        @Override
        protected void onInit() {
            pair(NetStruct.ID_CODE, R.id.customer_id);
            pair(NetStruct.ID_MSG, R.id.customer_name);
        }
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_main;
    }

    @Override
    protected int getListViewId() {
        return R.id.customer_products_1;
    }

    @Override
    protected int getPullViewId() {
        return R.id.pull_to_refresh;
    }

    @Override
    protected View initRefreshingView(Context context) {
        LinearLayout layout = new LinearLayout(getActivity());
        TextView view = new TextView(getView().getContext());
        view.setText("this is header");

        layout.addView(view);
        view = new TextView(getView().getContext());
        view.setText("this is header");

        layout.addView(view);

        return layout;
    }

    private EzyerDataViewAdapter<TestFragmentViewHolder, NetStruct> mDataViewAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EzyerEntry.init(getActivity());
        mDataViewAdapter = new EzyerDataViewAdapter<>(getViewHolder());
    }

    @Override
    protected TestFragmentViewHolder initViewHolder() {
        return new TestFragmentViewHolder(getView());
    }

    private <T> void request(final EzyerDataViewAdapter<? extends EzyerViewHolder, T> adapter, String url, Class<T> cls) {
        EzyerParseJsonRequest request = new EzyerParseJsonRequest(url, new ResponseListener<T>() {
            @Override
            public void onResponse(Request<T> request, T response, boolean fromCache) {
                Log.d("MainFragment", "fromCache = " + fromCache);
                adapter.setData(response);

                requestFinish(TYPE_REFRESH, (ArrayList<NetStruct.Data>) ((NetStruct) response).getData());
                mDataViewAdapter.getHolder().mRefreshView.setRefreshing(false);
            }
        }, cls);
        request.setShouldCache(true);
        request.execute();
    }

    private String mUrl = "http://192.168.1.100:8080/EzyerServer/json";

    @Override
    public void onResume() {
        super.onResume();
        getViewHolder().mRefreshView.setRefreshing(true);
        request(mDataViewAdapter, mUrl, NetStruct.class);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getViewHolder().mRefreshView.setRefreshing(true);
        request(mDataViewAdapter, mUrl, NetStruct.class);
    }

    @Override
    protected EzyerBaseListAdapter<NetStruct.Data> initListAdapter() {
        return new MainListAdapter(getView().getContext());
    }
}
