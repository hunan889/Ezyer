package com.niuan.common.ezyer.base.fragment;

import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.niuan.common.ezyer.data.RefreshType;
import com.niuan.common.ezyer.net.EzyerParseJsonRequest;
import com.niuan.common.ezyer.net.EzyerRequest;
import com.niuan.common.ezyer.net.ResponseListener;
import com.niuan.common.ezyer.ui.view.adapter.EzyerDataViewAdapter;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;

import java.util.Objects;

/**
 * Created by Carlos on 2015/9/20.
 */
public abstract class EzyerDataViewFragment<HOLDER extends EzyerViewHolder> extends EzyerSimpleFragment<HOLDER> {

    private ResponseListener mResponseListener = new ResponseListener() {
        @Override
        public void onResponse(Request request, Object response, boolean fromCache) {
            RefreshType refreshType = (RefreshType) ((EzyerParseJsonRequest) request).getCustomParams()[0];
            requestFinish(refreshType, response);
        }

        @Override
        public void onError(VolleyError error) {
            super.onError(error);
            requestError(error);
        }
    };

    private EzyerDataViewAdapter<HOLDER, Object> mAdapter = new EzyerDataViewAdapter<HOLDER, Object>() {
        @Override
        public void bindView(RefreshType refreshType, HOLDER holder, Object o, Object... params) {
            EzyerDataViewFragment.this.bindView(refreshType, holder, o, params);
        }
    };

    protected void bindView(RefreshType refreshType, HOLDER holder, Object o, Object... params) {
        mAdapter.bindHolder(refreshType, holder, o);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter.setHolder(getViewHolder());
    }

    public void requestFinish(RefreshType requestType, Object data, Object... params) {
        mAdapter.bindData(requestType, data, params);
    }

    public void requestError(VolleyError error) {

    }

    public void sendRequest(RefreshType refreshType, EzyerRequest request) {
        request.setResponseListener(mResponseListener);
        request.setCustomParams(refreshType);
        request.execute();
    }

    public EzyerDataViewAdapter<HOLDER, Object> getDataViewAdapter() {
        return mAdapter;
    }

//    public void sendRequest(RefreshType refreshType, EzyerParseJsonRequest<T> request, )

//    public static <T> void sendRequest(RefreshType refreshType, int method, String url, String requestBody, ResponseListener<T> listener, Class<T> type) {
//
//        EzyerParseJsonRequest request = new EzyerParseJsonRequest(url, mResponseListener, getDataType());
//        request.setCustomParams(refreshType);
//        request.execute();
//    }

}
