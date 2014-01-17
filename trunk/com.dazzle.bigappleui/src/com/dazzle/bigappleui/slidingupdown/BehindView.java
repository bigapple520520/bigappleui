/* 
 * @(#)BehindView.java    Created on 2014-1-8
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.slidingupdown;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 背面拉出来的View
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-8 下午7:48:16 $
 */
public class BehindView extends ViewGroup {
    private SlidingUpDownView slidingUpDownView;

    private View mUpBehind;// 从上出现的view
    private View mDownBehind;// 从下出现的view

    public BehindView(Context context) {
        this(context, null);
    }

    public BehindView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BehindView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);

        final int contentWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, height);
        mDownBehind.measure(contentWidthMeasureSpec, contentHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        mDownBehind.layout(0, 0, width, height);
    }

    public void setDownBehind(View view) {
        if (null != mDownBehind) {
            removeView(mDownBehind);
        }
        mDownBehind = view;
        addView(mDownBehind);
    }

    public SlidingUpDownView getSlidingUpDownView() {
        return slidingUpDownView;
    }

    public void setSlidingUpDownView(SlidingUpDownView slidingUpDownView) {
        this.slidingUpDownView = slidingUpDownView;
    }

}
