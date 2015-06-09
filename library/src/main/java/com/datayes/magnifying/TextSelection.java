package com.datayes.magnifying;

import android.view.MotionEvent;

/**
 * Created by quandong.li on 2015/6/9.
 */
public abstract class TextSelection {


    protected TextSelectionView mView;

    public TextSelection(TextSelectionView view) {
        mView = view;
    }

    protected abstract void select(MotionEvent event);

    protected abstract String getSelectedText();
}
