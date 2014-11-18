/* 
 * @(#)ImageViewSel.java    Created on 2014-11-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 选中效果遮罩
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-12 上午11:37:24 $
 */
public class ImageViewSel extends ImageView {
    private Paint paint;

    private int width;
    private int height;

    public ImageViewSel(Context context) {
        super(context);
        init();
    }

    public ImageViewSel(Context context, AttributeSet attrs) {
        super(context, attrs);// 内部会调用第一个构造方法的
    }

    public ImageViewSel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);// 内部会调用第一个构造方法的
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(6);
        paint.setAntiAlias(true);
        paint.setAlpha(200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine((width * 3) / 8, (height * 4) / 8, (width * 7) / 16, (height * 5) / 8, paint);
        canvas.drawLine((width * 7) / 16, (height * 5) / 8, (width * 5) / 8, (width * 3) / 8, paint);
        canvas.drawPoint((width * 7) / 16, (height * 5) / 8, paint);
    }

}
