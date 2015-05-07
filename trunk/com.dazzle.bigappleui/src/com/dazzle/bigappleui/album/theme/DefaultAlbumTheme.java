/* 
 * @(#)DefalutFileExplorerTheme.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.theme;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.dazzle.bigappleui.utils.ui.drawable.HookDrawable;

/**
 * 文件选择器的默认实现
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 下午5:28:08 $
 */
public class DefaultAlbumTheme implements AlbumTheme {

    @Override
    public int titleBgColor() {
        return Color.parseColor("#414141");
    }

    @Override
    public int titleTextColor() {
        return Color.WHITE;
    }

    @Override
    public Drawable selectedDrawable() {
        HookDrawable hookDrawable = new HookDrawable();
        hookDrawable.setPaintColor(Color.RED);
        return hookDrawable;
    }
}
