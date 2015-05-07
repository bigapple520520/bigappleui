/* 
 * @(#)ViewImageUtils.java    Created on 2014-11-5
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.photoview.app;

import android.content.Context;
import android.content.Intent;

import com.dazzle.bigappleui.view.photoview.app.handler.ViewImageBaseHandler;

public abstract class ViewImageUtils {

    /**
     * 跳去查看大图
     * 
     * @param context
     * @param urls
     * @param position
     * @param loadType
     * @param datas
     * @param clazz
     */
    public static void gotoViewImageActivity(Context context, String[] urls, int position, String loadType,
            Object[] datas, Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        intent.putExtra(ViewImageActivity.PARAM_IMAGE_URLS, urls);
        intent.putExtra(ViewImageActivity.PARAM_IMAGE_INDEX, position);
        intent.putExtra(ViewImageActivity.PARAM_LOADTYPE, loadType);
        intent.putExtra(ViewImageActivity.PARAM_DATAS, datas);
        context.startActivity(intent);
    }

    /**
     * 显示resid中的资源
     * 
     * @param context
     * @param resids
     * @param position
     * @param datas
     */
    public static void gotoViewImageActivityForResids(Context context, int[] resids, int position, Object[] datas) {
        String[] urlStrs = new String[resids.length];
        for (int i = 0, n = resids.length; i < n; i++) {
            urlStrs[i] = String.valueOf(resids[i]);
        }

        gotoViewImageActivity(context, urlStrs, position, ViewImageBaseHandler.LOADTYPE_BY_RESIID, datas,
                ViewImageActivity.class);
    }

    /**
     * 加载本地或者网络地址
     * 
     * @param context
     * @param urls
     * @param position
     * @param datas
     */
    public static void gotoViewImageActivityForUrls(Context context, String[] urls, int position, Object[] datas) {
        gotoViewImageActivity(context, urls, position, ViewImageBaseHandler.LOADTYPE_BY_URL, datas,
                ViewImageActivity.class);
    }

}
