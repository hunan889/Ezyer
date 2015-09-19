//package com.niuan.common.ezyer.ui.view;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//
//import com.yalantis.phoenix.PullToRefreshView;
//import com.yalantis.phoenix.refresh_view.BaseRefreshView;
//
//import java.lang.reflect.Field;
//
///**
// * Created by Carlos Liu on 2015/9/19.
// */
//public class EzyerPullToRefreshView extends PullToRefreshView {
//    private ViewGroup mHeaderLayout;
//    private EzyerRefreshView mBaseRefreshView;
//
//    public EzyerPullToRefreshView(Context context) {
//        super(context);
//        init(context);
//    }
//
//    public EzyerPullToRefreshView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        init(context);
//    }
//
//    private void init(Context context) {
//
//        mHeaderLayout = new FrameLayout(context);
//        mHeaderLayout.setBackgroundColor(Color.CYAN);
//        addView(mHeaderLayout);
//
//        mBaseRefreshView = new EzyerRefreshView(getContext(), this);
//        mBaseRefreshView.setView(mHeaderLayout);
//        try {
//            Field field = PullToRefreshView.class.getDeclaredField("mBaseRefreshView");
//            field.setAccessible(true);
//            field.set(this, mBaseRefreshView);
//
//            Field refreshView = PullToRefreshView.class.getDeclaredField("mRefreshView");
//            refreshView.setAccessible(true);
//            ImageView imageView = (ImageView) refreshView.get(this);
//            imageView.setImageDrawable(mBaseRefreshView);
//
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void setRefreshStyle(int type) {
//    }
//
//    public void setHeaderView(View view) {
//        mHeaderLayout.removeAllViews();
//        mHeaderLayout.addView(view);
//    }
//}
