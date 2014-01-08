/* 
 * @(#)PullUpDownView.java    Created on 2014-1-8
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.pullupdown;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 下拉或者上拉惊喜哦
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-8 下午7:46:07 $
 */
public class PullUpDownView extends FrameLayout {
    // 下拉或者上拉模式
    public static final int MODE_NONE = 0;// 不能上下拉
    public static final int MODE_UP = 1;// 上拉
    public static final int MODE_DOWN = 2;// 下拉
    public static final int MODE_UP_DOWN = 3;// 上下都可拉

    private View mAbove;
    private View mBehind;

    public PullUpDownView(Context context) {
        this(context, null);
    }

    public PullUpDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullUpDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addAboveAndBehind(View mAbove, View mBehind) {
        addAbove(mAbove);
        addBehind(mBehind);
    }

    public void addAbove(View mAbove) {
        this.mAbove = mAbove;
    }

    public void addBehind(View mBehind) {
        this.mBehind = mBehind;
    }

}
