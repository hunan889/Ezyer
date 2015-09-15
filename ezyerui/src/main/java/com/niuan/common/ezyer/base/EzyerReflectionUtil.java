package com.niuan.common.ezyer.base;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.niuan.common.ezyer.base.annotation.EzyerView;
import com.niuan.common.ezyer.base.reflection.EzyerClass;
import com.niuan.common.ezyer.base.reflection.EzyerClassCache;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Carlos on 2015/8/14.
 */
public class EzyerReflectionUtil {

    public static int getEzyerViewIdForClass(Class<?> cls) {
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

//    public static void fillEzyerViewField(@NonNull View view, @NonNull EzyerViewHolder holder) {
//
//        if (holder == null || view == null) {
//            return;
//        }
//        EzyerClass ezyerClass = EzyerClassCache.getEzyerClass(holder.getClass());
//
//        EzyerField[] viewFields = ezyerClass.getDeclaredFields();
//        if (viewFields == null) {
//            return;
//        }
//        for (EzyerField ezyerField : viewFields) {
//            EzyerView ezyerView = ezyerField.getAnnotation(EzyerView.class);
//            if (ezyerView == null) {
//                continue;
//            }
//
//            Field field = ezyerField.getField();
//            field.setAccessible(true);
//            int resourceId = ezyerView.resourceId();
//
//            if (resourceId == 0) {
//                resourceId = getResourceIdByName(view.getContext(), toIdName(ezyerField.getName()), "id");
//            }
//            if (resourceId == 0) {
//                continue;
//            }
//
//            View fieldView = view.findViewById(resourceId);
//            try {
//                field.set(holder, fieldView);
//                int dataId = ezyerView.dataId();
//                if (dataId != -1) {
//                    holder.mViewDataIdMap.put(dataId, fieldView);
//                }
//
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    private static final char ID_SEPARATOR = '_';

    private static String toIdName(String fieldName) {
        String idName = String.valueOf(Character.toLowerCase(fieldName
                .charAt(1)));

        for (int i = 2, length = fieldName.length(); i < length; ++i) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                idName = idName + ID_SEPARATOR + Character.toLowerCase(c);
            } else {
                idName += c;
            }
        }

        return idName;
    }

    public static int getResourceIdByName(Context context, String name, String type) {
        Resources resources = context.getResources();
        return resources.getIdentifier(name, type, context.getPackageName());
    }

    public static <T> T initialObject(@NonNull Class<T> cls, Object... params) {
        EzyerClass ezyerClass = EzyerClassCache.getEzyerClass(cls);
        Constructor[] constructors = ezyerClass.getConstructors();
        for (Constructor<T> c : constructors) {
            try {
                return c.newInstance(params);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
