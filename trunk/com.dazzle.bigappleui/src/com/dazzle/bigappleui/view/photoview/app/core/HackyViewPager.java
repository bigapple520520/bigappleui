/* 
 * @(#)HackyViewPager.java    Created on 2014-11-5
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.photoview.app.core;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 修改过几个异常的兼容性
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-5 下午5:17:32 $
 */
public class HackyViewPager extends ViewPager {
    private static final String TAG = "HackyViewPager";

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        }
        catch (IllegalArgumentException e) {
            Log.e(TAG, "hacky viewpager IllegalArgumentException：" + e.getMessage());
            return false;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "hacky viewpager ArrayIndexOutOfBoundsException：" + e.getMessage());
            return false;
        }
    }

}
