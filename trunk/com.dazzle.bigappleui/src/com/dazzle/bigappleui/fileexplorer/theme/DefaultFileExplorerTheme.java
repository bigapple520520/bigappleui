/* 
 * @(#)DefalutFileExplorerTheme.java    Created on 2014-12-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.fileexplorer.theme;

import java.util.Map;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.dazzle.bigappleui.utils.ui.drawable.checkbox.CheckBoxNormalDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.checkbox.CheckBoxSelectedDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.fileicon.DefaultFileDrawable;
import com.dazzle.bigappleui.utils.ui.drawable.fileicon.FolderDrawable;

/**
 * 文件选择器的默认实现
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 下午5:28:08 $
 */
public class DefaultFileExplorerTheme implements FileExplorerTheme {

    @Override
    public Drawable defaultFolderIcon() {
        return new FolderDrawable();
    }

    @Override
    public Drawable defaultFileIcon() {
        return new DefaultFileDrawable();
    }

    @Override
    public Map<String, Drawable> fileIconMap() {
        return null;
    }

    @Override
    public Drawable checkBoxNormalIcon() {
        return new CheckBoxNormalDrawable();
    }

    @Override
    public Drawable checkBoxSelectedIcon() {
        return new CheckBoxSelectedDrawable();
    }

    @Override
    public int titleBgColor() {
        return Color.parseColor("#414141");
    }

    @Override
    public int titleTextColor() {
        return Color.WHITE;
    }

}
