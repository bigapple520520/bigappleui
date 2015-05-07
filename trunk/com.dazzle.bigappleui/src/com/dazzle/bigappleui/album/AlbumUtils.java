/* 
 * @(#)AlbumUtils.java    Created on 2014-11-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album;

import android.app.Activity;
import android.content.Intent;

import com.dazzle.bigappleui.album.activity.BucketActivity;
import com.dazzle.bigappleui.album.core.AlbumConfig;

/**
 * 这个类可以启动相册Activity的工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-12 下午1:35:16 $
 */
public abstract class AlbumUtils {

    /**
     * 启动相册，多选操作，限制选择张数
     * 
     * @param activity
     * @param limitCount
     *            限制张数
     * @param requestCode
     */
    public static void gotoAlbumForMulti(Activity activity, int limitCount, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, BucketActivity.class);
        intent.putExtra(AlbumConfig.PARAM_LIMIT_COUNT, limitCount);
        intent.putExtra(AlbumConfig.PARAM_IF_MULTIPLE_CHOICE, true);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动相册，多选操作，可以无限制的选择张数
     * 
     * @param activity
     * @param requestCode
     *            返回标识
     */
    public static void gotoAlbumForMulti(Activity activity, int requestCode) {
        gotoAlbumForMulti(activity, -1, requestCode);
    }

    /**
     * 启动相册，单选操作
     * 
     * @param activity
     * @param requestCode
     *            返回标识
     */
    public static void gotoAlbumForSingle(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, BucketActivity.class);
        intent.putExtra(AlbumConfig.PARAM_LIMIT_COUNT, -1);
        intent.putExtra(AlbumConfig.PARAM_IF_MULTIPLE_CHOICE, false);
        activity.startActivityForResult(intent, requestCode);
    }

}
