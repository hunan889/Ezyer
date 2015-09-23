package com.niuan.common.ezyer.ui.view.holder;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niuan.common.ezyer.ui.annotation.EzyerView;
import com.niuan.common.ezyer.ui.reflection.EzyerClass;
import com.niuan.common.ezyer.ui.reflection.EzyerClassCache;
import com.niuan.common.ezyer.ui.reflection.EzyerField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Carlos on 2015/8/14.
 */
public class EzyerViewHolder {
    private View mRootView;
    private SparseArray<List<View>> mViewDataIdMap = new SparseArray<>();
    private SparseArray<View> mViewResIdMap = new SparseArray<>();
    private SparseArray<EzyerViewHolder> mHolderDataIdMap = new SparseArray<>();

    public EzyerViewHolder(@NonNull LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        int resourceId = getEzyerViewIdForClass(getClass());

        if (resourceId == 0) {
            throw new RuntimeException("No resource id found for class " + getClass()
                    + ", did you forget to add EzyerView annotation in you class definition?");
        }
        View view = inflater.inflate(resourceId, parent, attachToParent);

        init(view);
    }

    public EzyerViewHolder(View view) {
        if (view == null) {
            throw new RuntimeException("view cannot be null when initializing " + getClass());
        }

        init(view);
    }

    private void init(View view) {
        mRootView = view;
        fillEzyerViewField(view);
    }

    public final void pair(int dataId, int resId) {
        List<View> cacheViewList = mViewDataIdMap.get(dataId);
        if (cacheViewList == null) {
            cacheViewList = new ArrayList<>();
            mViewDataIdMap.put(dataId, cacheViewList);
        }

        View view = findViewById(resId);
        if (view != null && !cacheViewList.contains(view)) {
            cacheViewList.add(view);
        }
    }

    public final void pair(int dataId, EzyerViewHolder holder) {
        mHolderDataIdMap.put(dataId, holder);
    }

    private int getEzyerViewIdForClass(Class<?> cls) {
        if (cls == null) {
            return 0;
        }

        EzyerClass<?> ezyerClass = EzyerClassCache.getEzyerClass(cls);
        EzyerView layout = ezyerClass.getAnnotation(EzyerView.class);
        if (layout == null) {
            return 0;
        }
        return layout.resourceId();
    }

    private void fillEzyerViewField(@NonNull View view) {
        if (view == null) {
            return;
        }
        EzyerClass ezyerClass = EzyerClassCache.getEzyerClass(getClass());

        EzyerField[] viewFields = ezyerClass.getDeclaredFields();
        if (viewFields == null) {
            return;
        }
        for (EzyerField ezyerField : viewFields) {
            EzyerView ezyerView = ezyerField.getAnnotation(EzyerView.class);
            if (ezyerView == null) {
                continue;
            }

            Field field = ezyerField.getField();

            Class<?> fieldType = field.getType();
            if (EzyerViewHolder.class.isAssignableFrom(fieldType)) {
                Class<? extends EzyerViewHolder> childFieldType = (Class<? extends EzyerViewHolder>) fieldType;
                int dataId = ezyerView.dataId();

                EzyerViewHolder childHolder = initial(true, childFieldType, getView());

                if (childHolder == null) {
                    continue;
                }

                pair(dataId, childHolder);
            } else {
                field.setAccessible(true);
                int resourceId = ezyerView.resourceId();
                if (resourceId == 0) {
                    continue;
                }

                View fieldView = view.findViewById(resourceId);
                try {
                    field.set(this, fieldView);
                    int dataId = ezyerView.dataId();
                    if (dataId != -1) {
                        pair(dataId, resourceId);
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public final View getView() {
        return mRootView;
    }

    public final EzyerViewHolder findHolderByDataId(int dataId) {
        return mHolderDataIdMap.get(dataId);
    }

    public final <T extends View> List<T> findViewsByDataId(int dataId) {
        return (List<T>) mViewDataIdMap.get(dataId);
    }

    public final <T extends View> T findViewById(int resId) {
        T view = (T) mViewResIdMap.get(resId);
        if (view == null) {
            view = (T) mRootView.findViewById(resId);
            if (view != null) {
                mViewResIdMap.put(resId, view);
            }
        }
        return view;
    }

    public static <T extends EzyerViewHolder> T initial(@NonNull Class<T> cls, Object... params) {
        return initial(false, cls, params);
    }

    public static <T extends EzyerViewHolder> T initial(boolean allowEmpty, @NonNull Class<T> cls, Object... params) {

        T value = EzyerReflectionUtil.initialObject(cls, params);
        if (value == null && !allowEmpty) {
            Class<?>[] paramTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = params[i].getClass();
            }
            throw new RuntimeException("No corresponding constructor found for class " + cls
                    + " with parameter class list:" + Arrays.toString(paramTypes));
        }

        return value;
    }
}
