package com.niuan.common.ezyer.base.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by Carlos Liu on 2015/8/20.
 */
public class EzyerField {
    private String mName;
    private Field mField;
    private Annotation[] mAnnotations;

    public <T extends Annotation> T getAnnotation(Class<T> annotationCls) {
        if (mAnnotations == null || annotationCls == null) {
            return null;
        }

        for (Annotation annotation : mAnnotations) {
            if (annotation.annotationType().equals(annotationCls)) {
                return (T) annotation;
            }
        }
        return null;
    }

    public Annotation[] getAnnotations() {
        return mAnnotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        mAnnotations = annotations;
    }

    public Field getField() {
        return mField;
    }

    public void setField(Field field) {
        mField = field;
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

        EzyerField that = (EzyerField) o;

        return !(mName != null ? !mName.equals(that.mName) : that.mName != null);

    }

    @Override
    public int hashCode() {
        return mName != null ? mName.hashCode() : 0;
    }
}
