/* 
 * @(#)ImageViewSel.java    Created on 2014-11-12
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
 * 这个类自定义绘制了一个打钩的图片
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-12 上午11:37:24 $
 */
public class HookImageView extends ImageView {
    /** 画笔 */
    private Paint paint;
    /** 控件的宽 */
    private int width;
    /** 控件的高 */
    private int height;

    private Path hookPath;

    public HookImageView(Context context) {
        this(context, null);
    }

    public HookImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HookImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        hookPath = getHookPath();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setAlpha(200);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == hookPath) {
            return;
        }

        canvas.drawPath(hookPath, paint);
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

    private int getXByScale(int scale) {
        return width * scale / 100;
    }

    private int getYByScale(int scale) {
        return height * scale / 100;
    }

}
