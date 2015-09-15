package com.niuan.common.ezyer.base.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.niuan.common.ezyer.base.view.util.AutoAdjustHelper;

public class AutoAdjustImageView extends ImageView {

	private AutoAdjustHelper mHelper = new AutoAdjustHelper();
	private int mCustWidth;
	private int mCustHeight;

	public AutoAdjustImageView(Context context) {
		super(context);
		init(context, null);
	}

	public AutoAdjustImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public AutoAdjustImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mHelper.init(context, attrs);
	}

	public void setCustWidth(int width) {
		mCustWidth = width;
	}

	public void setCustHeight(int height) {
		mCustHeight = height;
	}

	public void setAdjustType(int type) {
		mHelper.setAdjustType(type);
	}
	
	public void setScaleRate(float scale){
		mHelper.setScale(scale);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int custWidth = 0;
		int custHeight = 0;
		if (mCustHeight != 0 && mCustWidth != 0) {
			custWidth = mCustWidth;
			custHeight = mCustHeight;
		} else {
			Drawable drawable = getDrawable();

			if (drawable != null) {
				custWidth = drawable.getIntrinsicWidth();
				custHeight = drawable.getIntrinsicHeight();
			}
		}

		mHelper.setRelativeHeight(custHeight);
		mHelper.setRelativeWidth(custWidth);

		mHelper.onMeasure(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(mHelper.getWidthSpec(), mHelper.getHeightSpec());
	}
}
