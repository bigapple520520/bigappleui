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
 * 启动相册工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-12 下午1:35:16 $
 */
public abstract class AlbumUtils {

    /**
     * 多选，可限制选择张数
     * 
     * @param activity
     * @param limitCount
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
     * 多选，不限制选择张数
     * 
     * @param activity
     * @param requestCode
     */
    public static void gotoAlbumForMulti(Activity activity, int requestCode) {
        gotoAlbumForMulti(activity, -1, requestCode);
    }

    /**
     * 单选
     * 
     * @param activity
     * @param requestCode
     */
    public static void gotoAlbumForSingle(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, BucketActivity.class);
        intent.putExtra(AlbumConfig.PARAM_LIMIT_COUNT, -1);
        intent.putExtra(AlbumConfig.PARAM_IF_MULTIPLE_CHOICE, false);
        activity.startActivityForResult(intent, requestCode);
    }

}
