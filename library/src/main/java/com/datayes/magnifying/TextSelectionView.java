package com.datayes.magnifying;

import android.content.Context;  
import android.graphics.Color;  
import android.text.Layout;  
import android.text.Selection;  
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;  
import android.view.Gravity;  
import android.view.MotionEvent;  
import android.widget.EditText;  
import android.widget.TextView;
   
public class TextSelectionView extends EditText {

    public interface WordSelectListener {
        void onWordSelected(String word);
    }

    private WordSelectListener mWordSelectListener;
 
    public TextSelectionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(); 
	}

	public TextSelectionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(); 
	}

	public TextSelectionView(Context context) {
        super(context);  
        initialize();  
    }  
  
    private void initialize() {  
        setGravity(Gravity.TOP);  
        setBackgroundColor(Color.WHITE);  
    }

    public void setWordSelectListener(WordSelectListener listener) {
        mWordSelectListener = listener;
    }
      
    @Override  
    protected void onCreateContextMenu(ContextMenu menu) {  

    }  
      
    @Override  
    public boolean getDefaultEditable() {  
        return false;  
    }  
      
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        int action = event.getAction();
        switch(action) {  
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:
        	selectWord(event);
            break;
        case MotionEvent.ACTION_UP:
        	int start = getSelectionStart();
        	int end = getSelectionEnd();
            if(start > 0 && end > 0 && start <= end) {
                String selectedWord = getText().subSequence(start, end).toString();
                Log.v("test", "getSelectionStart = " + start + ",getSelectionEnd = " + end
                        + ",selected text = " + selectedWord);
                if (mWordSelectListener != null) {
                    mWordSelectListener.onWordSelected(selectedWord);
                }
                Selection.removeSelection(getEditableText());
            }
        	break;
        }  
        return true;  
    }
    
    private void selectWord(MotionEvent event) {
    	Layout layout = getLayout();
    	int length = getText().length();
    	int line = layout.getLineForVertical(getScrollY()+(int)event.getY());   
        int selectPostion = layout.getOffsetForHorizontal(line, (int)event.getX());
        int leftOffset = selectPostion;
        int rightOffset = selectPostion;
        while(leftOffset >= 0) {
        	if(Character.isWhitespace(getText().charAt(leftOffset)) 
        			|| isPunctuation(getText().charAt(leftOffset))) {
        		leftOffset++;
        		break;
        	}
        	leftOffset--;
        }
        while(rightOffset < length) {
        	if(Character.isWhitespace(getText().charAt(rightOffset))
        			|| isPunctuation(getText().charAt(leftOffset))) {
        		break;
        	}
        	rightOffset++;
        }
        if(leftOffset > rightOffset) {
        	return;
        }
        Selection.setSelection(getEditableText(), leftOffset, rightOffset);  
        Log.d("test", "event = " + event.getAction() + ",selection [ " + leftOffset + "," + rightOffset + "]");
    }
    
    public static boolean isPunctuation(char c) {
        return c == ','
            || c == '.'
            || c == '!'
            || c == '?'
            || c == ':'
            || c == ';'
            ;
    }
}  