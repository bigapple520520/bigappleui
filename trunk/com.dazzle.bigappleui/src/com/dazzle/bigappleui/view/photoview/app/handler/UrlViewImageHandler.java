/* 
 * @(#)PathViewImageHandler.java    Created on 2014-11-6
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.photoview.app.handler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dazzle.bigappleui.view.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.dazzle.bigappleui.view.photoview.app.core.ImageLoader;
import com.dazzle.bigappleui.view.photoview.app.viewholder.WraperFragmentView;
import com.winupon.andframe.bigapple.bitmap.BitmapDisplayConfig;
import com.winupon.andframe.bigapple.bitmap.callback.ImageLoadCallBack;

/**
 * 加载网络地址或者本地SD卡的地址
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-6 下午3:45:12 $
 */
public class UrlViewImageHandler extends ViewImageBaseHandler {

    @Override
    public void onHandler(String url, final WraperFragmentView wraperFragmentView, final Activity activity,
            Object[] datas) {
        try {
            ImageLoader.init(activity);

            wraperFragmentView.photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    activity.finish();
                }
            });

            // 显示大图
            wraperFragmentView.progressBar.setVisibility(View.VISIBLE);
            BitmapDisplayConfig config = new BitmapDisplayConfig();
            config.setImageLoadCallBack(new ImageLoadCallBack() {
                @Override
                public void loadFailed(ImageView arg0, BitmapDisplayConfig arg1) {
                }

                @Override
                public void loadCompleted(ImageView imageView, Bitmap bitmap, BitmapDisplayConfig config) {
                    imageView.setImageBitmap(bitmap);
                    wraperFragmentView.progressBar.setVisibility(View.GONE);
                }
            });
            ImageLoader.display(wraperFragmentView.photoView, url, config);
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public String getLoadType() {
        return ViewImageBaseHandler.LOADTYPE_BY_URL;
    }

}
