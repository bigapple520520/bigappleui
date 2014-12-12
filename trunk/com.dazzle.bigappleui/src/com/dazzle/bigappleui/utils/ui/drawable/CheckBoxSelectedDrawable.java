/* 
 * @(#)CheckBoxSelectedDrawable.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.dazzle.bigappleui.utils.ui.ColorUtils;

/**
 * 多选框选中状态的图片资源
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 上午11:43:03 $
 */
public class CheckBoxSelectedDrawable extends BaseDrawable {
    /** 画笔 */
    private Paint paint;

    private Path path1;
    private Path path2;
    private Path path3;

    public CheckBoxSelectedDrawable() {
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
        path3 = getPath3();
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(ColorUtils.getColor("#BDBDBD"));
        canvas.drawPath(path1, paint);
        paint.setColor(Color.WHITE);
        canvas.drawPath(path2, paint);
        paint.setColor(ColorUtils.getColor("#7EC0EE"));
        canvas.drawPath(path3, paint);
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

    private Path getPath3() {
        Path path = new Path();
        path.moveTo(getXByScale(40), getYByScale(40));
        path.lineTo(getXByScale(49), getYByScale(49));
        path.lineTo(getXByScale(76), getYByScale(22));
        path.lineTo(getXByScale(83), getYByScale(28));
        path.lineTo(getXByScale(48), getYByScale(62));
        path.lineTo(getXByScale(33), getYByScale(47));
        path.close();
        return path;
    }

}
