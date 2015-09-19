package com.niuan.common.ezyer.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.niuan.common.ezyer.R;
import com.niuan.common.ezyer.base.EzyerEntry;
import com.niuan.common.ezyer.base.fragment.EzyerSimpleFragment;
import com.niuan.common.ezyer.net.EzyerParseJsonRequest;
import com.niuan.common.ezyer.net.ResponseListener;
import com.niuan.common.ezyer.test.pojo.NetStruct;
import com.niuan.common.ezyer.ui.annotation.EzyerView;
import com.niuan.common.ezyer.ui.view.adapter.EzyerDataViewAdapter;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;
import com.yalantis.phoenix.PullToRefreshView;

/**
 * Created by Carlos on 2015/9/14.
 */
public class MainFragment extends EzyerSimpleFragment {
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

    private static class TestFragmentViewHolder extends EzyerViewHolder {

        @EzyerView(dataId = NetStruct.ID_LIST, resourceId = R.id.customer_products_1)
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

            mListView.setAdapter(new MainListAdapter(getView().getContext()));

//            EzyerRefreshView view = new EzyerRefreshView(getView().getContext(), mRefreshView);
//            view.setView(findViewById(R.id.list_header));

            LinearLayout layout = new LinearLayout(getView().getContext());

            TextView view = new TextView(getView().getContext());
            view.setText("this is header");

            layout.addView(view);
            view = new TextView(getView().getContext());
            view.setText("this is header");

            layout.addView(view);
            mRefreshView.setCustomHeaderView(layout);
        }
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_main;
    }

    private EzyerDataViewAdapter<TestFragmentViewHolder, NetStruct> mDataViewAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EzyerEntry.init(getActivity());

        mDataViewAdapter = new EzyerDataViewAdapter<>(new TestFragmentViewHolder(getView()));
    }

    private <T> void request(final EzyerDataViewAdapter<? extends EzyerViewHolder, T> adapter, String url, Class<T> cls) {
        EzyerParseJsonRequest request = new EzyerParseJsonRequest(url, new ResponseListener<T>() {
            @Override
            public void onResponse(Request<T> request, T response, boolean fromCache) {
                Log.d("MainFragment", "fromCache = " + fromCache);
                adapter.setData(response);
                mDataViewAdapter.getHolder().mRefreshView.setRefreshing(false);
            }
        }, cls);
        request.setTtl(30000).setSoftTtl(20000).setShouldCache(true);
        request.execute();

    }

    @Override
    public void onResume() {
        super.onResume();
        String url = "http://192.168.1.101:8080/EzyerServer/json";
        request(mDataViewAdapter, url, NetStruct.class);
        mDataViewAdapter.getHolder().mRefreshView.setRefreshing(true);
        mDataViewAdapter.getHolder().mRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String url = "http://192.168.1.101:8080/EzyerServer/json";
                request(mDataViewAdapter, url, NetStruct.class);
                mDataViewAdapter.getHolder().mRefreshView.setRefreshing(true);
            }
        });
    }
}
