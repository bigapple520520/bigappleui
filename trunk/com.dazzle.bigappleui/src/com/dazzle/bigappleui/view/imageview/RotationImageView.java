/* 
 * @(#)RotationImageView.java    Created on 2014-12-15
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.imageview;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 可设置旋转角度图片控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-15 上午9:12:57 $
 */
public class RotationImageView extends ImageView {

    /** 旋转角度，顺时针旋转例如90度就设置90 */
    private int rotationDegree;
    /** 用户原本设置的显示图片类型 */
    private ScaleType originalScaleType;
    /** 调整的变化参数 */
    private Matrix matrix;

    /** 资源图片的宽和高，这里就是图片的宽和高 */
    private float dwidth;
    private float dheight;

    private float originalDwidth;
    private float originalDheight;

    /** 旋转过后需要调整的XY方向的偏移 */
    private float offsetXAfterRotate;
    private float offsetYAfterRotate;

    /** 用来设置fitCenter、fitEnd、fitStart、fitXY */
    RectF mTempSrc = new RectF();
    RectF mTempDst = new RectF();

    /**
     * 构造方法
     * 
     * @param context
     */
    public RotationImageView(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     * 
     * @param context
     * @param attrs
     */
    public RotationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 构造方法
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public RotationImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /** 初始化 */
    private void init(final Context context) {
        matrix = new Matrix();
        setScaleType(ScaleType.FIT_CENTER);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(ScaleType.MATRIX);
        originalScaleType = scaleType;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refreshMatrix();
    }

    @Override
    public void setImageDrawable(Drawable d) {
        super.setImageDrawable(d);
        dwidth = d.getIntrinsicWidth();
        dheight = d.getIntrinsicHeight();
        originalDwidth = dwidth;
        originalDheight = dheight;
        refreshMatrix();
    }

    /** 调整Matrix值 */
    private void refreshMatrix() {
        int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int vheight = getHeight() - getPaddingTop() - getPaddingBottom();
        if (vwidth <= 0 || vheight <= 0) {
            return;
        }

        // 旋转后重新调整图片的资源的宽和高
        recalculateDrawableWidthHeightIfDegree();

        // 判断是否刚刚好
        boolean fits = (dwidth < 0 || vwidth == dwidth) && (dheight < 0 || vheight == dheight);

        if (ScaleType.MATRIX == originalScaleType) {
            throw new IllegalArgumentException("not support matrix");
        }
        else if (fits) {
            // 刚刚好就不做处理了
        }
        if (ScaleType.CENTER_CROP == originalScaleType) {
            float scale;
            float dx = 0, dy = 0;

            if (dwidth * vheight > vwidth * dheight) {
                scale = vheight / dheight;
                dx = (vwidth - dwidth * scale) * 0.5f;
            }
            else {
                scale = vwidth / dwidth;
                dy = (vheight - dheight * scale) * 0.5f;
            }

            matrix.setScale(scale, scale);
            matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        }
        else if (ScaleType.CENTER_INSIDE == originalScaleType) {
            float scale;
            float dx;
            float dy;

            if (dwidth <= vwidth && dheight <= vheight) {
                scale = 1.0f;
            }
            else {
                scale = Math.min(vwidth / dwidth, vheight / dheight);
            }

            dx = (int) ((vwidth - dwidth * scale) * 0.5f + 0.5f);
            dy = (int) ((vheight - dheight * scale) * 0.5f + 0.5f);

            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);
        }
        else if (ScaleType.CENTER == originalScaleType) {
            matrix.setTranslate((int) ((vwidth - dwidth) * 0.5f + 0.5f), (int) ((vheight - dheight) * 0.5f + 0.5f));
        }
        else {
            mTempSrc.set(0, 0, dwidth, dheight);
            mTempDst.set(0, 0, vwidth, vheight);
            matrix.setRectToRect(mTempSrc, mTempDst, scaleTypeToScaleToFit(originalScaleType));
        }

        matrix.preTranslate(offsetXAfterRotate, offsetYAfterRotate);
        matrix.preRotate(rotationDegree, originalDwidth * 0.5f, originalDheight * 0.5f);
        setImageMatrix(matrix);
    }

