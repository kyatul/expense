package com.bigdreamslab.expensebox;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class CustomTextTouchListener implements View.OnTouchListener {
    int upColor;

    public CustomTextTouchListener(Context context,int color) {
        upColor = color;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ((TextView) view).setBackgroundColor(0xFF84d2ef);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ((TextView) view).setBackgroundColor(upColor);
                break;
        }
        return false;
    }
}