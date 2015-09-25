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

    private HashMap<String, DATA> mCurrentPageDataCache = new HashMap<>();
    private EzyerDataViewAutoBinder<HOLDER, DATA> mAdapter = new EzyerDataViewAutoBinder<>();
    private HashMap<Class<?>, Request<DATA>> mRequestCache = new HashMap<>();
    private ResponseListener<DATA> mResponseListener = new ResponseListener<DATA>() {
        @Override
        public void onResponse(Request<DATA> request, DATA response, boolean fromCache) {
            RefreshType refreshType = (RefreshType) ((EzyerParseJsonRequest) request).getCustomParams()[0];
            onRequestSuccess(request, refreshType, response, fromCache);

            mRequestCache.remove(request.getClass());
        }

        @Override
        public void onError(Request<DATA> request, VolleyError error) {
            super.onError(request, error);
            onRequestError(error);
            mRequestCache.remove(request.getClass());
        }
    };

    protected static final String KEY_DATA = "data";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.setHolder(getViewHolder());

        if (savedInstanceState != null) {
            HashMap currentPageDataCache = (HashMap) savedInstanceState.getSerializable(KEY_DATA);
            if (currentPageDataCache != null) {
                mCurrentPageDataCache.clear();
                mCurrentPageDataCache.putAll(currentPageDataCache);
            }
        }

        if (mCurrentPageDataCache.isEmpty()) {
            mCurrentPageDataCache = loadDataFromCache();
        }

        for (String key : mCurrentPageDataCache.keySet()) {
            DATA value = mCurrentPageDataCache.get(key);
            bindData(null, RefreshType.Replace, value);
        }
    }

    /**
     * Loads cached data for this fragment, called from {@link #onViewCreated(View, Bundle)}
     *
     * @return cached data for this fragment
     */
    protected HashMap<String, DATA> loadDataFromCache() {
        return null;
    }

    /**
     * Callback method for data request, indicates this request is executed successfully
     *
     * @param request     original data request object
     * @param refreshType refresh type the data is used to
     * @param data        requested data
     * @param fromCache   if data returned from cache
     */
    public void onRequestSuccess(Request<DATA> request, RefreshType refreshType, DATA data, boolean fromCache) {
        if (request != null) {
            String cacheKey = request.getCacheKey();
            DATA oldData = mCurrentPageDataCache.get(cacheKey);
            DATA updatedData = mergeWithOldData(refreshType, oldData, data);
            mCurrentPageDataCache.put(cacheKey, updatedData);
        }
        bindData(request, refreshType, data);
    }

    /**
     * Callback method for data request, indicates this request is executed failed and error returns
     *
     * @param error
     */
    public void onRequestError(VolleyError error) {

    }

    public void bindData(Request<DATA> request, RefreshType refreshType, DATA data) {
        bindData(request, refreshType, getViewHolder(), data);
    }

    public void bindData(Request<DATA> request, RefreshType refreshType, EzyerViewHolder holder, DATA data) {
        mAdapter.bindHolder(refreshType, holder, data);
    }

    public void bindView(Request<DATA> request, RefreshType refreshType, View view, Object data) {
        mAdapter.bindView(refreshType, view, data);
    }

    /**
     * Merge new data return from network with current data, by default, only return new data
     *
     * @param refreshType refresh type the data is used to
     * @param oldData     data before request
     * @param newData     data returned from request
     * @return merged data using old data and new data, default returns new data only
     */
    protected DATA mergeWithOldData(RefreshType refreshType, DATA oldData, DATA newData) {
        return newData;
    }

    /**
     * Schedule a request to get data, the request execution will be either succeed and returned in
     * {@link #onRequestSuccess(Request, RefreshType, Object, boolean)}
     * or failed which returned in {@link #onRequestError(VolleyError)}
     *
     * @param refreshType refresh type the data is used to
     * @param request     original data request object
     * @see #onRequestSuccess(Request, RefreshType, Object, boolean)
     * @see #onRequestError(VolleyError)
     */
    public void requestData(RefreshType refreshType, EzyerRequest<DATA> request) {
        mRequestCache.put(request.getClass(), request);
        request.setResponseListener(mResponseListener);
        request.setCustomParams(refreshType);
        request.execute();
    }

    /**
     * @return
     */
    public boolean hasData() {
        return !mCurrentPageDataCache.isEmpty();
    }

    public boolean hasData(String key) {
        return mCurrentPageDataCache.get(key) == null;
    }

    public boolean hasRequests() {
        return mRequestCache.isEmpty();
    }

    public boolean hasRequest(Class<?> requestClass) {
        return mRequestCache.containsKey(requestClass);
    }

    public DATA getData(String cacheKey) {
        return mCurrentPageDataCache.get(cacheKey);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_DATA, mCurrentPageDataCache);
    }
}
