package com.niuan.common.ezyer.base.fragment;

import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.niuan.common.ezyer.data.RefreshType;
import com.niuan.common.ezyer.net.EzyerParseJsonRequest;
import com.niuan.common.ezyer.net.EzyerRequest;
import com.niuan.common.ezyer.net.ResponseListener;
import com.niuan.common.ezyer.ui.view.adapter.EzyerDataViewAutoBinder;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;

/**
 * Created by Carlos on 2015/9/20.
 */
public abstract class EzyerDataViewFragment<HOLDER extends EzyerViewHolder, DATA> extends EzyerSimpleFragment<HOLDER> {

    private ResponseListener<DATA> mResponseListener = new ResponseListener<DATA>() {
        @Override
        public void onResponse(Request<DATA> request, DATA response, boolean fromCache) {
            RefreshType refreshType = (RefreshType) ((EzyerParseJsonRequest) request).getCustomParams()[0];
            requestFinish(request, refreshType, response);
        }

        @Override
        public void onError(VolleyError error) {
            super.onError(error);
            requestError(error);
        }
    };

    private EzyerDataViewAutoBinder<HOLDER, DATA> mAdapter = new EzyerDataViewAutoBinder<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.setHolder(getViewHolder());
    }

    public void requestFinish(Request<DATA> request, RefreshType refreshType, DATA data) {
        bindData(request, refreshType, data);
    }

    public void bindData(Request<DATA> request, RefreshType refreshType, DATA data) {
        mAdapter.bindData(refreshType, data);
    }

    public void bindView(Request<DATA> request, RefreshType refreshType, View view, Object data) {
        mAdapter.bindView(refreshType, view, data);
    }

    public void requestError(VolleyError error) {

    }

    public void requestData(RefreshType refreshType, EzyerRequest request) {
        request.setResponseListener(mResponseListener);
        request.setCustomParams(refreshType);
        request.execute();
    }

}
