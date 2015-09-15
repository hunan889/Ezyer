package com.niuan.common.ezyer.base.view.binder;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.niuan.common.ezyer.base.view.adapter.EzyerBaseListAdapter;

import java.util.List;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class EzyerAbsListViewBinder extends EzyerViewBinder<AbsListView> {

    @Override
    public boolean bindView(Object obj, View view) {
        if (view == null) {
            return false;
        }
        if (!(view instanceof AbsListView)) {
            return false;
        }
        if (!(obj instanceof List)) {
            return false;
        }

        AbsListView listView = (AbsListView) view;
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null || !(adapter instanceof EzyerBaseListAdapter)) {
            return false;
        }
        EzyerBaseListAdapter ezyerListAdapter = (EzyerBaseListAdapter) adapter;
        ezyerListAdapter.setDataSource((List) obj);
        return true;
    }

    @Override
    public Class<AbsListView> getSupportViewClass() {
        return AbsListView.class;
    }
}
