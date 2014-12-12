/* 
 * @(#)ImageLoader.java    Created on 2014-11-7
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.core;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.winupon.andframe.bigapple.bitmap.BitmapDisplayConfig;
import com.winupon.andframe.bigapple.bitmap.callback.ImageLoadCallBack;
import com.winupon.andframe.bigapple.bitmap.local.LocalImageLoader;
import com.winupon.andframe.bigapple.utils.Validators;

/**
 * 图片加载器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 上午11:46:00 $
 */
public abstract class ImageLoader {
    private static LocalImageLoader localImageLoader;

    /**
     * 初始化
     * 
     * @param context
     */
    public synchronized static void init(Context context) {
        if (null == localImageLoader) {
            if (context instanceof Activity) {
                context = ((Activity) context).getApplication();
            }

            localImageLoader = new LocalImageLoader(context);
            localImageLoader.getLocalImageLoaderConfig().setMemoryCacheSize(1024 * 1024);
        }
    }

    /**
     * 显示图片
     * 
     * @param imageView
     * @param url
     */
    public static void display(Context context, ImageView imageView, String url) {
        if (null == imageView || Validators.isEmpty(url) || null == localImageLoader) {
            return;
        }

        localImageLoader.display(imageView, url, getConfig(context, url));
    }

    /**
     * 显示图片
     * 
     * @param context
     * @param imageView
     * @param thumbPath
     * @param sourcePath
     */
    public static void display(final Context context, ImageView imageView, String thumbPath, String sourcePath) {
        if (Validators.isEmpty(thumbPath)) {
            display(context, imageView, sourcePath);
        }
        else {
            File file = new File(thumbPath);
            if (file.exists()) {
                display(context, imageView, thumbPath);
            }
            else {
                display(context, imageView, sourcePath);
            }
        }
    }

    private static BitmapDisplayConfig getConfig(final Context context, final String filePath) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setShowOriginal(false);
        config.setBitmapMaxHeight(AlbumSettings.instance().getThumbnailQualityHeight());
        config.setBitmapMaxWidth(AlbumSettings.instance().getThumbnailQualityWidth());
        config.setImageLoadCallBack(new ImageLoadCallBack() {
            @Override
            public void loadFailed(ImageView imageView, BitmapDisplayConfig config) {
                imageView.setImageBitmap(config.getLoadFailedBitmap());
            }

            @Override
            public void loadCompleted(ImageView imageView, Bitmap bitmap, BitmapDisplayConfig config) {
                int degree = ImageUtils.getBitmapDegree(context, Uri.parse(filePath));// 角度调整处理
                imageView.setImageBitmap(ImageUtils.rotateBitMap(bitmap, degree));
            }
        });
        return config;
    }

}
