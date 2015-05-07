/* 
 * @(#)UnreadBitmapProcessor.java    Created on 2014-9-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.dazzle.bigappleui.utils.ui.DisplayUtils;

/**
 * 给图片加上未读消息处理器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-9-12 下午3:30:39 $
 */
public class UnreadBitmapProcessor {
    public static final int TRANSPARENT_BITMAP = 0;// 使用默认透明的背景

    private Context context;
    private Paint paint;
    private Canvas canvas;
    private Bitmap drawBitmap;

    // 未读提示默认是在图标的右上角，用下面两个参数可以进行便宜调整
    private float offsetWidth = 0;
    private float offsetHeight = 0;

    private float paintTextSize = 0;// 数字体大小
    private int paintColor = Color.WHITE;

    public UnreadBitmapProcessor(Context context, int resid) {
        this.context = context;
        if (UnreadBitmapProcessor.TRANSPARENT_BITMAP == resid) {
            drawBitmap = Bitmap.createBitmap(22, 22, Bitmap.Config.ALPHA_8);// 无背景透明
        }
        else {
            drawBitmap = BitmapFactory.decodeResource(context.getResources(), resid);// 自定义背景
        }

        paint = new Paint();
        paint.setTextAlign(Align.CENTER);
    }

    public Bitmap process(Bitmap fromBitmap, int num) {
        Bitmap temp = fromBitmap.copy(Config.ARGB_8888, true);

        if (0 == paintTextSize) {
            paintTextSize = drawBitmap.getWidth() * 0.8f;// 字体的图片默认根据背景的大小来调整
        }
        paint.setTextSize(paintTextSize);
        paint.setColor(paintColor);

        canvas = new Canvas(temp);

        // 画上红色的圈圈，如果没有初始化过背景就不绘制背景
        int left = temp.getWidth() - drawBitmap.getWidth();
        canvas.drawBitmap(drawBitmap, left - offsetWidth, 0 + offsetHeight, paint);

        // 画上数字
        int x = (int) (temp.getWidth() - drawBitmap.getWidth() / 2d);
        int y = (int) (drawBitmap.getWidth() * 0.8d);

        String text = num > 9 ? "n" : String.valueOf(num);
        canvas.drawText(text, x - offsetWidth, y + offsetHeight, paint);

        return temp;
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

    // //////////////////////////////////////// 内置工具方法//////////////////////////////////////////////////
    private float getPxByDp(float dp) {
        if (null != context && context instanceof Activity) {
            return DisplayUtils.getPxByDp((Activity) context, dp);
        }
        else {
            return dp;
        }
    }

    private float getPxBySp(float sp) {
        if (null != context && context instanceof Activity) {
            return DisplayUtils.getPxBySp((Activity) context, sp);
        }
        else {
            return sp;
        }
    }

}
