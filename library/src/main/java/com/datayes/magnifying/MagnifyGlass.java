package com.datayes.magnifying;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by quandong.li on 2015/5/27.
 */
public class MagnifyGlass extends View{
    private static final String TAG = MagnifyGlass.class.getSimpleName();
    private Matrix matrix = new Matrix();
    private Bitmap bitmap;
    private float scaleFactor = 1.3f;
    private Paint mPaint = new Paint();
    private MagnifyingLayout mMagnifyingLayout;
    private float mCurrentX;
    private float mCurrentY;
    public MagnifyGlass(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MagnifyGlass(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MagnifyGlass(Context context) {
        super(context);
        init();
    }

    public void setMagnifyingLayout(MagnifyingLayout layout) {
        mMagnifyingLayout = layout;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(bitmap == null || bitmap.isRecycled()) {
            Log.e(TAG, "onDraw bitmap is null or recycled");
            if (mMagnifyingLayout != null) {
                mMagnifyingLayout.invalidate();
            }
            return;
        }
        canvas.drawBitmap(bitmap, matrix, mPaint);
    }

    public boolean move(float x, float y) {
        if (Math.abs(x-mCurrentX) < 1 && Math.abs(y-mCurrentY) < 1) {
            return false;
        }
        mCurrentX = x;
        mCurrentY = y;
        matrix.setTranslate(0 - x, 0 - y);
        matrix.postScale(scaleFactor, scaleFactor);
        matrix.postTranslate(mMagnifyingLayout.getGlassWidth() / 2, mMagnifyingLayout.getGlassHeight() / 2);
        return true;
    }

    public void drawMagnifyContent(Bitmap b) {
        Log.d(TAG,"drawMagnifyContent bitmap = " + b);
        bitmap = b;
        invalidate();
    }
}
