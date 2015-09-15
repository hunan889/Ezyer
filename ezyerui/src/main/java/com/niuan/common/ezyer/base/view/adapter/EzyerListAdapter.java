package com.niuan.common.ezyer.base.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Carlos on 2015/8/13.
 */
public abstract class EzyerListAdapter<DATA> extends EzyerBaseListAdapter<DATA> {
    public EzyerListAdapter(Context context) {
        super(context);
    }

    public abstract int getResourcesId(int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(getResourcesId(position), null, false);
        }
        bindView(convertView, getItem(position), position);
        return convertView;
    }

    public void bindView(View view, DATA data, int position) {

    }
}
