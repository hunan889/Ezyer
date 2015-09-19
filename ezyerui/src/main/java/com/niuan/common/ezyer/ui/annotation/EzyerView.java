package com.niuan.common.ezyer.ui.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EzyerView {
    int resourceId() default -1;

    int dataId() default -1;

}
