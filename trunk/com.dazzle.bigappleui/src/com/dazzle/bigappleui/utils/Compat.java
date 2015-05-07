/* 
 * @(#)Compat.java    Created on 2014-12-23
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;

/**
 * 高低版本兼容
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-23 下午4:41:34 $
 */
public abstract class Compat {
    /**
     * 设置View背景
     * 
     * @param view
     * @param drawable
     */
    public static void setViewBackgroundDrawable(View view, Drawable drawable) {
        if (VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(drawable);
        }
        else {
            view.setBackground(drawable);
        }
    }

}
