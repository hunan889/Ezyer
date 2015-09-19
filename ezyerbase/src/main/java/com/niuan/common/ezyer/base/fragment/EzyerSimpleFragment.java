package com.niuan.common.ezyer.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;

/**
 * Created by Carlos on 2015/9/14.
 */
public abstract class EzyerSimpleFragment<T extends EzyerViewHolder> extends EzyerViewHolderFragment<T> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getResourceId(), container, false);
    }

    protected abstract int getResourceId();
}
