/* 
 * @(#)ColorUtils.java    Created on 2014-11-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils;

import android.graphics.Color;

/**
 * 颜色工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-12 下午1:10:44 $
 */
public abstract class ColorUtils {
    public static final String TOP_BTN_COLOR_NORMAL = "#00000000";// 全透明
    public static final String TOP_BTN_COLOR_PRESSED = "#17000000";// 半透明

    /**
     * 根据16进制串获取颜色值
     * 
     * @param colorStr
     * @return
     */
    public static int getColor(String colorStr) {
        return Color.parseColor(colorStr);
    }

}
