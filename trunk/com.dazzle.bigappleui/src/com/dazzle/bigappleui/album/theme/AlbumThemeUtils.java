/* 
 * @(#)FileExplorerThemeUtils.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.theme;

import android.graphics.drawable.Drawable;

import com.dazzle.bigappleui.album.core.AlbumDrawableHelper;

/**
 * 文件选择器的主题工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 下午5:16:31 $
 */
public abstract class AlbumThemeUtils {

    /**
     * 设置主题
     * 
     * @param theme
     */
    public static void setTheme(AlbumTheme theme) {
        if (null == theme) {
            return;
        }

        // 标题栏部分配置
        int titleBgColor = theme.titleBgColor();
        if (-1 != titleBgColor) {
            AlbumDrawableHelper.setTitleBgColor(titleBgColor);
        }

        int titleTextColor = theme.titleTextColor();
        if (-1 != titleTextColor) {
            AlbumDrawableHelper.setTitleTextColor(titleTextColor);
        }

        // 选中资源文件
        Drawable selectedDrawable = theme.selectedDrawable();
        if (null != selectedDrawable) {
            AlbumDrawableHelper.setSelectedDrawable(selectedDrawable);
        }
    }

}
