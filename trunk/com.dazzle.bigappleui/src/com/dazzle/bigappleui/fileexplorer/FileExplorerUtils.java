/* 
 * @(#)AlbumUtils.java    Created on 2014-11-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.fileexplorer;

import android.app.Activity;
import android.content.Intent;

import com.dazzle.bigappleui.fileexplorer.widget.FileExplorerActivity;

/**
 * 这个类可以启动相册Activity的工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-12 下午1:35:16 $
 */
public abstract class FileExplorerUtils {

    /**
     * 启动文件选择器，多选可限制张数
     * 
     * @param activity
     * @param limitCount
     *            张数限制
     * @param requestCode
     *            返回标识
     */
    public static void gotoFileExplorerForForMulti(Activity activity, int limitCount, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, FileExplorerActivity.class);
        intent.putExtra(FileExplorerActivity.PARAM_LIMIT_COUNT, limitCount);
        intent.putExtra(FileExplorerActivity.PARAM_IF_MULTIPLE_CHOICE, true);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动文件选择器，多选无张数限制
     * 
     * @param activity
     * @param requestCode
     *            返回标识
     */
    public static void gotoFileExplorerForForMulti(Activity activity, int requestCode) {
        gotoFileExplorerForForMulti(activity, -1, requestCode);
    }

    /**
     * 启动文件选择器，单选
     * 
     * @param activity
     * @param requestCode
     *            返回标识
     */
    public static void gotoFileExplorerForSingle(Activity activity, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, FileExplorerActivity.class);
        intent.putExtra(FileExplorerActivity.PARAM_LIMIT_COUNT, -1);
        intent.putExtra(FileExplorerActivity.PARAM_IF_MULTIPLE_CHOICE, false);
        activity.startActivityForResult(intent, requestCode);
    }

}