    /** 返回fit类型 */
    private Matrix.ScaleToFit scaleTypeToScaleToFit(ScaleType st) {
        if (ScaleType.FIT_XY == st) {
            return Matrix.ScaleToFit.FILL;
        }
        else if (ScaleType.FIT_START == st) {
            return Matrix.ScaleToFit.START;
        }
        else if (ScaleType.FIT_END == st) {
            return Matrix.ScaleToFit.END;
        }
        else if (ScaleType.FIT_CENTER == st) {
            return Matrix.ScaleToFit.CENTER;
        }
        else {
            return Matrix.ScaleToFit.FILL;
        }
    }

    /** 图片根据角度重新计算他的宽高，并计算旋转后需要XY偏移的量 */
    private void recalculateDrawableWidthHeightIfDegree() {
        if (0 == rotationDegree) {
            offsetXAfterRotate = 0;
            offsetYAfterRotate = 0;
            return;
        }

        /**
         * 算法如下
         * 
         * 以(x0,y0)为旋转中心点<br>
         * 已经知旋转前点的位置(x1,y1)和旋转的角度a，求旋转后点的新位置(x2,y2)
         * 
         * 如果是逆时针旋转： <br>
         * x2 = (x1 - x0) * cosa - (y1 - y0) * sina + x0 <br>
         * y2 = (y1 - y0) * cosa + (x1 - x0) * sina + y0 <br>
         * 如果是顺时针旋转： <br>
         * x2 = (x1 - x0) * cosa + (y1 - y0) * sina + x0 <br>
         * y2 = (y1 - y0) * cosa - (x1 - x0) * sina + y0 <br>
         */
        // 矩形四个角的点坐标，矩形左上角为原点，向下向右为正，顺时针排列变量：old1,old2,old3,old4
        PointF basePoint = new PointF(dwidth * 0.5f, dheight * 0.5f);
        PointF old1 = new PointF(0, 0);
        PointF old2 = new PointF(dwidth, 0);
        PointF old3 = new PointF(dwidth, dheight);
        PointF old4 = new PointF(0, dheight);

        // 旋转过后的四个点
        PointF new1 = new PointF();
        PointF new2 = new PointF();
        PointF new3 = new PointF();
        PointF new4 = new PointF();
        double radians = Math.toRadians(rotationDegree);// 角度转换成弧度
        new1.x = (float) ((old1.x - basePoint.x) * Math.cos(radians) + (old1.y - basePoint.y) * Math.sin(radians))
                + basePoint.x;
        new1.y = (float) ((old1.y - basePoint.y) * Math.cos(radians) - (old1.x - basePoint.x) * Math.sin(radians))
                + basePoint.y;
        new2.x = (float) ((old2.x - basePoint.x) * Math.cos(radians) + (old2.y - basePoint.y) * Math.sin(radians))
                + basePoint.x;
        new2.y = (float) ((old2.y - basePoint.y) * Math.cos(radians) - (old2.x - basePoint.x) * Math.sin(radians))
                + basePoint.y;
        new3.x = (float) ((old3.x - basePoint.x) * Math.cos(radians) + (old3.y - basePoint.y) * Math.sin(radians))
                + basePoint.x;
        new3.y = (float) ((old3.y - basePoint.y) * Math.cos(radians) - (old3.x - basePoint.x) * Math.sin(radians))
                + basePoint.y;
        new4.x = (float) ((old4.x - basePoint.x) * Math.cos(radians) + (old4.y - basePoint.y) * Math.sin(radians))
                + basePoint.x;
        new4.y = (float) ((old4.y - basePoint.y) * Math.cos(radians) - (old4.x - basePoint.x) * Math.sin(radians))
                + basePoint.y;

        // 根据4个点，重新计算宽高
        float maxX = Math.max(Math.max(new1.x, new2.x), Math.max(new3.x, new4.x));
        float minX = Math.min(Math.min(new1.x, new2.x), Math.min(new3.x, new4.x));
        float maxY = Math.max(Math.max(new1.y, new2.y), Math.max(new3.y, new4.y));
        float minY = Math.min(Math.min(new1.y, new2.y), Math.min(new3.y, new4.y));

        offsetXAfterRotate = -minX;
        offsetYAfterRotate = -minY;
        dwidth = maxX - minX;
        dheight = maxY - minY;
    }

    /**
     * 获取旋转角度
     * 
     * @return
     */
    public int getRotationDegree() {
        return rotationDegree;
    }

    /**
     * 设置旋转角度
     * 
     * @param rotationDegree
     */
    public void setRotationDegree(int rotationDegree) {
        this.rotationDegree = rotationDegree;
    }

}
