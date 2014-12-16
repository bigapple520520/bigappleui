/* 
 * @(#)HookDrawable.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.winupon.andframe.bigapple.utils.log.LogUtils;

/**
 * 打钩资源图片
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 上午10:14:46 $
 */
public class HookDrawable extends BaseDrawable {

    /** 画笔 */
    private Paint paint;

    private Path hookPath;

    /** 打钩颜色 */
    private int paintColor = Color.RED;

    public HookDrawable() {
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(200);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {

        Rect t = getBounds();
        LogUtils.d(t.toString());

        if (null == hookPath) {
            return;
        }

        paint.setColor(paintColor);
        canvas.drawPath(hookPath, paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        hookPath = getHookPath();
    }

    private Path getHookPath() {
        Path path = new Path();
        path.moveTo(getXByScale(35), getYByScale(48));
        path.lineTo(getXByScale(45), getYByScale(56));
        path.lineTo(getXByScale(57), getYByScale(32));
        path.lineTo(getXByScale(72), getYByScale(32));
        path.lineTo(getXByScale(47), getYByScale(68));
        path.lineTo(getXByScale(28), getYByScale(54));
        path.close();
        return path;
    }

    public int getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }

}
