/* 
 * @(#)FolderDrawable.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils.ui.drawable.fileicon;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.dazzle.bigappleui.utils.ui.ColorUtils;
import com.dazzle.bigappleui.utils.ui.drawable.BaseDrawable;

/**
 * 一个文件夹图标
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 上午10:24:59 $
 */
public class FolderDrawable extends BaseDrawable {

    /** 画笔 */
    private Paint paint;

    private Path path1;
    private Path path2;

    // 文件夹图片的颜色定义
    private int folderBodyColor = ColorUtils.getColor("#EEAD0E");
    private int folderHeadColor = ColorUtils.getColor("#EE9A00");

    public FolderDrawable() {
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
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(folderBodyColor);
        canvas.drawPath(path1, paint);
        paint.setColor(folderHeadColor);
        canvas.drawPath(path2, paint);
    }

    private Path getPath1() {
        Path path = new Path();
        path.moveTo(getXByScale(3), getYByScale(14));
        path.lineTo(getXByScale(38), getYByScale(14));
        path.lineTo(getXByScale(52), getYByScale(25));
        path.lineTo(getXByScale(97), getYByScale(25));
        path.lineTo(getXByScale(97), getYByScale(90));
        path.lineTo(getXByScale(3), getYByScale(90));
        path.close();
        return path;
    }

    private Path getPath2() {
        Path path = new Path();
        path.moveTo(getXByScale(44), getYByScale(19));
        path.lineTo(getXByScale(90), getYByScale(19));
        path.lineTo(getXByScale(97), getYByScale(25));
        path.lineTo(getXByScale(52), getYByScale(25));
        path.close();
        return path;
    }

    public int getFolderBodyColor() {
        return folderBodyColor;
    }

    public void setFolderBodyColor(int folderBodyColor) {
        this.folderBodyColor = folderBodyColor;
    }

    public int getFolderHeadColor() {
        return folderHeadColor;
    }

    public void setFolderHeadColor(int folderHeadColor) {
        this.folderHeadColor = folderHeadColor;
    }

}
