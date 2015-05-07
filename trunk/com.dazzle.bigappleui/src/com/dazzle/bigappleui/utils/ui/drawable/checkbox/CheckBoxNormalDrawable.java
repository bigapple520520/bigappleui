/* 
 * @(#)CheckBoxNormalDrawable.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils.ui.drawable.checkbox;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.dazzle.bigappleui.utils.ui.ColorUtils;
import com.dazzle.bigappleui.utils.ui.drawable.BaseDrawable;

/**
 * 多选框正常状态的图片资源
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 上午11:31:58 $
 */
public class CheckBoxNormalDrawable extends BaseDrawable {
    /** 画笔 */
    private Paint paint;

    private Path path1;
    private Path path2;

    public CheckBoxNormalDrawable() {
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
        path.moveTo(getXByScale(25), getYByScale(25));
        path.lineTo(getXByScale(75), getYByScale(25));
        path.lineTo(getXByScale(75), getYByScale(75));
        path.lineTo(getXByScale(25), getYByScale(75));
        path.close();
        return path;
    }

    private Path getPath2() {
        Path path = new Path();
        path.moveTo(getXByScale(30), getYByScale(30));
        path.lineTo(getXByScale(70), getYByScale(30));
        path.lineTo(getXByScale(70), getYByScale(70));
        path.lineTo(getXByScale(30), getYByScale(70));
        path.close();
        return path;
    }

}
