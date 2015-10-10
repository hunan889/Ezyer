package com.niuan.common.ezyer.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Carlos Liu on 2015/9/20.
 */
public class CheckUtil {
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Object... items) {
        if (items == null) {
            return true;
        }

        for (Object object : items) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }
}
