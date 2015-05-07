/* 
 * @(#)ImageLoader.java    Created on 2014-11-7
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.photoview.app.core;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.winupon.andframe.bigapple.bitmap.AnBitmapUtils;
import com.winupon.andframe.bigapple.bitmap.BitmapDisplayConfig;
import com.winupon.andframe.bigapple.bitmap.local.LocalImageLoader;
import com.winupon.andframe.bigapple.utils.Validators;

/**
 * 图片加载器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 上午11:46:00 $
 */
public abstract class ImageLoader {
    private static AnBitmapUtils anBitmapUtils;
    private static LocalImageLoader localImageLoader;

    /**
     * 初始化
     * 
     * @param context
     */
    public synchronized static void init(Context context) {
        if (null == anBitmapUtils) {
            if (context instanceof Activity) {
                context = ((Activity) context).getApplication();
            }

            anBitmapUtils = new AnBitmapUtils(context);
            anBitmapUtils.getGlobalConfig().setMemoryCacheSize(1024 * 1024);

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
    public static void display(ImageView imageView, String url, BitmapDisplayConfig config) {
        if (null == imageView || Validators.isEmpty(url)) {
            return;
        }

        if (url.startsWith("/")) {
            // 加载网络的
            localImageLoader.display(imageView, url, config);
        }
        else {
            // 加载本地的
            anBitmapUtils.display(imageView, url, config);
        }
    }

}
