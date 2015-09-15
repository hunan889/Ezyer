package com.niuan.common.ezyer.base.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.niuan.common.ezyer.base.util.MathUtil;
import com.niuan.common.ezyer.base.view.util.ChildTouchEventInterceptor;
import com.niuan.common.ezyer.base.util.Counter;
import com.niuan.common.ezyer.base.util.CounterItem;

import java.util.ArrayList;
import java.util.List;


/**
 * 使用矩阵对图片进行缩放和位移，支持双击以及手势缩放。 默认设置scaleType为matrix, 图片为居中显示(类似于centerInside)
 *
 * @author liuchaoqun
 */
public class ScalableImageView extends ImageView implements
        ChildTouchEventInterceptor {

    private static final float ACCURACY = 0.001f;
    private SimpleOnGestureListener mMouseGestureListener;
    private GestureDetector mGestureDetector;
    private Counter mRevertCounter = new Counter();
    private Counter mDecCounter = new Counter();

    private float mMinScale = 1.0f;
    private float mMaxScale = 2.0f;
    private float mMidScale = 4.0f;

    private static class TranslateItem {
        CounterItem transItemX;
        CounterItem transItemY;
    }

    private class OnImageAnimCallback implements Counter.OnCounterCallback {

        private float mProgress;
        private float mTransX;
        private float mTransY;
        private boolean mIsAnimating;

        public boolean isAnimating() {
            return mIsAnimating;
        }

        @Override
        public void onUpdate(List<CounterItem> list) {
            CounterItem scaleItem = getScaleItem(list);
            TranslateItem transItem = getTranslateItem(list);

            // 第一次会传start的值，用于初始化
            if (!mIsAnimating) {
                if (scaleItem != null) {
                    mProgress = scaleItem.getValue();
                }
                if (transItem.transItemX != null) {
                    mTransX = transItem.transItemX.getValue();
                }
                if (transItem.transItemY != null) {
                    mTransY = transItem.transItemY.getValue();
                }
                mIsAnimating = true;
                return;
            }

            Log.d(TAG, "[mRevertCallback]tX = " + mTransX + ", tY = " + mTransY);
            calculateTranslate(transItem);

            calculateScale(scaleItem);
        }

        private CounterItem getScaleItem(List<CounterItem> list) {
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
            return null;
        }

        private TranslateItem getTranslateItem(List<CounterItem> list) {
            TranslateItem item = new TranslateItem();
            if (list == null) {
                return null;
            }
            if (list.size() > 1) {
                item.transItemX = list.get(1);
            }
            if (list.size() > 2) {
                item.transItemY = list.get(2);
            }

            return item;
        }

        private void calculateTranslate(TranslateItem item) {
            if (item == null) {
                return;
            }
            // 计算位移
            float valueX = 0;
            if (item.transItemX != null) {

                valueX = item.transItemX.getValue() - mTransX; // valueX为本次位移需移动的距离，如从第1个像素移动到第10个像素，则移动距离为9
                mTransX = item.transItemX.getValue();
            }
            float valueY = 0;
            if (item.transItemY != null) {

                valueY = item.transItemY.getValue() - mTransY;
                mTransY = item.transItemY.getValue();
            }

            mCurrentMatrix.postTranslate(valueX, valueY);
        }

        private void calculateScale(CounterItem scaleItem) {
            // 计算scale
            if (scaleItem != null) {
                Log.d(TAG, "[mRevertCallback]progress = " + mProgress);

                float divide = scaleItem.getValue() / mProgress;
                mProgress = scaleItem.getValue();
                mCurrentMatrix.postScale(divide, divide, mid.x + mTransX, mid.y
                        + mTransY); // 通过位移计算相应的scale中点
            }

            setImageMatrix(mCurrentMatrix);
        }

        @Override
        public void onStop() {
            mIsAnimating = false;
            mProgress = 0;
            mTransX = 0;
            mTransY = 0;
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onPause() {
        }

    }

    private OnImageAnimCallback mRevertCallback = new OnImageAnimCallback();

    private OnImageAnimCallback mDecCallback = new OnImageAnimCallback() {

        public void onStop() {
            super.onStop();
            checkRevert();

            mode = NONE;
            Log.d(TAG, "mode=NONE");
        }

        ;

    };

    private static final String TAG = "ScreenView";

    private Context mContext;

    public void setGestureListener(SimpleOnGestureListener mouseGestureListener) {
        mMouseGestureListener = mouseGestureListener;
        mGestureDetector = new GestureDetector(mContext, mMouseGestureListener);
    }

    public ScalableImageView(Context context) {
        this(context, null);
    }

    public ScalableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ScalableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initOriginalBmpSize(false);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initOriginalBmpSize(true);
    }

    void init(Context context) {
        mRevertCounter.setOnTimerCallback(mRevertCallback);
        mDecCounter.setOnTimerCallback(mDecCallback);
        Matrix matrix = getImageMatrix();
        mCurrentMatrix.set(matrix);
        setGestureListener(new SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                doubleTapToScale(e);
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return super.onSingleTapConfirmed(e);
            }
        });
    }

    private float getScaleX(Matrix matrix) {
        float[] newValues = new float[9];
        matrix.getValues(newValues);
        return newValues[0];
    }

    private float getScaleY(Matrix matrix) {
        float[] newValues = new float[9];
        matrix.getValues(newValues);
        return newValues[4];
    }

    private float getImageLeft(Matrix matrix) {
        float[] newValues = new float[9];
        matrix.getValues(newValues);
        return newValues[2];
    }

    private float getImageTop(Matrix matrix) {
        float[] newValues = new float[9];
        matrix.getValues(newValues);
        return newValues[5];
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private boolean mCalculated;

    public void initOriginalBmpSize(boolean reInflate) {

        // ScaleType必须为matrix时才能进行缩放和位移操作
        setScaleType(ScaleType.MATRIX);
        if (mCalculated && !reInflate) {
            return;
        }

        Drawable drawable = getDrawable();
        if (drawable == null || mCurrentMatrix == null) {
            return;
        }
        reInflate = true;
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int width = getWidth();
        int height = getHeight();

        int viewWidth = width == 0 ? measuredWidth : width;
        int viewHeight = height == 0 ? measuredHeight : height;
        if (viewWidth == 0 || viewHeight == 0) {
            mCalculated = false;
            return;
        }
        mCalculated = true;

        float scaleX = (float) viewWidth / (float) drawableWidth;

        float scaleY = (float) viewHeight / (float) drawableHeight;
        mMinScale = scaleX < scaleY ? scaleX : scaleY;
        mMaxScale = mMinScale * 4f;
        mMidScale = mMinScale * 2f;

        Matrix matrix = getImageMatrix();
        matrix.setScale(mMinScale, mMinScale);
        mCurrentMatrix.set(matrix);
        doCheckTranslateBack(false);
        setImageMatrix(mCurrentMatrix);
    }

    private float getImageRight(Matrix matrix) {
        Drawable drawable = getDrawable();
        if (drawable != null) {

            float bmpWidth = drawable.getIntrinsicWidth() * getScaleX(matrix);

            return bmpWidth + getImageLeft(matrix);
        }

        return getWidth();
    }

    private float getImageBottom(Matrix matrix) {
        Drawable drawable = getDrawable();
        if (drawable != null) {

            float bmpHeight = drawable.getIntrinsicHeight() * getScaleY(matrix);

            return bmpHeight + getImageTop(matrix);
        }

        return getHeight();
    }

    Matrix mCurrentMatrix = new Matrix();
    Matrix mSavedMatrix = new Matrix();
    PointF start = new PointF();
    PointF mid = new PointF();
    PointF speed = new PointF();
    float oldDist;

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRevertCallback.isAnimating() || mDecCallback.isAnimating()) {
            return true;
        }
        // Handle touch events here...
        boolean isGestureDetected = false;
        if (mGestureDetector != null) {
            isGestureDetected = mGestureDetector.onTouchEvent(event);
        }
        if (isGestureDetected) {
            return true;
        }

        ImageView view = this;
        boolean isDown = false;
        // Log.d(TAG, "onTouchEvent, event = " + event.getAction());
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mSavedMatrix.set(mCurrentMatrix);
                start.set(event.getX(), event.getY());
                // Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                isDown = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (mode != NONE) {
                    checkRevert();

                    // CounterItem scale = null;
                    //
                    // CounterItem transX = new CounterItem(event.getX(),
                    // event.getX()
                    // + speed.x);
                    // CounterItem transY = new CounterItem(event.getY(),
                    // event.getY()
                    // + speed.y);
                    // mDecCounter.setValue(scale, transX, transY);
                    // mDecCounter.start();
                }

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                // Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    mSavedMatrix.set(mCurrentMatrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    // Log.d(TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    float fromX = start.x;
                    float toX = event.getX();

                    float fromY = start.y;
                    float toY = event.getY();

                    if (Math.abs(toX - fromX) < 10 && Math.abs(toY - fromY) < 10) {
                        break;
                    }

                    speed.x = toX - fromX;
                    speed.y = toY - fromY;

                    mCurrentMatrix.set(mSavedMatrix);
                    mCurrentMatrix.postTranslate(speed.x, speed.y);

                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    float scaleRate = newDist / oldDist;

                    float currentScaleRate = getScaleX(mSavedMatrix);
                    if (currentScaleRate * scaleRate > mMaxScale) {
                        scaleRate = mMaxScale / currentScaleRate;
                    }
                    if (newDist > 10f) {
                        mCurrentMatrix.set(mSavedMatrix);
                        mCurrentMatrix
                                .postScale(scaleRate, scaleRate, mid.x, mid.y);
                    }
                }
                break;
        }
        view.setImageMatrix(mCurrentMatrix);
        if (mode == ZOOM) {
            return true;
        }

        if (isDown) {
            return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * check the bounds/size of the image against the imageview, if exceed the
     * bounds/minimun size of the imageview, it will adjust the image view to
     * the bounds/size
     *
     * @return
     */
    private void checkRevert() {
        Log.d(TAG, "checkRevert");
        List<CounterItem> timeItems = new ArrayList<CounterItem>();
        CounterItem zoomItem = getZoomBackItem(mCurrentMatrix);
        timeItems.add(zoomItem);

        Matrix matrix = new Matrix();
        matrix.set(mCurrentMatrix);

        boolean needRevert = false;
        if (zoomItem != null) {
            float scaleRate = zoomItem.getEnd() / zoomItem.getStart();
            matrix.postScale(scaleRate, scaleRate, mid.x, mid.y);
        }

        needRevert |= zoomItem != null;

        List<CounterItem> transItems = getTranslateBackItems(matrix);
        if (mode == ZOOM || mode == DRAG) { // 只有当操作是zoom和drag时才需要做
            timeItems.addAll(transItems);
        }

        needRevert |= timeItems.size() > 0;

        if (needRevert) {
            CounterItem[] items = new CounterItem[timeItems.size()];
            timeItems.toArray(items);
            mRevertCounter.setValue(items);
            mRevertCounter.start();
        }
    }

    private CounterItem getZoomBackItem(Matrix matrix) {
        if (mode == ZOOM) {
            float currentScaleX = getScaleX(matrix);
            if (currentScaleX < mMinScale) {
                return new CounterItem(currentScaleX, mMinScale);
            }
        }
        return null;
    }

    /**
     * 获取位移动画x轴和y轴动画信息，当图像边界超过视图边界（即边框在视图显示范围内）时，自动将图像位移至视图边框位置
     * <p/>
     * <pre>
     *     -----------------
     * ....|....           |
     * .A  |   .           |
     * .   |   .   B       |
     * .   |   .           |
     * .   -----------------
     * .........
     * </pre>
     * <p/>
     * 如上图，A（图像）的右边及上边超过了B（视图）的右边及上边，则需要对A做向上及向右的位移
     *
     * @param matrix 当前图像矩阵
     * @return 一个包含{@link CounterItem}的列表，包含所需位移的x位移动画信息和y位移动画信息
     */
    private List<CounterItem> getTranslateBackItems(Matrix matrix) {
        List<CounterItem> itemList = new ArrayList<CounterItem>();

        PointF point = getTranslatePoint(matrix);

        Log.d(TAG, "point.x = " + point.x + ", point.y = " + point.y);
        if (Math.abs(point.x) < ACCURACY && Math.abs(point.y) < ACCURACY) {
            return itemList;
        }

        CounterItem xItem = new CounterItem(0, point.x);
        CounterItem yItem = new CounterItem(0, point.y);

        itemList.add(xItem);
        itemList.add(yItem);

        return itemList;
    }

    private PointF getTranslatePoint(Matrix matrix) {
        PointF point = new PointF();

        point.x = getDeltaX(matrix);
        point.y = getDeltaY(matrix);
        return point;
    }

    private float getDeltaX(Matrix matrix) {
        int width = getWidth();
        int measuredWidth = getMeasuredWidth();
        float imageLeft = getImageLeft(matrix);
        float imageRight = getImageRight(matrix);
        int viewWidth = width == 0 ? measuredWidth : width;
        return calculateEdgeDistance(imageLeft, imageRight, 0, viewWidth);
    }

    private float getDeltaY(Matrix matrix) {
        int height = getHeight();
        int measuredHeight = getMeasuredHeight();
        float imageTop = getImageTop(matrix);
        float imageDown = getImageBottom(matrix);
        int viewHeight = height == 0 ? measuredHeight : height;
        return calculateEdgeDistance(imageTop, imageDown, 0, viewHeight);
    }

    /**
     * 计算同一方向的两条边start和end与view该方向总宽度/长度之间的位置关系 在计算时，起点默认为0，宽/长为s
     * 如s为200，则表明该视图该方向的取值范围在0~200之间，在此条件下： 有两种情况
     * <p/>
     * case 1：需位移的图像边长小于视图边长
     * <p/>
     * <pre>
     *
     * 123456789012345678
     *    -------------     视图
     * .....                图像
     *
     *         以上情况： startImage = 1, endImage = 5 边长为4，中心点为3
     *                   startView = 4, endView = 16 边长为12，中心点为10
     *         此时需要把图像中心点移至视图中心点，需要做的位移是10-3=7，位移后的结果：
     *
     *
     * 123456789012345678
     *    -------------     视图
     *         .....         图像
     *
     *   case 2: 图像边长大于视图边长，此种情况只需保证超出的那一边位移至相应的边即可
     * </pre>
     *
     * @param startImage 图像起始边位置
     * @param endImage   图像结束边位置
     * @param startView  视图起始边位置
     * @param endView    视图结束边位置
     * @return
     */
    private float calculateEdgeDistance(float startImage, float endImage,
                                        float startView, float endView) {
        float viewCenter = (endView + startView) / 2;
        float imageCenter = (endImage + startImage) / 2;

        float delta = 0;

        if (endImage - startImage < endView) { // 如果图像小于视图
            if (startImage > 0 || endImage < endView) { // 位移至中心点
                delta = viewCenter - imageCenter;
            }
        } else { // 图像大于视图
            if (startImage >= startView) { // 起始边越界
                delta = startView - startImage;
            }
            if (endImage <= endView) { // 结束边越界
                delta = endView - endImage;
            }
        }
        return delta;
    }

    private void doCheckTranslateBack(boolean anim) {
        List<CounterItem> itemList = getTranslateBackItems(mCurrentMatrix);

        if (itemList.size() == 0) {
            return;
        }
        float deltaX = itemList.get(0).getEnd();
        float deltaY = itemList.get(1).getEnd();
        if (deltaX == 0 && deltaY == 0) {
            return;
        }

        mCurrentMatrix.postTranslate(deltaX, deltaY);
        setImageMatrix(mCurrentMatrix);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 当Scale时获取两个手指的中间位置
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {

        float x = 0;
        float y = 0;
        for (int i = 0; i < event.getPointerCount(); i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        point.set(x / 2, y / 2);
    }

    // 双击进行缩放
    private void doubleTapToScale(MotionEvent e) {
        float currentScale = getScaleX(mCurrentMatrix);
        float scale = getLatestScale();
        Log.d(TAG, "tapScale");

        mid.set(e.getX(), e.getY());

        List<CounterItem> timeItems = new ArrayList<CounterItem>();
        CounterItem zoomItem = new CounterItem(currentScale, scale);
        timeItems.add(zoomItem);

        Matrix matrix = new Matrix();
        matrix.set(mCurrentMatrix);

        if (zoomItem != null) {
            float scaleRate = zoomItem.getEnd() / zoomItem.getStart();
            matrix.postScale(scaleRate, scaleRate, mid.x, mid.y);
        }

        List<CounterItem> transItems = getTranslateBackItems(matrix);
        // if (!(mode == ZOOM || mode == DRAG)) { // 只有当操作是zoom和drag时才需要做
        timeItems.addAll(transItems);
        // }
        CounterItem[] items = new CounterItem[timeItems.size()];
        timeItems.toArray(items);

        mRevertCounter.setValue(items);
        mRevertCounter.start();
    }

    /**
     * 获取最靠近当前位置的缩放比例，该方法用于用户双击进行缩放时
     *
     * @return
     */
    private float getLatestScale() {
        float currentScale = getScaleX(mCurrentMatrix);
        float minScale = Math.abs(mMinScale - currentScale);
        float midScale = Math.abs(mMidScale - currentScale);
        float maxScale = Math.abs(mMaxScale - currentScale);

        if (MathUtil.isFloatEquals(currentScale, mMinScale)) {
            return mMidScale;
        }
        if (MathUtil.isFloatEquals(currentScale, mMidScale)) {
            return mMaxScale;
        }
        if (MathUtil.isFloatEquals(currentScale, mMaxScale)) {
            return mMinScale;
        }

        float scale = minScale < midScale ? minScale : midScale;
        scale = scale < maxScale ? scale : maxScale;

        float finalScaleValue;
        if (scale == minScale) {
            finalScaleValue = mMinScale;
        } else if (scale == midScale) {
            finalScaleValue = mMidScale;
        } else {
            finalScaleValue = mMaxScale;
        }

        return finalScaleValue;
    }

    private boolean isMinScale() {
        float currentScale = getScaleX(mCurrentMatrix);
        return !MathUtil.isFloatEquals(currentScale, mMinScale);
    }

    public boolean isIntercept(MotionEvent e) {
        if (getVisibility() != View.VISIBLE) {
            return false;
        }
        return isMinScale();
    }
}
