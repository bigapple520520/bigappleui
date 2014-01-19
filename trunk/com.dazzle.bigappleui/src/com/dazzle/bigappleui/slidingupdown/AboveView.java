/* 
 * @(#)AboveView.java    Created on 2014-1-8
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.slidingupdown;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 正面View
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-8 下午7:47:59 $
 */
public class AboveView extends ViewGroup {
	private SlidingUpDownView slidingUpDownView;
	private View mAbove;

	private final int touchSlop;// 触发后小于改距离的，不移动
	private final Scroller scroller;// 使切view的时候效果比较平滑
	private VelocityTracker velocityTracker;// 计算手势的一些速率等的工具类
	private float lastMotionY;// 记录最后一次y坐标值

	private int mode = SlidingUpDownView.MODE_UP;

	private int touchState = TOUCH_STATE_REST;
	private static final int TOUCH_STATE_REST = 0;// 空闲状态
	private static final int TOUCH_STATE_SCROLLING = 1;// 正在滚动状态

	private static final int SNAP_VELOCITY = 600;// 单位px，每秒滑过的px距离

	private int curLocation = LOCATION_MIDDLE;
	public static final int LOCATION_UP = 0;// 屏幕在上面
	public static final int LOCATION_MIDDLE = 1;// 在中间
	public static final int LOCATION_DOWN = 2;// 在下面

	public AboveView(Context context) {
		this(context, null);
	}

	public AboveView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AboveView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		touchSlop = ViewConfigurationCompat
				.getScaledPagingTouchSlop(ViewConfiguration.get(context));
		scroller = new Scroller(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (SlidingUpDownView.MODE_NONE == mode) {
			return false;
		}

		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (touchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastMotionY = y;

			// 如果正在滑动，按下事件需要截留
			touchState = scroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_MOVE:
			// 如果滑动到需要滑动界面的参数，需要截留
			final int yDiff = (int) Math.abs(lastMotionY - y);
			if (yDiff > touchSlop) {
				touchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			touchState = TOUCH_STATE_REST;
			break;
		}

		return touchState != TOUCH_STATE_REST;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (SlidingUpDownView.MODE_NONE == mode) {
			return false;
		}

		if (null == velocityTracker) {
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(event);

		final int action = event.getAction();
		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!scroller.isFinished()) {
				scroller.abortAnimation();
			}
			lastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = (int) (lastMotionY - y);
			lastMotionY = y;

			scrollBy(0, deltaY);
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			final VelocityTracker vt = velocityTracker;
			vt.computeCurrentVelocity(1000);
			int velocityY = (int) vt.getYVelocity();
			int scrollY = getScrollY();

			Log.d("-----------", "-----------iii" + scrollY + "vvvvv"
					+ velocityY);

			if (velocityY < -SNAP_VELOCITY) {// 快速向上
				if ((isLocationMiddle() && scrollY > 0) || isLocationUp()) {
					snapToUp();
				} else {
					snapToMiddle();
				}
			} else if (velocityY > SNAP_VELOCITY) {// 快速向下
				if ((isLocationMiddle() && scrollY < 0) || isLocationDown()) {
					snapToDown();
				} else {
					snapToMiddle();
				}
			} else {
				snapToDestination();
			}

			if (null != velocityTracker) {
				velocityTracker.recycle();
				velocityTracker = null;
			}
			touchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	// //////////////////////////////////////根据当前Location位置调整界面/////////////////////////////////////
	// 计算当前位置偏向哪种状态
	private int countScrollYOffsetWhichLocation() {
		int scrollY = getScrollY();
		int halfHeight = getHeight() / 2;

		int offsetLocation;
		if (scrollY > halfHeight) {
			offsetLocation = LOCATION_UP;
		} else if (scrollY < -halfHeight) {
			offsetLocation = LOCATION_DOWN;
		} else {
			offsetLocation = LOCATION_MIDDLE;
		}

		return offsetLocation;
	}

	/**
	 * 根据当前位置计算，above界面应该在那个位置
	 */
	public void snapToDestination() {
		snapToScreen(countScrollYOffsetWhichLocation());
	}

	// 根据要滑动的状态计算要滑动的距离
	private int getDestByWhichScreenToGo(int which) {
		int aboveHeight = getHeight();

		int dest = 0;
		switch (which) {
		case LOCATION_UP:
			dest = aboveHeight;
			break;
		case LOCATION_MIDDLE:
			dest = 0;
			break;
		case LOCATION_DOWN:
			dest = -aboveHeight;
			break;
		}

		return dest;
	}

	/**
	 * 直接跳转到指定位置
	 * 
	 * @param which
	 */
	public void setToScreen(int which) {
		int dest = getDestByWhichScreenToGo(which);
		scrollTo(0, dest);
	}

	/**
	 * 平滑的切换到指定屏幕，0使above刚好在上面，1使above刚好在中间，2使above刚好在下面
	 * 
	 * @param whichScreen
	 */
	public void snapToScreen(int which) {
		int dest = getDestByWhichScreenToGo(which);

		if (getScrollY() != dest) {
			int delta = dest - getScrollY();
			scroller.startScroll(0, getScrollY(), 0, delta, Math.abs(delta) * 2);

			if (null != slidingUpDownView.getSlidingUpDownListener()) {
				slidingUpDownView.getSlidingUpDownListener().whichScreen(which);
			}

			curLocation = which;
			invalidate();
		}
	}

	public void snapToUp() {
		snapToScreen(LOCATION_UP);
	}

	public boolean isLocationUp() {
		return LOCATION_UP == curLocation;
	}

	public void snapToDown() {
		snapToScreen(LOCATION_DOWN);
	}

	public boolean isLocationDown() {
		return LOCATION_DOWN == curLocation;
	}

	public void snapToMiddle() {
		snapToScreen(LOCATION_MIDDLE);
	}

	public boolean isLocationMiddle() {
		return LOCATION_MIDDLE == curLocation;
	}

	public int getCurLocation() {
		return curLocation;
	}

	public void setCurLocation(int curLocation) {
		this.curLocation = curLocation;
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			invalidate();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		setMeasuredDimension(width, height);

		final int contentWidthMeasureSpec = getChildMeasureSpec(
				widthMeasureSpec, 0, width);
		final int contentHeightMeasureSpec = getChildMeasureSpec(
				heightMeasureSpec, 0, height);
		mAbove.measure(contentWidthMeasureSpec, contentHeightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		mAbove.layout(0, 0, width, height);
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setAbove(View view) {
		if (null != mAbove) {
			removeView(mAbove);
		}
		mAbove = view;
		addView(mAbove);
	}

	public SlidingUpDownView getSlidingUpDownView() {
		return slidingUpDownView;
	}

	public void setSlidingUpDownView(SlidingUpDownView slidingUpDownView) {
		this.slidingUpDownView = slidingUpDownView;
	}

}
