/* 
 * @(#)FileExplorerTheme.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.theme;

import android.graphics.drawable.Drawable;

/**
 * 主题类，所有自定义主题继承于他就可以了
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 下午5:17:20 $
 */
public abstract interface AlbumTheme {

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

    /**
     * 多选时选中的资源文件
     * 
     * @return
     */
    Drawable selectedDrawable();

}
