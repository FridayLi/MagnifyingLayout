package com.datayes.magnifying;

import android.content.Context;  
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Gravity;  
import android.view.MotionEvent;  
import android.widget.EditText;

public class TextSelectionView extends EditText {

    private TextSelection mTextSelection;

    private boolean mSelectable;

    public interface TextSelectListener {
        void onTextSelected(String selected);
    }

    private TextSelectListener mTextSelectListener;
 
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
        mTextSelection = new WordSelection(this);
        setGravity(Gravity.TOP);  
        setBackgroundColor(Color.WHITE);  
    }

    public void setTextSelection(TextSelection selection) {
        mTextSelection = selection;
    }

    public void setTextSelectListener(TextSelectListener listener) {
        mTextSelectListener = listener;
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
        if (mSelectable) {
            int action = event.getAction();
            switch(action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mTextSelection.select(event);
                break;
            case MotionEvent.ACTION_UP:
                if (mTextSelectListener != null) {
                    mTextSelectListener.onTextSelected(mTextSelection.getSelectedText());
                }
                break;
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

}  