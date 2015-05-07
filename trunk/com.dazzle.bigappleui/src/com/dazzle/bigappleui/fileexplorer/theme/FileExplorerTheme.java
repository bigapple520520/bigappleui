/* 
 * @(#)FileExplorerTheme.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.fileexplorer.theme;

import java.util.Map;

import android.graphics.drawable.Drawable;

/**
 * 主题类，所有自定义主题继承于他就可以了
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 下午5:17:20 $
 */
public abstract interface FileExplorerTheme {
    /**
     * 默认文件夹图标
     * 
     * @return
     */
    Drawable defaultFolderIcon();

    /**
     * 默认文件图标
     * 
     * @return
     */
    Drawable defaultFileIcon();

    /**
     * 不同文件图标集合
     * 
     * @return
     */
    Map<String, Drawable> fileIconMap();

    /**
     * 多选框未选中状态
     * 
     * @return
     */
    Drawable checkBoxNormalIcon();

    /**
     * 多选框选中状态
     * 
     * @return
     */
    Drawable checkBoxSelectedIcon();

    /**
     * 标题栏的背颜色
     * 
     * @return
     */
    int titleBgColor();

    /**
     * 标题栏的文字颜色
     * 
     * @return
     */
    int titleTextColor();

}
