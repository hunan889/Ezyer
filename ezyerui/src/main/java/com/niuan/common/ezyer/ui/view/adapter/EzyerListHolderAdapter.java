package com.niuan.common.ezyer.ui.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;

/**
 * Created by Carlos on 2015/8/13.
 */
public abstract class EzyerListHolderAdapter<HOLDER extends EzyerViewHolder, DATA> extends EzyerBaseListAdapter<DATA> {
    private Class<HOLDER>[] mHolderTypes;

    public EzyerListHolderAdapter(Context context) {
        super(context);
        mHolderTypes = initSupportHolderTypes();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        EzyerDataViewAdapter<HOLDER, DATA> adapter;
        if (convertView == null) {
            HOLDER holder = createHolder(parent, position);
            adapter = createDataViewAdapter(holder, position);
            if (holder != null) {
                convertView = holder.getView();
                convertView.setTag(adapter);
            }
        } else {
            adapter = (EzyerDataViewAdapter<HOLDER, DATA>) convertView.getTag();
        }
        adapter.setData(getItem(position));
        return convertView;
    }

    @Override
    public final int getItemViewType(int position) {
        for (int i = 0; i < mHolderTypes.length; i++) {
            Class<HOLDER> holder = mHolderTypes[i];
            if (holder == null) {
                continue;
            }
            if (holder.equals(getHolderType(position))) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public final int getViewTypeCount() {
        return mHolderTypes == null ? 0 : mHolderTypes.length;
    }

    protected HOLDER createHolder(ViewGroup parent, int position) {
        return EzyerViewHolder.initial(getHolderType(position), mInflater, parent, false);
    }

    protected EzyerDataViewAdapter<HOLDER, DATA> createDataViewAdapter(HOLDER holder, int position) {
        return new EzyerDataViewAdapter<>(holder);
    }

    public abstract Class<HOLDER>[] initSupportHolderTypes();

    public Class<HOLDER>[] getSupportHolderTypes() {
        return mHolderTypes;
    }

    public Class<? extends HOLDER> getHolderType(int position) {
        if (mHolderTypes == null || mHolderTypes.length == 0) {
            return null;
        }
        return mHolderTypes[0];
    }
}
