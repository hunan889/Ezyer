package com.niuan.common.ezyer.ui.view.adapter;

import android.util.SparseArray;

import com.niuan.common.ezyer.util.CheckUtil;

import java.util.Arrays;

/**
 * Created by Carlos on 2015/10/9.
 * <p/>
 * Utility class to pair data id with view id, all the view id for same data id will be stored into an int array,
 * for the purpose that a data id sometimes matches multiple view id
 */
public class EzyerDataViewIdPair {
    private SparseArray<int[]> mDataViewIdMap = new SparseArray<>();

    private static final int INVALID_MARK = 0;
    private static final int LENGTH_PER_INCREASE = 5;

    public void pair(int dataId, int resId) {

        int[] resArray = mDataViewIdMap.get(dataId);

        int insertPosition = 0;
        if (resArray == null) {
            resArray = new int[LENGTH_PER_INCREASE];
            insertPosition = 0;
        } else {
            if (resArray[resArray.length - 1] != INVALID_MARK) {
                int[] newArray = new int[resArray.length + LENGTH_PER_INCREASE];

                System.arraycopy(resArray, 0, newArray, 0, resArray.length);
                resArray = newArray;
                insertPosition = resArray.length - 1;
            } else {
                insertPosition = checkInsertPos(resArray);
            }
        }
        resArray[insertPosition] = resId;

        mDataViewIdMap.put(dataId, resArray);
    }

    public int[] findViewIdsByDataId(int dataId) {
        int[] viewIds = mDataViewIdMap.get(dataId);
        if (viewIds == null) {
            return new int[0];
        }

        // if viewIds didn't contains invalid items, return directly, otherwise remove invalid items.
        if (viewIds[viewIds.length - 1] != INVALID_MARK) {
            return viewIds;
        } else {
            int endPos = checkInsertPos(viewIds);

            if (endPos >= 0) {
                viewIds = Arrays.copyOf(viewIds, endPos);
                mDataViewIdMap.put(dataId, viewIds);
            } else {
                return new int[0];
            }
        }

        return viewIds;
    }

    private static int checkInsertPos(int[] array) {
        if (CheckUtil.isEmpty(array)) {
            return -1;
        }
        int end = array.length - 1;
        int start = 0;
        int mid = end / 2;
        int t1 = array[mid];

        while (true) {
            if (t1 == INVALID_MARK) {
                if (array[mid - 1] != INVALID_MARK) {
                    return mid;
                }
                end = mid;

            } else {
                start = mid;
            }

            mid = (start + end) / 2;
            t1 = array[mid];
        }
    }
}
