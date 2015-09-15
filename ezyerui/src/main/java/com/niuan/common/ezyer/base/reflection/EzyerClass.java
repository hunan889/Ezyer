package com.niuan.common.ezyer.base.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * Created by Carlos Liu on 2015/8/20.
 */
public class EzyerClass<T> {

    private Class<T> mCls;
    private String mSimpleName;
    private String mName;
    private EzyerField[] mDeclaredFields;
    private Constructor<T>[] mConstructors;
    private Annotation[] mAnnotations;

    public Annotation[] getAnnotations() {
        return mAnnotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        mAnnotations = annotations;
    }

    public <E extends Annotation> E getAnnotation(Class<E> annotationCls) {
        if (mAnnotations == null || annotationCls == null) {
            return null;
        }

        for (Annotation annotation : mAnnotations) {
            if (annotation.annotationType().equals(annotationCls)) {
                return (E) annotation;
            }
        }
        return null;
    }

    public Class<T> getCls() {
        return mCls;
    }

    public void setCls(Class<T> cls) {
        mCls = cls;
    }


    public EzyerField[] getDeclaredFields() {
        return mDeclaredFields;
    }

    public void setDeclaredFields(EzyerField[] declaredFields) {
        mDeclaredFields = declaredFields;
    }

    public Constructor<T>[] getConstructors() {
        return mConstructors;
    }

    public void setConstructors(Constructor<T>[] constructors) {
        mConstructors = constructors;
    }

    public String getSimpleName() {
        return mSimpleName;
    }

    public void setSimpleName(String fullName) {
        mSimpleName = fullName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EzyerClass that = (EzyerClass) o;

        return !(mName != null ? !mName.equals(that.mName) : that.mName != null);

    }

    @Override
    public int hashCode() {
        return mName != null ? mName.hashCode() : 0;
    }
}
