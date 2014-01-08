/* 
 * @(#)BehindView.java    Created on 2014-1-8
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.pullupdown;

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
    private View mBehind;

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
        mBehind.measure(contentWidthMeasureSpec, contentHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        mBehind.layout(0, 0, width, height);
    }

}
