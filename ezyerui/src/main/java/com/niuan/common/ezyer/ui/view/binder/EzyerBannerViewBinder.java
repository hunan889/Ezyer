package com.niuan.common.ezyer.ui.view.binder;

import android.widget.ListAdapter;

import com.niuan.common.ezyer.data.RefreshType;
import com.niuan.common.ezyer.ui.view.BaseBannerView;
import com.niuan.common.ezyer.ui.view.adapter.EzyerBaseListAdapter;

import java.util.List;

/**
 * Created by Carlos on 2015/9/14.
 */
public class EzyerBannerViewBinder extends EzyerViewBinder<BaseBannerView, List> {

    @Override
    public boolean bindView(RefreshType refreshType, List obj, BaseBannerView view) {
        if (view == null) {
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

}
