package com.niuan.common.ezyer.ui.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoqun_liu on 2015/6/30.
 */
public abstract class EzyerBaseListAdapter<E> extends BaseAdapter {
    protected ArrayList<E> mDataSource = new ArrayList<>();
    protected LayoutInflater mInflater;
    private Resources mResource;

    private Context mContext;

    public EzyerBaseListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = context.getResources();
    }

    public Resources getResources() {
        return mResource;
    }

    public Context getContext() {
        return mContext;
    }

    public void setDataSource(List<E> dataSource) {
        mDataSource.clear();
        if(dataSource != null) {
            mDataSource.addAll(dataSource);
        }

        if (dataSource == null) {
            notifyDataSetInvalidated();
        } else {
            notifyDataSetChanged();
        }
    }

    public List<E> getDataSource() {
        return mDataSource;
    }

    @Override
    public int getCount() {
        return mDataSource == null ? 0 : mDataSource.size();
    }

    @Override
    public E getItem(int position) {
        if (mDataSource == null || position < 0
                || position >= mDataSource.size()) {
            return null;
        }
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onItemClick(View view, int position) {

    }
}
