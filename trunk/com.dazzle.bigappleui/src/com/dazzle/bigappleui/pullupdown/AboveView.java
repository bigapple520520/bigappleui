/* 
 * @(#)AboveView.java    Created on 2014-1-8
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.pullupdown;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
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

    private View mAbove;

    private final int touchSlop;// 触发后小于改距离的，不移动
    private final Scroller scroller;// 使切view的时候效果比较平滑
    private VelocityTracker velocityTracker;// 计算手势的一些速率等的工具类
    private float lastMotionY;// 记录最后一次y坐标值

    private int mode = PullUpDownView.MODE_UP;

    private int touchState = TOUCH_STATE_REST;
    private static final int TOUCH_STATE_REST = 0;// 空闲状态
    private static final int TOUCH_STATE_SCROLLING = 1;// 正在滚动状态

    private static final int SNAP_VELOCITY = 600;// 单位px，每秒滑过的px距离

    public AboveView(Context context) {
        this(context, null);
    }

    public AboveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AboveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
        scroller = new Scroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (PullUpDownView.MODE_NONE == mode) {
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
            int deltaX = (int) (lastMotionY - y);
            lastMotionY = y;

            int wantToOffset = getScrollY() + deltaX;
            if (wantToOffset >= 0) {
                scrollBy(deltaX, 0);
            }
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            final VelocityTracker vt = velocityTracker;
            vt.computeCurrentVelocity(1000);
            int velocityY = (int) vt.getYVelocity();

            if (velocityY > SNAP_VELOCITY) {// 快速向上
            }
            else if (velocityY < -SNAP_VELOCITY) {// 快速向下
            }
            else {
                snapToDestination();
            }

            if (null != velocityTracker) {
                velocityTracker.recycle();
                velocityTracker = null;
            }
            touchState = TOUCH_STATE_REST;
            break;
        }

        return false;
    }

    // @Override
    // public boolean onInterceptTouchEvent(MotionEvent ev) {
    // if (PullUpDownView.MODE_NONE == mode) {
    // return false;
    // }
    //
    // final int action = ev.getAction();
    // if ((action == MotionEvent.ACTION_MOVE) && (touchState != TOUCH_STATE_REST)) {
    // return true;
    // }
    //
    // final float y = ev.getY();
    // switch (action) {
    // case MotionEvent.ACTION_DOWN:
    // lastMotionY = y;
    // touchState = scroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
    // break;
    // case MotionEvent.ACTION_MOVE:
    // final int xDiff = (int) Math.abs(lastMotionY - y);
    // if (xDiff > touchSlop) {
    // touchState = TOUCH_STATE_SCROLLING;
    // }
    // break;
    // case MotionEvent.ACTION_CANCEL:
    // case MotionEvent.ACTION_UP:
    // touchState = TOUCH_STATE_REST;
    // break;
    // }
    //
    // return touchState != TOUCH_STATE_REST;
    // }

    public void snapToDestination() {
        final int destScreen = (getScrollY() + getHeight() / 2) / getHeight();
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

        final int contentWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, height);
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

}
