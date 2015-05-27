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
    private static final float FACTOR = 1.3f;
    private Paint mPaint = new Paint();

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

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(bitmap == null || bitmap.isRecycled()) {
            Log.e(TAG, "onDraw bitmap is null or recycled");
            postInvalidateDelayed(100);
            return;
        }
        canvas.drawBitmap(bitmap, matrix, mPaint);
    }

    public void move(float x, float y) {
        matrix.setTranslate(0-x, 0-y);
        matrix.postScale(FACTOR, FACTOR);
    }

    public void drawMagnifyContent(Bitmap b) {
        Log.d(TAG,"drawMagnifyContent bitmap = " + b);
        bitmap = b;
        invalidate();
    }
}
