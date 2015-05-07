/* 
 * @(#)BlueFileExplorerTheme.java    Created on 2014-12-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.theme.custom;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.dazzle.bigappleui.album.theme.DefaultAlbumTheme;
import com.dazzle.bigappleui.utils.ui.ColorUtils;
import com.dazzle.bigappleui.utils.ui.drawable.HookDrawable;

/**
 * 蓝色主题
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-12 上午11:23:26 $
 */
public class BlueAlbumTheme extends DefaultAlbumTheme {
    @Override
    public int titleBgColor() {
        return ColorUtils.getColor("#7EC0EE");
    }

    @Override
    public int titleTextColor() {
        return Color.WHITE;
    }

    @Override
    public Drawable selectedDrawable() {
        HookDrawable hookDrawable = new HookDrawable();
        hookDrawable.setPaintColor(ColorUtils.getColor("#7EC0EE"));
        return hookDrawable;
    }

}
