package com.datayes.magnifying;

import android.text.Layout;
import android.text.Selection;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by quandong.li on 2015/6/9.
 */
public class WordSelection extends TextSelection {

    public WordSelection(TextSelectionView view) {
        super(view);
    }

    @Override
    protected void select(MotionEvent event) {
        Layout layout = mView.getLayout();
        int length = mView.getText().length();
//        Log.d("test","selectWord getScrollY = " + getScrollY() +",event.getY() = " + event.getY());
        int line = layout.getLineForVertical(mView.getScrollY()+(int)event.getY());
        int selectPostion = layout.getOffsetForHorizontal(line, (int)event.getX());
        int leftOffset = selectPostion;
        int rightOffset = selectPostion;
        while(leftOffset >= 0) {
            if(Character.isLetter(mView.getText().charAt(leftOffset))) {
                leftOffset--;
            } else {
                leftOffset++;
                break;
            }
        }
        while(rightOffset < length) {
            if(Character.isLetter(mView.getText().charAt(rightOffset))) {
                rightOffset++;
            } else {
                break;
            }
        }
        if(leftOffset > rightOffset) {
            return;
        }
        Selection.setSelection(mView.getEditableText(), leftOffset, rightOffset);
//        Log.d("test", "event = " + event.getAction() + ",selection [ " + leftOffset + "," + rightOffset + "]");
    }

    @Override
    protected String getSelectedText() {
        String selectedText = "";
        int start = mView.getSelectionStart();
        int end = mView.getSelectionEnd();
        if(start > 0 && end > 0 && start <= end) {
            selectedText = mView.getText().subSequence(start, end).toString();
//                Log.v("test", "getSelectionStart = " + start + ",getSelectionEnd = " + end
//                        + ",selected text = " + selectedWord);

            Selection.removeSelection(mView.getEditableText());
        }
        return selectedText;
    }

}
