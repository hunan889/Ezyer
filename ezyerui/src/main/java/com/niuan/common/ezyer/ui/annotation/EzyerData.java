package com.niuan.common.ezyer.ui.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Carlos Liu on 2015/8/18.
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EzyerData {
    int id() default 0;

    int type() default EzyerDataType.TYPE_DIRECT_VALUE;
}
