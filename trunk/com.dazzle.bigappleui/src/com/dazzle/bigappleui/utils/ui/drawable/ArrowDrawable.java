/* 
 * @(#)ArrowDrawable.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.dazzle.bigappleui.utils.ui.ColorUtils;

/**
 * 一个箭头图片资源
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 上午9:30:10 $
 */
public class ArrowDrawable extends BaseDrawable {
    private Paint paint;

    private Path rectPath1;
    private Path rectPath2;
    private Path rectPath3;
    private Path rectPath4;
    private Path trianglePath;

    /** 箭头的颜色 */
    private int paintColor = ColorUtils.getColor("#999999");

    public ArrowDrawable() {
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        if (null == rectPath1 || null == rectPath2 || null == rectPath3 || null == rectPath4 || null == trianglePath) {
            return;
        }
        paint.setColor(paintColor);
        paint.setAlpha(100);
        canvas.drawPath(rectPath1, paint);
        paint.setAlpha(150);
        canvas.drawPath(rectPath2, paint);
        paint.setAlpha(200);
        canvas.drawPath(rectPath3, paint);
        paint.setAlpha(255);
        canvas.drawPath(rectPath4, paint);
        canvas.drawPath(trianglePath, paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        trianglePath = getTrianglePath();
        rectPath1 = getRect1();
        rectPath2 = getRect2();
        rectPath3 = getRect3();
        rectPath4 = getRect4();
    }

    private Path getRect1() {
        Path path = new Path();
        path.moveTo(getXByScale(37), getYByScale(9));
        path.lineTo(getXByScale(63), getYByScale(9));
        path.lineTo(getXByScale(63), getYByScale(14));
        path.lineTo(getXByScale(37), getYByScale(14));
        path.close();
        return path;
    }

    private Path getRect2() {
        Path path = new Path();
        path.moveTo(getXByScale(37), getYByScale(18));
        path.lineTo(getXByScale(63), getYByScale(18));
        path.lineTo(getXByScale(63), getYByScale(26));
        path.lineTo(getXByScale(37), getYByScale(26));
        path.close();
        return path;
    }

    private Path getRect3() {
        Path path = new Path();
        path.moveTo(getXByScale(37), getYByScale(30));
        path.lineTo(getXByScale(63), getYByScale(30));
        path.lineTo(getXByScale(63), getYByScale(40));
        path.lineTo(getXByScale(37), getYByScale(40));
        path.close();
        return path;
    }

    private Path getRect4() {
        Path path = new Path();
        path.moveTo(getXByScale(37), getYByScale(44));
        path.lineTo(getXByScale(63), getYByScale(44));
        path.lineTo(getXByScale(63), getYByScale(60));
        path.lineTo(getXByScale(37), getYByScale(60));
        path.close();
        return path;
    }

    private Path getTrianglePath() {
        Path path = new Path();
        path.moveTo(getXByScale(27), getYByScale(60));
        path.lineTo(getXByScale(73), getYByScale(60));
        path.lineTo(getXByScale(50), getYByScale(84));
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
