package com.niuan.common.ezyer.base.view.adapter;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

import com.niuan.common.ezyer.base.EzyerViewHolder;
import com.niuan.common.ezyer.base.annotation.EzyerData;
import com.niuan.common.ezyer.base.annotation.EzyerDataType;
import com.niuan.common.ezyer.base.reflection.EzyerClass;
import com.niuan.common.ezyer.base.reflection.EzyerClassCache;
import com.niuan.common.ezyer.base.reflection.EzyerField;
import com.niuan.common.ezyer.base.view.binder.EzyerViewBinder;
import com.niuan.common.ezyer.base.view.binder.EzyerViewBinderManager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Carlos on 2015/8/14.
 */
public class EzyerDataViewAdapter<HOLDER extends EzyerViewHolder, DATA> {

    private final HOLDER mHolder;
    private SparseArray mDataValueMap = new SparseArray();
    private DATA mData;

    public EzyerDataViewAdapter(@NonNull HOLDER holder) {
        mHolder = holder;

        if (mHolder == null) {
            throw new RuntimeException("holder cannot be null");
        }
    }

    public final HOLDER getHolder() {
        return mHolder;
    }

    public final void setData(DATA data) {
        mData = data;

        bindView(mHolder, data);
    }

    protected void bindView(HOLDER holder, DATA data) {
        bindViewInner(holder, data);
    }

    protected final void bindView(View view, Object object) {
        bindView(view, object, null);
    }

    protected final void bindView(int resId, Object object) {
        bindView(mHolder.findViewById(resId), object);
    }

    protected final void bindView(View view, Object object, EzyerViewBinder binder) {
        if (object != null) {
            Object currentValue = mDataValueMap.get(view.hashCode());
            if (!object.equals(currentValue)) {
                EzyerViewBinderManager.bindView(view, object, binder);
            }
        }
    }

    private void bindViewInner(EzyerViewHolder holder, Object data) {
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
                            bindView(view, fieldValue);
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
                        bindViewInner(childHolder, fieldValue);
                    }
                    break;
                }
            }
        }
    }
}
