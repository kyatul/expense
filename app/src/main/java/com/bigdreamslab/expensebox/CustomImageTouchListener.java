package com.bigdreamslab.expensebox;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class CustomImageTouchListener implements View.OnTouchListener {
    int backgroundColor;

    public CustomImageTouchListener(Context context) {
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ((ImageView) view).setBackgroundColor(0xFF84d2ef);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ((ImageView) view).setBackgroundColor(0xfffafafa);
                break;
        }
        return false;
    }
}