package com.niuan.common.ezyer.ui.view.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;

import com.niuan.common.ezyer.ui.R;


public class AutoAdjustHelper {

    private String mAutoAdjustTypeStr;
    private int mAutoAdjustType;
    private float mScale = 1.0f;
    public static final int AUTO_ADJUST_NONE = 0;
    public static final int AUTO_ADJUST_WIDTH = 1; // 按传入大小自适应宽度
    public static final int AUTO_ADJUST_HEIGHT = 2; // 按传入大小自适应高度
    public static final int AUTO_ADJUST_SCALE_WIDTH = 3; // 按配置文件中比例自适应宽度
    public static final int AUTO_ADJUST_SCALE_HEIGHT = 4; // 按配置文件中比例自适应高度

    // private static final String STR_AUTO_ADJUST_NONE = "none";
    private static final String STR_AUTO_ADJUST_WIDTH = "auto_adjust_width";
    private static final String STR_AUTO_ADJUST_HEIGHT = "auto_adjust_height";
    private static final String STR_AUTO_ADJUST_SCALE_WIDTH = "auto_adjust_scale_width";
    private static final String STR_AUTO_ADJUST_SCALE_HEIGHT = "auto_adjust_scale_height";

    public void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray types = context.obtainStyledAttributes(attrs,
                    R.styleable.AutoAdjustView);
            mAutoAdjustTypeStr = types
                    .getString(R.styleable.AutoAdjustView_adjustType);
            if (STR_AUTO_ADJUST_WIDTH.equals(mAutoAdjustTypeStr)) {
                mAutoAdjustType = AUTO_ADJUST_WIDTH;
            } else if (STR_AUTO_ADJUST_HEIGHT.equals(mAutoAdjustTypeStr)) {
                mAutoAdjustType = AUTO_ADJUST_HEIGHT;
            } else if (STR_AUTO_ADJUST_SCALE_WIDTH.equals(mAutoAdjustTypeStr)) {
                mAutoAdjustType = AUTO_ADJUST_SCALE_WIDTH;
            } else if (STR_AUTO_ADJUST_SCALE_HEIGHT.equals(mAutoAdjustTypeStr)) {
                mAutoAdjustType = AUTO_ADJUST_SCALE_HEIGHT;
            } else {
                mAutoAdjustType = AUTO_ADJUST_NONE;
            }
            mScale = types.getFloat(R.styleable.AutoAdjustView_scaleRate, 1.0f);

            types.recycle();
        }
    }

    private int mRelativeWidth;
    private int mRelativeHeight;

    public void setAdjustType(int type) {
        mAutoAdjustType = type;
    }

    public void setRelativeWidth(int relativeWidth) {
        mRelativeWidth = relativeWidth;
    }

    public void setRelativeHeight(int relativeHeight) {
        mRelativeHeight = relativeHeight;
    }

    private int mViewWidth;
    private int mViewHeight;
    private int mViewWidthSpec;
    private int mViewHeightSpec;

    public int getWidth() {
        return mViewWidth;
    }

    public int getHeight() {
        return mViewHeight;
    }

    public int getWidthSpec() {
        return mViewWidthSpec;
    }

    public int getHeightSpec() {
        return mViewHeightSpec;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        float customizeScale = 0;
        switch (mAutoAdjustType) {
            case AUTO_ADJUST_NONE: {
                // 不用做处理

                break;
            }
            case AUTO_ADJUST_WIDTH: {
                if (mRelativeWidth != 0 && mRelativeHeight != 0) {
                    customizeScale = (float) mRelativeWidth
                            / (float) mRelativeHeight;
                    viewWidth = (int) (viewHeight * customizeScale);
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(viewWidth,
                            android.view.View.MeasureSpec.EXACTLY);
                }
                break;
            }
            case AUTO_ADJUST_HEIGHT: {
                if (mRelativeWidth != 0 && mRelativeHeight != 0) {
                    customizeScale = (float) mRelativeWidth
                            / (float) mRelativeHeight;
                    viewHeight = (int) (viewWidth / customizeScale);
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight,
                            android.view.View.MeasureSpec.EXACTLY);
                }
                break;
            }
            case AUTO_ADJUST_SCALE_WIDTH: {
                viewWidth = (int) (viewHeight * mScale);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(viewWidth,
                        android.view.View.MeasureSpec.EXACTLY);
                break;
            }
            case AUTO_ADJUST_SCALE_HEIGHT: {
                viewHeight = (int) (viewWidth / mScale);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight,
                        android.view.View.MeasureSpec.EXACTLY);
                break;
            }
        }

        mViewWidth = viewWidth;
        mViewHeight = viewHeight;
        mViewWidthSpec = widthMeasureSpec;
        mViewHeightSpec = heightMeasureSpec;
    }

    public void setScale(float mScale) {
        this.mScale = mScale;
    }
}
