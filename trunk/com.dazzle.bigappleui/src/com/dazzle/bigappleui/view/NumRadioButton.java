/* 
 * @(#)NumRadioButton.java    Created on 2013-10-29
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * 自定义RadioButton做tab按钮，可在上面绘制未读消息数
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-29 下午2:37:01 $
 */
public class NumRadioButton extends RadioButton {
    private static Paint paint;
    static {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(18);
    }

    private int num;
    private Bitmap drawBitmap;
    private Context context;
    private int width;

    public NumRadioButton(Context context) {
        super(context);
        init(context);
    }

    public NumRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NumRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (num > 0) {
            // 画上红色的圈圈
            int left = width - drawBitmap.getWidth();
            canvas.drawBitmap(drawBitmap, left, 0, paint);

            // 画上数字
            int x = (int) (width - drawBitmap.getWidth() / 2d);
            int y = (int) (drawBitmap.getWidth() * 0.7d);
            String text = num > 9 ? "n" : String.valueOf(num);
            canvas.drawText(text, x, y, paint);
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int n, int resid) {
        if (n > 0 && n != num) {
            this.num = n;
            initDrawBitmap(resid);
            invalidate();
        }
    }

    private void init(Context context) {
        this.context = context;
    }

    private void initDrawBitmap(int resid) {
        if (null == drawBitmap) {
            drawBitmap = BitmapFactory.decodeResource(context.getResources(), resid);
        }
    }

}
