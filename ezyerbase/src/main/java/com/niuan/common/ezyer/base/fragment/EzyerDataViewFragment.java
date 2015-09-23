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

import java.util.HashMap;

/**
 * A child class for fragment that provides method to bind data to view
 * <p/>
 * Data
 * Created by Carlos on 2015/9/20.
 */
public abstract class EzyerDataViewFragment<HOLDER extends EzyerViewHolder, DATA> extends EzyerSimpleFragment<HOLDER> {

    private HashMap<String, DATA> mDataCache = new HashMap<>();
    private EzyerDataViewAutoBinder<HOLDER, DATA> mAdapter = new EzyerDataViewAutoBinder<>();
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

    protected static final String KEY_DATA = "data";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.setHolder(getViewHolder());

        if (savedInstanceState != null) {
            mDataCache = (HashMap) savedInstanceState.getSerializable(KEY_DATA);
        }

    }

    public void requestFinish(Request<DATA> request, RefreshType refreshType, DATA data) {
        bindData(request, refreshType, data);
    }

    public void bindData(Request<DATA> request, RefreshType refreshType, DATA data) {
        if (request != null) {
            String cacheKey = request.getCacheKey();
            DATA oldData = mDataCache.get(cacheKey);
            DATA updatedData = updateCache(refreshType, oldData, data);
            mDataCache.put(cacheKey, updatedData);
        }
        mAdapter.bindData("", refreshType, data);
    }

    protected DATA updateCache(RefreshType refreshType, DATA oldData, DATA newData) {
        return newData;
    }

    public void bindView(Request<DATA> request, RefreshType refreshType, View view, Object data) {
        mAdapter.bindView(refreshType, view, data);
    }

    public void requestError(VolleyError error) {

    }

    public void requestData(RefreshType refreshType, EzyerRequest<DATA> request) {
        request.setResponseListener(mResponseListener);
        request.setCustomParams(refreshType);
        request.execute();
    }

    public boolean hasData() {
        return mDataCache.isEmpty();
    }

    public boolean hasData(String key) {
        return mDataCache.get(key) == null;
    }

    public DATA getData(String cacheKey) {
        return mDataCache.get(cacheKey);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_DATA, mDataCache);
    }
}
