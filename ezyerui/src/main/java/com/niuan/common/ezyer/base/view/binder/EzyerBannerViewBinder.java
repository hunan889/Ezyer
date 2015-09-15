package com.niuan.common.ezyer.base.view.binder;

import android.view.View;
import android.widget.ListAdapter;

import com.niuan.common.ezyer.base.view.BaseBannerView;
import com.niuan.common.ezyer.base.view.InfiniteBannerView;
import com.niuan.common.ezyer.base.view.adapter.EzyerBaseListAdapter;

import java.util.List;

/**
 * Created by Carlos on 2015/9/14.
 */
public class EzyerBannerViewBinder extends EzyerViewBinder<BaseBannerView> {

    @Override
    public boolean bindView(Object obj, View view) {
        if (view == null) {
            return false;
        }
        if (!(view instanceof InfiniteBannerView)) {
            return false;
        }
        if (!(obj instanceof List)) {
            return false;
        }

        BaseBannerView listView = (BaseBannerView) view;
        ListAdapter adapter = listView.getListAdapter();
        if (adapter == null || !(adapter instanceof EzyerBaseListAdapter)) {
            return false;
        }
        EzyerBaseListAdapter ezyerListAdapter = (EzyerBaseListAdapter) adapter;
        ezyerListAdapter.setDataSource((List) obj);
        return true;
    }

    @Override
    public Class<BaseBannerView> getSupportViewClass() {
        return BaseBannerView.class;
    }
}
