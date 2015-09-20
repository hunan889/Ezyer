package com.niuan.common.ezyer.ui.view.adapter;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

import com.niuan.common.ezyer.data.RefreshType;
import com.niuan.common.ezyer.ui.annotation.EzyerData;
import com.niuan.common.ezyer.ui.annotation.EzyerDataType;
import com.niuan.common.ezyer.ui.reflection.EzyerClass;
import com.niuan.common.ezyer.ui.reflection.EzyerClassCache;
import com.niuan.common.ezyer.ui.reflection.EzyerField;
import com.niuan.common.ezyer.ui.view.binder.EzyerViewBinder;
import com.niuan.common.ezyer.ui.view.binder.EzyerViewBinderManager;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Carlos on 2015/8/14.
 */
public class EzyerDataViewAdapter<HOLDER extends EzyerViewHolder, DATA> {

    private HOLDER mHolder;
    private SparseArray mDataValueMap = new SparseArray();
    private DATA mData;

    public EzyerDataViewAdapter() {

    }

    public void setHolder(@NonNull HOLDER holder) {
        mHolder = holder;
    }

    public final HOLDER getHolder() {
        return mHolder;
    }

    public final void bindData(RefreshType type, DATA data, Object... params) {
        mData = data;

        bindView(type, mHolder, data, params);
    }

    protected void bindView(RefreshType refreshType, HOLDER holder, DATA data, Object... params) {
        bindHolder(refreshType, holder, data);
    }

    protected final void bindView(RefreshType refreshType, View view, Object object) {
        bindView(refreshType, view, object, null);
    }

    protected final void bindView(RefreshType refreshType, int resId, Object object) {
        bindView(refreshType, mHolder.findViewById(resId), object);
    }

    protected final <V extends View, D> void bindView(RefreshType refreshType, V view, D object, EzyerViewBinder<V, D> binder) {
        if (object != null) {
            Object currentValue = mDataValueMap.get(view.hashCode());
            if (!object.equals(currentValue)) {
                EzyerViewBinderManager.bindView(refreshType, view, object, binder);
            }
        }
    }

    public final void bindHolder(RefreshType refreshType, EzyerViewHolder holder, Object data) {
        if (holder == null || data == null) {
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

            int type = ezyerData.type();
            switch (type) {
                case EzyerDataType.TYPE_DIRECT_VALUE: {
                    int id = ezyerData.id();
                    List<View> viewList = holder.findViewsByDataId(id);
                    if (viewList == null) {
                        continue;
                    }

                    for (View view : viewList) {
                        Object fieldValue = null;
                        try {
                            fieldValue = field.get(data);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        if (fieldValue != null) {
                            bindView(refreshType, view, fieldValue);
                            mDataValueMap.put(view.hashCode(), fieldValue);
                        }
                    }

                    break;
                }
                case EzyerDataType.TYPE_WRAPPER: {
                    Object fieldValue = null;
                    try {
                        fieldValue = field.get(data);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    if (fieldValue != null) {
                        int id = ezyerData.id();
                        EzyerViewHolder childHolder = holder.findHolderByDataId(id);
                        if (childHolder == null) {
                            continue;
                        }
                        bindHolder(refreshType, childHolder, fieldValue);
                    }
                    break;
                }
            }
        }
    }
}
