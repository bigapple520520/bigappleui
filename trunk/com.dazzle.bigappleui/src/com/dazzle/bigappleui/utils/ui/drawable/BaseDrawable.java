/* 
 * @(#)BaseDrawable.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils.ui.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * 自定义资源的基类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 上午9:28:44 $
 */
public class BaseDrawable extends Drawable {
    protected int width;
    protected int height;

    @Override
    public void draw(Canvas arg0) {
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public void setAlpha(int arg0) {
    }

    @Override
    public void setColorFilter(ColorFilter arg0) {
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        width = bounds.right - bounds.left;
        height = bounds.bottom - bounds.top;
    }

    protected int getXByScale(int scale) {
        return width * scale / 100;
    }

    protected int getYByScale(int scale) {
        return height * scale / 100;
    }

}
