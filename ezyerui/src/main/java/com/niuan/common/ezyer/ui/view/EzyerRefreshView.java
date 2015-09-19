//package com.niuan.common.ezyer.ui.view;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.text.TextPaint;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.yalantis.phoenix.PullToRefreshView;
//import com.yalantis.phoenix.refresh_view.BaseRefreshView;
//
///**
// * Created by Carlos Liu on 2015/9/19.
// */
//public class EzyerRefreshView extends BaseRefreshView {
//    private ViewGroup mView;
//
//    public EzyerRefreshView(Context context, PullToRefreshView layout) {
//        super(context, layout);
//    }
//
//    public void setView(ViewGroup view) {
//        mView = view;
//    }
//
//    @Override
//    public void offsetTopAndBottom(int i) {
//
//    }
//
//
//    @Override
//    public void setPercent(float v, boolean b) {
//        mView.layout(0, 0, getRefreshLayout().getWidth(), (int) (getRefreshLayout().getTotalDragDistance() * v));
//
//        for(int i = 0; i < mView.getChildCount(); i++) {
//            View view = mView.getChildAt(i);
//            view.layout(0, 0, getRefreshLayout().getWidth(), (int) (getRefreshLayout().getTotalDragDistance() * v));
//        }
//    }
//
//    @Override
//    public void start() {
//
//    }
//
//    @Override
//    public void stop() {
//
//    }
//
//    @Override
//    public boolean isRunning() {
//        return false;
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//
//    }
//}
