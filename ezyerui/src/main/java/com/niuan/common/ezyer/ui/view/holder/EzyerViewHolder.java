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

/**
 * Created by Carlos on 2015/8/14.
 */
public class EzyerViewHolder {
    private View mRootView;
    private SparseArray<View> mViewResIdMap = new SparseArray<>();
    private SparseArray<EzyerViewHolder> mHolderViewIdMap = new SparseArray<>();
//    private SparseArray<List<View>> mViewDataIdMap = new SparseArray<>();
//    private SparseArray<EzyerViewHolder> mHolderDataIdMap = new SparseArray<>();

//    public EzyerViewHolder(@NonNull LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
//        int resourceId = getEzyerViewIdForClass(getClass());
//
//        if (resourceId == 0) {
//            throw new RuntimeException("No resource id found for class " + getClass()
//                    + ", did you forget to add EzyerView annotation in you class definition?");
//        }
//        View view = inflater.inflate(resourceId, parent, attachToParent);
//
//        init(view);
//    }


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

//    public final void pair(int dataId, int resId) {
//        List<View> cacheViewList = mViewDataIdMap.get(dataId);
//        if (cacheViewList == null) {
//            cacheViewList = new ArrayList<>();
//            mViewDataIdMap.put(dataId, cacheViewList);
//        }
//
//        View view = findViewById(resId);
//        if (view != null && !cacheViewList.contains(view)) {
//            cacheViewList.add(view);
//        }
//    }
//
//    public final void pair(int dataId, EzyerViewHolder holder) {
//        mHolderDataIdMap.put(dataId, holder);
//    }

    public int getId() {
        return mRootView.getId();
    }

    private static int getEzyerViewIdForClass(Class<?> cls) {
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
//                int dataId = ezyerView.dataId();
                int resourceId = ezyerView.resourceId();
                if (resourceId == 0) {
                    continue;
                }

                EzyerViewHolder childHolder = initial(true, childFieldType, findViewById(resourceId));

                if (childHolder == null) {
                    continue;
                }

                mHolderViewIdMap.put(resourceId, childHolder);
//                pair(dataId, childHolder);
            } else {
                field.setAccessible(true);
                int resourceId = ezyerView.resourceId();
                if (resourceId == 0) {
                    continue;
                }

                View fieldView = view.findViewById(resourceId);
                try {
                    field.set(this, fieldView);

                    mViewResIdMap.put(resourceId, view);
//                    int dataId = ezyerView.dataId();
//                    if (dataId != -1) {
//                        pair(dataId, resourceId);
//                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public final View getView() {
        return mRootView;
    }

//    public final EzyerViewHolder findHolderByDataId(int dataId) {
//        return mHolderDataIdMap.get(dataId);
//    }
//
//    public final <T extends View> List<T> findViewsByDataId(int dataId) {
//        return (List<T>) mViewDataIdMap.get(dataId);
//    }

    public final <T extends EzyerViewHolder> T findHolderById(int resId) {
        return (T) mHolderViewIdMap.get(resId);
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

    public static View inflateView(Class<? extends EzyerViewHolder> holderClass, LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        int resourceId = getEzyerViewIdForClass(holderClass);

        if (resourceId == 0) {
            throw new RuntimeException("No resource id found for class " + holderClass
                    + ", did you forget to add EzyerView annotation in you class definition?");
        }
        View view = inflater.inflate(resourceId, parent, attachToParent);

        return view;
    }

    public static <T extends EzyerViewHolder> T initial(@NonNull Class<T> cls, LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return initial(false, cls, inflater, parent, attachToParent);
    }

    public static <T extends EzyerViewHolder> T initial(boolean allowEmpty, @NonNull Class<T> cls, LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {

        View view = inflateView(cls, inflater, parent, attachToParent);

        return initial(allowEmpty, cls, view);
    }

    public static <T extends EzyerViewHolder> T initial(@NonNull Class<T> cls, View view) {
        return initial(false, cls, view);
    }

    public static <T extends EzyerViewHolder> T initial(boolean allowEmpty, @NonNull Class<T> cls, View view) {

        T value = EzyerReflectionUtil.initialObject(cls, view);
        if (value == null && !allowEmpty) {
            throw new RuntimeException("No corresponding constructor found for class " + cls
                    + " with parameter class list:" + View.class);
        }

        return value;
    }

}
