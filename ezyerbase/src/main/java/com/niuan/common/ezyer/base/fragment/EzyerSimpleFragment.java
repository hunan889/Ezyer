package com.niuan.common.ezyer.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Carlos on 2015/9/14.
 */
public abstract class EzyerSimpleFragment extends EzyerFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getResourceId(), container, false);
    }

    protected abstract int getResourceId();
}
