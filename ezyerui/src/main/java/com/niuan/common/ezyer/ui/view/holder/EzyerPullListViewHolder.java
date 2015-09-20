package com.niuan.common.ezyer.ui.view.holder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.niuan.common.ezyer.ui.view.adapter.EzyerBaseListAdapter;

/**
 * Created by Carlos on 2015/9/20.
 */
public abstract class EzyerPullListViewHolder extends EzyerPullViewHolder {
    private ListView mListView;

    public EzyerPullListViewHolder(@NonNull LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        super(inflater, parent, attachToParent);
    }

    public EzyerPullListViewHolder(View view) {
        super(view);
    }

    protected abstract int initListViewId();

    protected void initListView(ListView listView) {

    }

    protected abstract EzyerBaseListAdapter initAdapter();

    @Override
    protected void onInit() {
        super.onInit();
        mListView = findViewById(initListViewId());
        if (mListView == null) {
            return;
        }
        initListView(mListView);
        mListView.setAdapter(initAdapter());
    }

    public ListView getListView() {
        return mListView;
    }
}
