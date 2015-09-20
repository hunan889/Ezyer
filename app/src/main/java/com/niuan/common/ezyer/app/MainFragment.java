package com.niuan.common.ezyer.app;

import android.os.Bundle;
import android.view.View;

import com.niuan.common.ezyer.R;
import com.niuan.common.ezyer.app.net.NetStructRequest;
import com.niuan.common.ezyer.base.EzyerEntry;
import com.niuan.common.ezyer.base.fragment.EzyerDataViewFragment;
import com.niuan.common.ezyer.data.RefreshType;
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
                sendRequest(RefreshType.Update, new NetStructRequest());
            }
        });
    }

    @Override
    public void requestFinish(RefreshType requestType, Object struct, Object... params) {
        super.requestFinish(requestType, struct, params);
        getViewHolder().setRefreshing(false);
    }

    @Override
    protected MainFragmentViewHolder initViewHolder() {
        return new MainFragmentViewHolder(getView());
    }

    @Override
    public void onResume() {
        super.onResume();
        sendRequest(RefreshType.Replace, new NetStructRequest());
    }
}
