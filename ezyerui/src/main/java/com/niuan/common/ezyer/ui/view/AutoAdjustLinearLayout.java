package com.niuan.common.ezyer.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.niuan.common.ezyer.ui.view.util.AutoAdjustHelper;

public class AutoAdjustLinearLayout extends LinearLayout {

	private AutoAdjustHelper mHelper = new AutoAdjustHelper();

	public AutoAdjustLinearLayout(Context context) {
		super(context);
		init(context, null);
	}

	public AutoAdjustLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public AutoAdjustLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mHelper.init(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		mHelper.onMeasure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(mHelper.getWidthSpec(), mHelper.getHeightSpec());
	}
}
