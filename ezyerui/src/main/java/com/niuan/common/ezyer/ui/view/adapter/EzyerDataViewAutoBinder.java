package com.niuan.common.ezyer.ui.view.adapter;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import com.niuan.common.ezyer.data.RefreshType;
import com.niuan.common.ezyer.ui.annotation.EzyerData;
import com.niuan.common.ezyer.ui.reflection.EzyerClass;
import com.niuan.common.ezyer.ui.reflection.EzyerClassCache;
import com.niuan.common.ezyer.ui.reflection.EzyerField;
import com.niuan.common.ezyer.ui.view.binder.EzyerViewBinder;
import com.niuan.common.ezyer.ui.view.binder.EzyerViewBinderManager;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;

import java.lang.reflect.Field;

/**
 * Created by Carlos on 2015/8/14.
 */
public class EzyerDataViewAutoBinder<HOLDER extends EzyerViewHolder, DATA> {

    private DATA mData;
    private HOLDER mHolder;
    private SparseArray mDataValueMap = new SparseArray();

    private SparseIntArray mDataViewIdMap = new SparseIntArray();

    public EzyerDataViewAutoBinder() {

    }

    public void setHolder(@NonNull HOLDER holder) {
        mHolder = holder;
    }

    public final HOLDER getHolder() {
        return mHolder;
    }

    public final void bindData(RefreshType type, DATA data, EzyerDataViewIdPair dataViewPair) {
        mData = data;

        bindView(type, mHolder, data, dataViewPair);
    }

    public void bindView(RefreshType refreshType, HOLDER holder, DATA data, EzyerDataViewIdPair dataViewPair) {
        bindHolder(refreshType, holder, data, dataViewPair);
    }

    public final void bindView(RefreshType refreshType, View view, Object object) {
        bindView(refreshType, view, object, null);
    }

    public final void bindView(RefreshType refreshType, int resId, Object object) {
        bindView(refreshType, mHolder.findViewById(resId), object);
    }

    public final <V extends View, D> void bindView(RefreshType refreshType, V view, D object, EzyerViewBinder<V, D> binder) {
        if (object != null) {
            Object currentValue = mDataValueMap.get(view.hashCode());
            if (!object.equals(currentValue)) {
                EzyerViewBinderManager.bindView(refreshType, view, object, binder);
            }
        }
    }

    public void bindHolder(RefreshType refreshType, EzyerViewHolder holder, Object data, EzyerDataViewIdPair idPair) {
        if (holder == null || data == null || idPair == null) {
            return;
        }

        EzyerClass ezyerClass = EzyerClassCache.getEzyerClass(data.getClass());
        EzyerField[] fields = ezyerClass.getDeclaredFields();
        for (EzyerField ezyerField : fields) {
            EzyerData ezyerData = ezyerField.getAnnotation(EzyerData.class);
            if (ezyerData == null) {
                continue;
            }

            Field field = ezyerField.getField();
            field.setAccessible(true);


            int dataId = ezyerData.id();

            int[] viewIds = idPair.findViewIdsByDataId(dataId);
            for (int viewId : viewIds) {
                Object fieldValue = null;
                try {
                    fieldValue = field.get(data);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                EzyerViewHolder childHolder = holder.findHolderById(viewId);
                if (childHolder != null) {
                    bindHolder(refreshType, childHolder, fieldValue, idPair);
                } else {

                    View view = holder.findViewById(viewId);
                    bindView(refreshType, view, fieldValue);
                    mDataValueMap.put(view.hashCode(), fieldValue);
                }
            }
        }
    }
}
