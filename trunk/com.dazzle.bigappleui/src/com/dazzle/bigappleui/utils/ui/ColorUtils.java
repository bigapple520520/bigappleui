/* 
 * @(#)ColorUtils.java    Created on 2014-11-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils.ui;

import android.graphics.Color;

/**
 * 颜色工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-12 下午1:10:44 $
 */
public abstract class ColorUtils {
    public static final int TRANSPARENT = Color.parseColor("#00000000");;// 全透明
    public static final int TRANSLUCENT = Color.parseColor("#17000000");// 半透明

    public static final int COLOR_EBEBEB = Color.parseColor("#EBEBEB");
    public static final int COLOR_D4D4D4 = Color.parseColor("#D4D4D4");

    public static final int COLOR_00000000 = Color.parseColor("#00000000");

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
