/* 
 * @(#)Logger.java    Created on 2014-12-16
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils;

import android.util.Log;

/**
 * 这个bigappleui的日志接口
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-16 上午9:55:16 $
 */
public abstract class LogUtils {

    /**
     * d级别日志
     * 
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    /**
     * w级别日志
     * 
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    /**
     * e级别日志
     * 
     * @param tag
     * @param msg
     * @param e
     */
    public static void e(String tag, String msg, Throwable e) {
        Log.e(tag, msg, e);
    }

    /**
     * e级别日志
     * 
     * @param tag
     * @param e
     */
    public static void e(String tag, Throwable e) {
        Log.e(tag, e.getMessage(), e);
    }

}
