package com.niuan.common.ezyer.base.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Carlos Liu on 2015/8/20.
 */
public class EzyerClassCache {

    private static final int MAX_CACHE_SIZE = 100;
    private static Map<Class<?>, EzyerClass> sClassCache = new HashMap<>();
    private static List<Class<?>> sClassList = new ArrayList<>();

    public static <E> EzyerClass<E> getEzyerClass(Class<E> cls) {
        if (cls == null) {
            return null;
        }
        EzyerClass ezyerClass = sClassCache.get(cls);
        if (ezyerClass == null) {
            ezyerClass = initEzyerClass(cls);

            if (sClassList.size() > MAX_CACHE_SIZE) {
                Class<?> removeCls = sClassList.get(0);
                sClassCache.remove(removeCls);
                sClassList.remove(0);
            }

            sClassList.add(cls);
            sClassCache.put(cls, ezyerClass);
        }
        return ezyerClass;
    }

    public static <E> EzyerClass<E> initEzyerClass(Class<E> cls) {
        EzyerClass ezyerClass = new EzyerClass();
        ezyerClass.setCls(cls);
        ezyerClass.setAnnotations(cls.getAnnotations());
        ezyerClass.setName(cls.getName());
        ezyerClass.setSimpleName(cls.getSimpleName());
        ezyerClass.setConstructors(cls.getConstructors());
        Field[] fields = cls.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            EzyerField[] ezyerFields = new EzyerField[fields.length];
            int index = 0;
            for (Field field : fields) {
                EzyerField ezyerField = new EzyerField();
                ezyerField.setField(field);
                ezyerField.setName(field.getName());
                ezyerField.setAnnotations(field.getAnnotations());
                ezyerFields[index++] = ezyerField;
            }
            ezyerClass.setDeclaredFields(ezyerFields);
        }
        return ezyerClass;
    }
}
