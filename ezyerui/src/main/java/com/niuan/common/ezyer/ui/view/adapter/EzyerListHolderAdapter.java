package com.niuan.common.ezyer.ui.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.niuan.common.ezyer.data.RefreshType;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;

/**
 * Created by Carlos on 2015/8/13.
 */
public abstract class EzyerListHolderAdapter<HOLDER extends EzyerViewHolder, DATA> extends EzyerBaseListAdapter<DATA> {


    private Class<HOLDER>[] mHolderTypes;
    private EzyerDataViewAutoBinder<HOLDER, DATA> mDataViewBinder;
    private EzyerDataViewIdPair mDataViewIdPair;

    public EzyerListHolderAdapter(Context context) {
        super(context);
        mHolderTypes = initSupportHolderTypes();
        mDataViewBinder = initDataViewAutoBinder();
        mDataViewIdPair = initDataViewIdPair();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HOLDER holder = null;
        if (convertView == null) {
            holder = createHolder(parent, position);
            if (holder != null) {
                convertView = holder.getView();
                convertView.setTag(holder);
            }
        } else {
            holder = (HOLDER) convertView.getTag();
        }

        bindView(getItem(position), holder, position);
        return convertView;
    }

    protected void bindView(DATA data, HOLDER holder, int position) {

        if (mDataViewBinder != null && mDataViewIdPair != null) {
            mDataViewBinder.bindView(RefreshType.Replace, holder, data, mDataViewIdPair);
        }
    }

    @Override
    public final int getItemViewType(int position) {
        for (int i = 0; i < mHolderTypes.length; i++) {
            Class<HOLDER> pair = mHolderTypes[i];
            if (pair == null) {
                continue;
            }
            if (pair.equals(getHolderType(position))) {
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
        Class<HOLDER> holderClass = getHolderType(position);
        return EzyerViewHolder.initial(holderClass, EzyerViewHolder.inflateView(holderClass, mInflater, parent, false));
    }

    protected EzyerDataViewAutoBinder<HOLDER, DATA> createDataViewAdapter(HOLDER holder, int position) {
        EzyerDataViewAutoBinder<HOLDER, DATA> adapter = new EzyerDataViewAutoBinder<>();
        adapter.setHolder(holder);
        return adapter;
    }

    public abstract Class<HOLDER>[] initSupportHolderTypes();

    public EzyerDataViewAutoBinder initDataViewAutoBinder() {
        return new EzyerDataViewAutoBinder<>();
    }

    public EzyerDataViewIdPair initDataViewIdPair() {
        return null;
    }

    public Class<HOLDER>[] getSupportHolderTypes() {
        return mHolderTypes;
    }


    public Class<HOLDER> getHolderType(int position) {
        if (mHolderTypes == null || mHolderTypes.length == 0) {
            return null;
        }
        return mHolderTypes[0];
    }
}
