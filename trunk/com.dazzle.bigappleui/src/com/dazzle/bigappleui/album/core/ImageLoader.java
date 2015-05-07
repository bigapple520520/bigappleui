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
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.dazzle.bigappleui.utils.ui.ColorUtils;
import com.dazzle.bigappleui.view.imageview.RotationImageView;
import com.winupon.andframe.bigapple.bitmap.BitmapDisplayConfig;
import com.winupon.andframe.bigapple.bitmap.callback.ImageLoadCallBack;
import com.winupon.andframe.bigapple.bitmap.local.LocalImageLoader;

/**
 * 图片加载器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 上午11:46:00 $
 */
public abstract class ImageLoader {
    private static Bitmap defaultBitmap = Bitmap.createBitmap(new int[] { ColorUtils.COLOR_EBEBEB }, 1, 1,
            Config.ARGB_8888);
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
     * @param context
     * @param imageView
     * @param thumbPath
     * @param sourcePath
     */
    public static void display(final Context context, ImageView imageView, String thumbPath, String sourcePath) {
        if (null == context || null == imageView || null == localImageLoader) {
            return;
        }

        String showUrl = null;
        if (TextUtils.isEmpty(thumbPath)) {
            showUrl = sourcePath;
        }
        else {
            File file = new File(thumbPath);
            if (file.exists()) {
                showUrl = thumbPath;
            }
            else {
                showUrl = sourcePath;
            }
        }

        if (TextUtils.isEmpty(showUrl)) {
            return;
        }

        localImageLoader.display(imageView, showUrl, getConfig(context, showUrl, sourcePath));
    }

    private static BitmapDisplayConfig getConfig(final Context context, final String filePath, final String sourcePath) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setShowOriginal(false);
        config.setLoadingBitmap(defaultBitmap);
        config.setLoadFailedBitmap(defaultBitmap);
        config.setBitmapMaxHeight(AlbumSettings.instance().getThumbnailQualityHeight());
        config.setBitmapMaxWidth(AlbumSettings.instance().getThumbnailQualityWidth());
        config.setImageLoadCallBack(new ImageLoadCallBack() {
            @Override
            public void loadFailed(ImageView imageView, BitmapDisplayConfig config) {
                imageView.setImageBitmap(config.getLoadFailedBitmap());
            }

            @Override
            public void loadCompleted(ImageView imageView, Bitmap bitmap, BitmapDisplayConfig config) {
                if (imageView instanceof RotationImageView) {
                    RotationImageView rotationImageView = (RotationImageView) imageView;
                    rotationImageView.setRotationDegree(ImageUtils.getBitmapDegree(context, Uri.parse(sourcePath)));
                    rotationImageView.setImageBitmap(bitmap);
                }
                else {
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
        return config;
    }

}
