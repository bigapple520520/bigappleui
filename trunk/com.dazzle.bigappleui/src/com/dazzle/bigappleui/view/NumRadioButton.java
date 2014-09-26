/* 
 * @(#)NumRadioButton.java    Created on 2013-10-29
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.dazzle.bigappleui.utils.DisplayUtils;

/**
 * 自定义RadioButton做tab按钮，可在上面绘制未读消息数
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-29 下午2:37:01 $
 */
public class NumRadioButton extends RadioButton {
    public static final int NOT_INIT_BITMAP = -1;// 不加载背景
    public static final int TRANSPARENT_BITMAP = 0;// 使用默认透明的背景

    private static Paint paint;
    static {
        paint = new Paint();
        paint.setTextAlign(Align.CENTER);
    }

    private int num;
    private Bitmap drawBitmap;
    private int width;

    // 未读提示默认是在图标的右上角，用下面两个参数可以进行便宜调整
    private float offsetWidth = 0;
    private float offsetHeight = 0;

    private float paintTextSize = 0;// 数字体大小
    private int paintColor = Color.WHITE;

    public NumRadioButton(Context context) {
        super(context);
    }

    public NumRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (num > 0 && null != drawBitmap) {
            // 设置画笔参数
            if (0 == paintTextSize) {
                paintTextSize = drawBitmap.getWidth() * 0.8f;// 字体的图片默认根据背景的大小来调整
            }
            paint.setTextSize(paintTextSize);
            paint.setColor(paintColor);

            // 画上红色的圈圈，如果没有初始化过背景就不绘制背景
            int left = width - drawBitmap.getWidth();
            canvas.drawBitmap(drawBitmap, left - offsetWidth, 0 + offsetHeight, paint);

            // 画上数字
            int x = (int) (width - drawBitmap.getWidth() / 2d);
            int y = (int) (drawBitmap.getWidth() * 0.8d);

            String text = num > 9 ? "n" : String.valueOf(num);
            canvas.drawText(text, x - offsetWidth, y + offsetHeight, paint);
        }
    }

    /**
     * 获取未读消息数
     * 
     * @return
     */
    public int getNum() {
        return num;
    }

    /**
     * 设置未读消息数
     * 
     * @param n
     *            如果n小于等于0，不会绘制未读消息数
     * @param resid
     *            绘制未读消息数的背景
     */
    public void setNum(int n, int resid) {
        if (n != num) {
            this.num = n;
            initDrawBitmap(resid);
            invalidate();
        }
    }

    /**
     * 设置未读消息数
     * 
     * @param n
     *            如果n小于等于0，不会绘制未读消息数
     * @param resid
     *            绘制未读消息数的背景
     */
    public void setNum(int n) {
        setNum(n, NumRadioButton.TRANSPARENT_BITMAP);
    }

    /**
     * 去掉未读消息数
     */
    public void clearNum() {
        setNum(0, NumRadioButton.NOT_INIT_BITMAP);
    }

    // /////////////////////////////////////////////设置数字体颜色////////////////////////////////////////////////
    /**
     * 设置数字体颜色
     * 
     * @param paintColor
     */
    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
    }

    // /////////////////////////////////////////////设置数字体大小///////////////////////////////////////////////
    /**
     * 用px设置数字大小
     * 
     * @param paintTextSize
     */
    public void setPaintTextSizeByPx(float paintTextSizePx) {
        this.paintTextSize = paintTextSizePx;
    }

    /**
     * 用dp设置数字大小
     * 
     * @param paintTextSizeDp
     */
    public void setPaintTextSizeBySp(float paintTextSizeSp) {
        this.paintTextSize = getPxBySp(paintTextSizeSp);
    }

    // //////////////////////////////////////////设置未读块的偏移////////////////////////////////////////////////
    /**
     * 根据px值设置偏移宽
     * 
     * @param offsetWidthPx
     */
    public void setOffsetWidthByPx(float offsetWidthPx) {
        this.offsetWidth = offsetWidthPx;
    }

    /**
     * 根据px值设置便宜高
     * 
     * @param offsetHeightPx
     */
    public void setOffsetHeightByPx(float offsetHeightPx) {
        this.offsetHeight = offsetHeightPx;
    }

    /**
     * 根据px值设置偏移宽
     * 
     * @param offsetWidthDp
     */
    public void setOffsetWidthByDp(float offsetWidthDp) {
        this.offsetWidth = getPxByDp(offsetWidthDp);
    }

    /**
     * 根据px值设置便宜高
     * 
     * @param offsetHeightDp
     */
    public void setOffsetHeightByDp(float offsetHeightDp) {
        this.offsetHeight = getPxByDp(offsetHeightDp);
    }

    // //////////////////////////////////////////内置工具方法////////////////////////////////////////////////
    private float getPxBySp(float sp) {
        Context context = getContext();
        if (context instanceof Activity) {
            return DisplayUtils.getPxBySp((Activity) context, sp);
        }
        else {
            return sp;
        }
    }

    private float getPxByDp(float dp) {
        Context context = getContext();
        if (context instanceof Activity) {
            return DisplayUtils.getPxByDp((Activity) context, dp);
        }
        else {
            return dp;
        }
    }

    private void initDrawBitmap(int resid) {
        if (NumRadioButton.NOT_INIT_BITMAP == resid) {
            return;
        }

        if (NumRadioButton.TRANSPARENT_BITMAP == resid) {
            // 无背景透明
            drawBitmap = Bitmap.createBitmap(22, 22, Bitmap.Config.ALPHA_8);
        }
        else {
            // 自定义背景
            drawBitmap = BitmapFactory.decodeResource(getResources(), resid);
        }
    }

}
