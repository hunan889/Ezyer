package com.niuan.common.ezyer.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niuan.common.ezyer.ui.view.adapter.EzyerDataViewIdPair;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;

/**
 * Created by Carlos on 2015/9/14.
 */
public abstract class EzyerViewHolderFragment<T extends EzyerViewHolder> extends EzyerBaseFragment {
    protected T mViewHolder;
    protected EzyerDataViewIdPair mDataViewIdPair;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewHolder = initRootViewHolder();
        mDataViewIdPair = initDataViewIdPair();
    }

    protected abstract T initRootViewHolder();

    protected T getViewHolder() {
        return mViewHolder;
    }

    protected EzyerDataViewIdPair initDataViewIdPair() {
        return null;
    }

    protected EzyerDataViewIdPair getDataViewIdPair() {
        return mDataViewIdPair;
    }


    public <V extends View> V findViewById(int id) {
        if (mViewHolder == null) {
            mViewHolder = initRootViewHolder();
        }
        if (mViewHolder == null) {
            return null;
        }
        return getViewHolder().findViewById(id);
    }

}
