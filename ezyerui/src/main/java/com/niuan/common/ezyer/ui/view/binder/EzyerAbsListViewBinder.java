package com.niuan.common.ezyer.ui.view.binder;

import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.niuan.common.ezyer.data.RefreshType;
import com.niuan.common.ezyer.ui.view.adapter.EzyerBaseListAdapter;

import java.util.List;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class EzyerAbsListViewBinder extends EzyerViewBinder<AbsListView, List> {

    @Override
    public boolean bindView(RefreshType refreshType, List obj, AbsListView listView) {
        if (listView == null) {
            return false;
        }
        ListAdapter adapter = listView.getAdapter();

        if (adapter == null || !(adapter instanceof EzyerBaseListAdapter)) {
            return false;
        }

        EzyerBaseListAdapter baseListAdapter = (EzyerBaseListAdapter) adapter;
        switch (refreshType) {
            case Load: {
                baseListAdapter.addDataSource(obj);
                break;
            }
            case Update: {
                baseListAdapter.addDataSourceFront(obj);
                break;
            }
            case Replace:
            default: {
                baseListAdapter.setDataSource(obj);
            }
        }

        return true;
    }
}
