/* 
 * @(#)ArrowImageView.java    Created on 2014-11-24
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.img;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义绘制一个箭头图片
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-24 上午10:58:06 $
 */
public class ArrowImageView extends ImageView {
    private Paint paint;

    private int width;
    private int height;

    private Path rectPath1;
    private Path rectPath2;
    private Path rectPath3;
    private Path rectPath4;
    private Path trianglePath;

    public ArrowImageView(Context context) {
        this(context, null);
    }

    public ArrowImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#999999"));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        trianglePath = getTrianglePath();
        rectPath1 = getRect1();
        rectPath2 = getRect2();
        rectPath3 = getRect3();
        rectPath4 = getRect4();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == rectPath1 || null == rectPath2 || null == rectPath3 || null == rectPath4 || null == trianglePath) {
            return;
        }

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

    private int getXByScale(int scale) {
        return width * scale / 100;
    }

    private int getYByScale(int scale) {
        return height * scale / 100;
    }

}
