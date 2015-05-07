/* 
 * @(#)ViewImageBaseHandler.java    Created on 2014-11-5
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.photoview.app.handler;

import android.app.Activity;

import com.dazzle.bigappleui.view.photoview.app.viewholder.WraperFragmentView;

/**
 * 图片显示处理器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-5 下午6:57:37 $
 */
public abstract class ViewImageBaseHandler {
    protected final static String TAG = "ViewImageBaseHandler";

    public final static String LOADTYPE_BY_RESIID = "bigapple.loadtype.by.resid";
    public final static String LOADTYPE_BY_URL = "bigapple.loadtype.by.url";

    /**
     * 图片显示处理，自定义handler时，请复写onHandler方法，不要复写该方法
     * 
     * @param url
     * @param wraperFragmentView
     * @param activity
     * @param datas
     */
    public void handler(String url, WraperFragmentView wraperFragmentView, Activity activity, Object[] datas) {
        beforeHandler(url, wraperFragmentView, activity, datas);
        onHandler(url, wraperFragmentView, activity, datas);
        afterHandler(url, wraperFragmentView, activity, datas);
    }

    /**
     * 可以复写该方法来添加一些自定义的逻辑，在执行onHandler之前
     * 
     * @param url
     * @param wraperFragmentView
     * @param activity
     * @param datas
     */
    protected void beforeHandler(String url, WraperFragmentView wraperFragmentView, Activity activity, Object[] datas) {
    }

    /**
     * 可以复写该方法来添加一些自定义的逻辑，在执行onHandler之后
     * 
     * @param url
     * @param wraperFragmentView
     * @param activity
     * @param datas
     */
    protected void afterHandler(String url, WraperFragmentView wraperFragmentView, Activity activity, Object[] datas) {
    }

    /**
     * 子类复写该方法，实现图片加载逻辑
     * 
     * @param url
     * @param wraperFragmentView
     * @param activity
     * @param datas
     */
    public abstract void onHandler(String url, WraperFragmentView wraperFragmentView, Activity activity, Object[] datas);

    /**
     * 返回当前处理器的加载类型
     */
    public abstract String getLoadType();

}
