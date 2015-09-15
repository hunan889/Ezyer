package com.niuan.common.ezyer.base.util;

/**
 * Created by Carlos on 2015/9/14.
 */
public class MathUtil {
    public static boolean isFloatEquals(float f1, float f2, float r) {
        return Math.abs(f1 - f2) < r;
    }

    public static boolean isFloatEquals(float f1, float f2) {
        return isFloatEquals(f1, f2, 0.0001f);
    }
}
