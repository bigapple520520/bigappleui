/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dazzle.bigappleui.album.core;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.dazzle.bigappleui.utils.ui.drawable.HookDrawable;

/**
 * 文件对应图标关系帮助类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 下午4:48:42 $
 */
public abstract class AlbumDrawableHelper {
    /** 头部的一些样式颜色 */
    private static int titleBgColor = Color.parseColor("#414141");
    private static int titleTextColor = Color.WHITE;

    /** 选中的资源 */
    private static Drawable selectedDrawable = new HookDrawable();

    public static int getTitleTextColor() {
        return titleTextColor;
    }

    public static void setTitleTextColor(int titleTextColor) {
        AlbumDrawableHelper.titleTextColor = titleTextColor;
    }

    public static int getTitleBgColor() {
        return titleBgColor;
    }

    public static void setTitleBgColor(int titleBgColor) {
        AlbumDrawableHelper.titleBgColor = titleBgColor;
    }

    public static Drawable getSelectedDrawable() {
        return selectedDrawable;
    }

    public static void setSelectedDrawable(Drawable selectedDrawable) {
        AlbumDrawableHelper.selectedDrawable = selectedDrawable;
    }

}
