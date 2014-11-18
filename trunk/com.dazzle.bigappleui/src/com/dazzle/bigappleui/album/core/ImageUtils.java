/* 
 * @(#)ImageUtils.java    Created on 2014-11-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.core;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore;
import android.util.Log;

import com.winupon.andframe.bigapple.utils.Validators;

/**
 * 图片处理工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-11 下午2:54:51 $
 */
public abstract class ImageUtils {
    public static final String TAG = "album.core.ImageUtils";

    /**
     * 获取图片存储角度
     * 
     * @param context
     * @param path
     * @return
     */
    public static int getBitmapDegree(Context context, Uri path) {
        // 获取实际图片地址
        String imgPath = "";
        if (path.toString().indexOf("content://") != -1) {
            try {
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor actualimagecursor = ((Activity) context).managedQuery(path, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                imgPath = actualimagecursor.getString(actual_image_column_index);
                if (VERSION.SDK_INT < 14) {
                    actualimagecursor.close();
                }
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        else {
            imgPath = path.getPath();
        }

        int degree = 0;
        if (Validators.isEmpty(imgPath)) {
            return degree;
        }

        Log.d(TAG, "图片地址：" + imgPath);

        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(imgPath);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return degree;
    }

    /**
     * 图片角度旋转
     * 
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap rotateBitMap(Bitmap bitmap, int degree) {
        Bitmap nowBp = null;
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(degree);
        try {
            nowBp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        }

        if (null == nowBp) {
            // 异常了，会进入这里
            nowBp = bitmap;
        }
        if (bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return nowBp;
    }

}
