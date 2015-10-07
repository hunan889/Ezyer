package com.niuan.common.ezyer.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Carlos Liu on 2015/9/20.
 */
public class CollectionUtil {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }
}
