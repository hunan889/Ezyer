package com.niuan.common.ezyer.base.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niuan.common.ezyer.base.EzyerViewHolder;

/**
 * Created by Carlos on 2015/9/14.
 */
public class EzyerFragment extends Fragment {
    private EzyerViewHolder mViewCache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewCache = new EzyerViewHolder(view);
    }

    public <T extends View> T findViewById(int id) {
        if (mViewCache == null) {
            mViewCache = new EzyerViewHolder(getView());
        }
        return mViewCache.findViewById(id);
    }
}
