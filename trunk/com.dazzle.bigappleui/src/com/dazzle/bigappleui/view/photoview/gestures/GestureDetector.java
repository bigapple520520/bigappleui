package com.dazzle.bigappleui.view.photoview.gestures;

import android.view.MotionEvent;

public interface GestureDetector {

    public boolean onTouchEvent(MotionEvent ev);

    public boolean isScaling();

    public void setOnGestureListener(OnGestureListener listener);

}
