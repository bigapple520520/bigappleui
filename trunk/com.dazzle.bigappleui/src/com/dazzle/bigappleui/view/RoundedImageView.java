/* 
 * @(#)RoundedImageView.java    Created on 2013-8-5
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 复写ImageView，使图片可以圆角显示
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-8-5 下午5:19:35 $
 */
public class RoundedImageView extends ImageView {
    private final Context context;

    public RoundedImageView(Context context) {
        super(context);
        this.context = context;
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    // ////////////////////////////////////////设置圆角图片///////////////////////////////////////////////////////////
    public void setImageDrawable(Drawable drawable, float roundPx) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

        RoundedCornerBitmapProcessor roundedCornerBitmapProcessor = new RoundedCornerBitmapProcessor();
        Bitmap roundedBitmap = roundedCornerBitmapProcessor.getRoundedCornerBitmap(bitmapDrawable.getBitmap(), roundPx);

        super.setImageDrawable(new BitmapDrawable(context.getResources(), roundedBitmap));
    }

    public void setImageBitmap(Bitmap bitmap, float roundPx) {
        setImageDrawable(new BitmapDrawable(context.getResources(), bitmap), roundPx);
    }

    public void setImageResource(int resId, float roundPx) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        setImageDrawable(new BitmapDrawable(context.getResources(), bitmap), roundPx);
    }

    // ////////////////////////////////////////设置圆角背景///////////////////////////////////////////////////////////
    public void setBackgroundDrawable(Drawable drawable, float roundPx) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

        RoundedCornerBitmapProcessor roundedCornerBitmapProcessor = new RoundedCornerBitmapProcessor();
        Bitmap roundedBitmap = roundedCornerBitmapProcessor.getRoundedCornerBitmap(bitmapDrawable.getBitmap(), roundPx);

        super.setBackgroundDrawable(new BitmapDrawable(context.getResources(), roundedBitmap));
    }

    public void setBackgroundDrawable(Bitmap bitmap, float roundPx) {
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap), roundPx);
    }

    public void setBackgroundDrawable(int resId, float roundPx) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap), roundPx);
    }

    /**
     * 图片处理成圆角工具
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2013-10-15 下午6:57:42 $
     */
    public class RoundedCornerBitmapProcessor {
        /**
         * 把图片装成圆角
         * 
         * @param bitmap
         * @param roundPx
         * @return
         */
        public Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }
    }

}
