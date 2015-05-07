/* 
 * @(#)DefaultFileDrawable.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils.ui.drawable.fileicon;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.dazzle.bigappleui.utils.ui.ColorUtils;
import com.dazzle.bigappleui.utils.ui.drawable.BaseDrawable;

/**
 * 默认文件图标
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 上午11:07:43 $
 */
public class DefaultFileDrawable extends BaseDrawable {
    /** 画笔 */
    private Paint paint;

    private Path path1;
    private Path path2;

    public DefaultFileDrawable() {
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        path1 = getPath1();
        path2 = getPath2();
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(ColorUtils.getColor("#BDBDBD"));
        canvas.drawPath(path1, paint);
        paint.setColor(Color.WHITE);
        canvas.drawPath(path2, paint);
    }

    private Path getPath1() {
        Path path = new Path();
        path.moveTo(getXByScale(10), getYByScale(10));
        path.lineTo(getXByScale(60), getYByScale(10));
        path.lineTo(getXByScale(80), getYByScale(30));
        path.lineTo(getXByScale(80), getYByScale(90));
        path.lineTo(getXByScale(10), getYByScale(90));
        path.close();
        return path;
    }

    private Path getPath2() {
        Path path = new Path();
        path.moveTo(getXByScale(15), getYByScale(15));
        path.lineTo(getXByScale(60), getYByScale(15));
        path.lineTo(getXByScale(60), getYByScale(30));
        path.lineTo(getXByScale(75), getYByScale(30));
        path.lineTo(getXByScale(75), getYByScale(85));
        path.lineTo(getXByScale(15), getYByScale(85));
        path.close();
        return path;
    }

}
