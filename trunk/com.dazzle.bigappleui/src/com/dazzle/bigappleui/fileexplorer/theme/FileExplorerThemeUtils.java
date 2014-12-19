/* 
 * @(#)FileExplorerThemeUtils.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.fileexplorer.theme;

import java.util.Map;

import android.graphics.drawable.Drawable;

import com.dazzle.bigappleui.fileexplorer.core.DrawableHelper;

/**
 * 文件选择器的主题工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 下午5:16:31 $
 */
public abstract class FileExplorerThemeUtils {

    /**
     * 设置主题
     * 
     * @param theme
     */
    public static void setTheme(FileExplorerTheme theme) {
        if (null == theme) {
            return;
        }

        // 文件夹图标
        Drawable folderIcon = theme.defaultFolderIcon();
        if (null != folderIcon) {
            DrawableHelper.setDefalutFolderIcon(theme.defaultFolderIcon());
        }

        // 默认文件图标
        Drawable fileIcon = theme.defaultFileIcon();
        if (null != fileIcon) {
            DrawableHelper.setDefalutFileIcon(theme.defaultFileIcon());
        }

        // 自定义后缀文件图标
        Map<String, Drawable> fileIconMap = theme.fileIconMap();
        if (null != fileIconMap) {
            DrawableHelper.addAllExtIcon(fileIconMap);
        }

        // 复选框的图标
        Drawable checkBoxNormal = theme.checkBoxNormalIcon();
        if (null != checkBoxNormal) {
            DrawableHelper.setCheckBoxNormalIcon(checkBoxNormal);
        }
        Drawable checkBoxSelected = theme.checkBoxSelectedIcon();
        if (null != checkBoxSelected) {
            DrawableHelper.setCheckBoxSelectedIcon(checkBoxSelected);
        }

        // 标题栏部分：背景颜色、文字颜色
        int titleBgColor = theme.titleBgColor();
        if (-1 != titleBgColor) {
            DrawableHelper.setTitleBgColor(titleBgColor);
        }
        int titleTextColor = theme.titleTextColor();
        if (-1 != titleTextColor) {
            DrawableHelper.setTitleTextColor(titleTextColor);
        }
    }

}
