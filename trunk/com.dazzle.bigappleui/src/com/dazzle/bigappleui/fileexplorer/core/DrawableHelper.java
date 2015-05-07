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

package com.dazzle.bigappleui.fileexplorer.core;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.dazzle.bigappleui.utils.ui.drawable.checkbox.CheckBoxNormalDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.checkbox.CheckBoxSelectedDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.fileicon.DefaultFileDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.fileicon.FolderDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.fileicon.TxtFileDrawable;

/**
 * 文件对应图标关系帮助类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 下午4:48:42 $
 */
public abstract class DrawableHelper {
    /** 默认文件图标 */
    private static Drawable defalutFileIcon = new DefaultFileDrawable();
    /** 默认文件夹图标 */
    private static Drawable defalutFolderIcon = new FolderDrawable();

    /** 文件图标对应关系 */
    private static final HashMap<String, Drawable> fileExt2IconMap = new HashMap<String, Drawable>();
    private static final HashMap<String, Drawable> systemFileExt2IconMap = new HashMap<String, Drawable>();// 系统自带
    static {
        systemFileExt2IconMap.put("txt", new TxtFileDrawable());
        fileExt2IconMap.putAll(systemFileExt2IconMap);
    }

    /** 复选框未选中状态 */
    private static Drawable checkBoxNormalIcon = new CheckBoxNormalDrawable();
    /** 复选框选中状态 */
    private static Drawable checkBoxSelectedIcon = new CheckBoxSelectedDrawable();

    /** 头部的一些样式颜色 */
    private static int titleBgColor = Color.parseColor("#414141");
    private static int titleTextColor = Color.WHITE;

    /**
     * 添加文件图标对应关系
     * 
     * @param ext
     * @param icon
     */
    public static void addExtIcon(String ext, Drawable icon) {
        fileExt2IconMap.put(ext, icon);
    }

    /**
     * 批量添加
     * 
     * @param map
     */
    public static void addAllExtIcon(Map<String, Drawable> map) {
        fileExt2IconMap.clear();
        fileExt2IconMap.putAll(systemFileExt2IconMap);
        fileExt2IconMap.putAll(map);
    }

    /**
     * 获取指定后缀的文件图标
     * 
     * @param ext
     */
    public static Drawable getIconByExt(String ext) {
        if (TextUtils.isEmpty(ext)) {
            return defalutFileIcon;
        }

        Drawable icon = fileExt2IconMap.get(ext);
        if (null == icon) {
            icon = defalutFileIcon;
        }

        return icon;
    }

    public static Drawable getDefalutFileIcon() {
        return defalutFileIcon;
    }

    public static void setDefalutFileIcon(Drawable defalutFileIcon) {
        DrawableHelper.defalutFileIcon = defalutFileIcon;
    }

    public static Drawable getDefalutFolderIcon() {
        return defalutFolderIcon;
    }

    public static void setDefalutFolderIcon(Drawable defalutFolderIcon) {
        DrawableHelper.defalutFolderIcon = defalutFolderIcon;
    }

    public static Drawable getCheckBoxNormalIcon() {
        return checkBoxNormalIcon;
    }

    public static void setCheckBoxNormalIcon(Drawable checkBoxNormalIcon) {
        DrawableHelper.checkBoxNormalIcon = checkBoxNormalIcon;
    }

    public static Drawable getCheckBoxSelectedIcon() {
        return checkBoxSelectedIcon;
    }

    public static void setCheckBoxSelectedIcon(Drawable checkBoxSelectedIcon) {
        DrawableHelper.checkBoxSelectedIcon = checkBoxSelectedIcon;
    }

    public static HashMap<String, Drawable> getFileext2iconmap() {
        return fileExt2IconMap;
    }

    public static int getTitleTextColor() {
        return titleTextColor;
    }

    public static void setTitleTextColor(int titleTextColor) {
        DrawableHelper.titleTextColor = titleTextColor;
    }

    public static int getTitleBgColor() {
        return titleBgColor;
    }

    public static void setTitleBgColor(int titleBgColor) {
        DrawableHelper.titleBgColor = titleBgColor;
    }

}
