package com.datayes.magnifying;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by quandong.li on 2015/5/27.
 */
public class MagnifyingLayout extends RelativeLayout{
    private static final String TAG = MagnifyingLayout.class.getSimpleName();
    private int glassWidth;
    private int glassHeight;
    private int offsetX;
    private int offsetY;

    private boolean mShowMagnifyGlass;
    private MagnifyGlass mMagnifyGlass;
    private float mTouchDownX;
    private float mTouchDownY;

    private Bitmap mRenderBitmap;
    private MagnifyingLayoutConfiguration mConfig;

    public MagnifyingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MagnifyingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MagnifyingLayout(Context context) {
        super(context);
        init();
    }

    public int getGlassWidth() {
        return glassWidth;
    }

    public int getGlassHeight() {
        return glassHeight;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//		Log.i(TAG, "onInterceptTouchEvent : " + ev.getAction());
        if(mShowMagnifyGlass && ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//		Log.w(TAG, "dispatchTouchEvent : " + ev.getAction());
        if(mShowMagnifyGlass && (ev.getAction() == MotionEvent.ACTION_MOVE
                || ev.getAction() == MotionEvent.ACTION_UP)) {
            dispatchTouchEventToTextView(this, ev);
        }
        handleTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void dispatchTouchEventToTextView(ViewGroup group, MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        int childrenCount = group.getChildCount();
        for (int i = childrenCount - 1; i >= 0; i--) {
            View child = group.getChildAt(i);
            if (!canViewReceiveTouchEvents(child)
                    || !isTouchInView(x, y, child)) {
                continue;
            }
            final MotionEvent transformedEvent = MotionEvent.obtain(ev);
            final float offsetX = group.getScrollX() - child.getLeft();
            final float offsetY = group.getScrollY() - child.getTop();
            transformedEvent.offsetLocation(offsetX, offsetY);
//            Log.d(TAG,"dispatchTouchEventToTextView group = " + group +",transformedEvent Y = " + transformedEvent.getY());
            if(child instanceof ViewGroup) {
                dispatchTouchEventToTextView((ViewGroup) child, transformedEvent);
            } else if(child instanceof TextView){
                dispatchTransformedTouchEvent(transformedEvent, child);
            }
            transformedEvent.recycle();
        }
    }

    private boolean dispatchTransformedTouchEvent(MotionEvent event, View child) {
        final boolean handled;

        final int oldAction = event.getAction();
        if (oldAction == MotionEvent.ACTION_CANCEL) {
            event.setAction(MotionEvent.ACTION_CANCEL);
            if (child == null) {
                handled = super.dispatchTouchEvent(event);
            } else {
                handled = child.dispatchTouchEvent(event);
            }
            event.setAction(oldAction);
            return handled;
        }

        if (child == null) {
            handled = super.dispatchTouchEvent(event);
        } else {
            handled = child.dispatchTouchEvent(event);
        }
        return handled;
    }

    private void handleTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            touchCancel();
        } else if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            Log.v(TAG, "dispatchTouchEvent ACTION_DOWN : " + event + ",getY = " + event.getY());
            mTouchDownX = event.getX();
            mTouchDownY = event.getY();
        } else if(event.getAction() == MotionEvent.ACTION_MOVE && mMagnifyGlass != null) {
            moveMagnifyGlass(event.getX(), event.getY());
        }
        super.onTouchEvent(event);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if(mShowMagnifyGlass) {
            //ignore request if showing magnify glass;
        } else {
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }

    }

    private void init() {
        setClickable(true);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMagnifyGlass();
                return false;
            }
        });
    }

    public void initConfiguration(MagnifyingLayoutConfiguration config) {
        mConfig = config;
        glassWidth = config.glassWidth;
        glassHeight = config.glassHeight;
        offsetX = glassWidth/2;
        offsetY = glassHeight + config.padding;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//		Log.e("test", "onTouchEvent : " + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(!mShowMagnifyGlass || getWidth() == 0 || getHeight() == 0) {
            super.dispatchDraw(canvas);
        } else {
            dispatchDrawUsingBitmap(canvas);
        }

    }

    private void dispatchDrawUsingBitmap(Canvas canvas) {
        Bitmap renderBitmap = tryObtainRenderBitmap();
        if(renderBitmap == null) {
            return;
        }
        Canvas renderCanvas = new Canvas(renderBitmap);
        renderCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        super.dispatchDraw(renderCanvas);
        canvas.drawBitmap(renderBitmap, 0, 0, null);
        if(mMagnifyGlass != null) {
            mMagnifyGlass.drawMagnifyContent(renderBitmap);
        }
    }

    private Bitmap tryObtainRenderBitmap() {
        if (mRenderBitmap == null) {
            mRenderBitmap = tryCreateRenderBitmap();
        }
        return mRenderBitmap;
    }

    private Bitmap tryCreateRenderBitmap() {
        int width = getWidth();
        int height = getHeight();
        try {
            return createBitmapAndGcIfNecessary(width, height);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap createBitmapAndGcIfNecessary(int width, int height) {
        try {
            return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            System.gc();
            return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
    }

    private void touchCancel() {
        dismissMagnifyGlass();
    }

    private void showMagnifyGlass() {
        if(mShowMagnifyGlass) {
            return;
        }
        mShowMagnifyGlass = true;

        Vibrator vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {0,100,100};
        vibrator.vibrate(pattern,-1);

        mMagnifyGlass = new MagnifyGlass(getContext());
        mMagnifyGlass.setMagnifyingLayout(this);
        mMagnifyGlass.initConfiguration(mConfig);
        LayoutParams params = new LayoutParams(glassWidth, glassHeight);
        mMagnifyGlass.setTranslationX(mTouchDownX - offsetX);
        mMagnifyGlass.setTranslationY(mTouchDownY - offsetY);
        addView(mMagnifyGlass, params);

        setTextViewSelectable(this, true);
        moveMagnifyGlass(mTouchDownX, mTouchDownY);
    }

    private void dismissMagnifyGlass() {
        if(!mShowMagnifyGlass) {
            return;
        }
        setTextViewSelectable(this, false);
        mShowMagnifyGlass = false;
        removeView(mMagnifyGlass);
        mMagnifyGlass = null;
    }

    private void setTextViewSelectable(ViewGroup group, boolean selectable) {
        int childrenCount = group.getChildCount();
        for (int i = 0; i < childrenCount; i++) {
            View view = group.getChildAt(i);
            if (view instanceof ViewGroup) {
                setTextViewSelectable((ViewGroup)view, selectable);
            } else if (view instanceof TextSelectionView) {
                ((TextSelectionView)view).setSelectable(selectable);
            }
        }
    }

    private void moveMagnifyGlass(float x, float y) {
        mMagnifyGlass.setTranslationX(x - offsetX);
        mMagnifyGlass.setTranslationY(y - offsetY);
        boolean needInvalidate = mMagnifyGlass.move(x, y);
        if (needInvalidate) {
            invalidate();
        }
    }

    private static boolean canViewReceiveTouchEvents(View child) {
        return child.getVisibility() == View.VISIBLE
                || child.getAnimation() != null;
    }

    private boolean isTouchInView(float x, float y, View child) {
        float localX = x + getScrollX() - child.getLeft();
        float localY = y + getScrollY() - child.getTop();
        final boolean isInView = pointInChildView(child,localX, localY);
        return isInView;
    }

    private boolean pointInChildView(View child, float localX, float localY) {
        return localX >= 0 && localX < (child.getRight() - child.getLeft())
                && localY >= 0 && localY < (child.getBottom() - child.getTop());
    }

}
