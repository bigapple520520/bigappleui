package com.dazzle.bigappleui.view.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 垂直ScrollView，防止垂直滚动和内部的水平滚动冲突
 * 
 * @author xuan
 */
public class ANVerticalScrollView extends ScrollView {
	private float mXdistance;
	private float mYdistance;

	private float mLastX;
	private float mLastY;

	public ANVerticalScrollView(Context context) {
		super(context);
	}

	public ANVerticalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mXdistance = mYdistance = 0f;
			mLastX = ev.getX();
			mLastY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();
			mXdistance += Math.abs(curX - mLastX);
			mYdistance += Math.abs(curY - mLastY);
			mLastX = curX;
			mLastY = curY;
			if (mXdistance > mYdistance) {
				return false;
			}
		}

		return super.onInterceptTouchEvent(ev);
	}

}
