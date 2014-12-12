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

/**
 * 一些基本设置
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 下午4:00:40 $
 */
public class FileExplorerSettings {
    /** 是否显示系统很缓存图片，默认不显示 */
    private boolean showDotAndHiddenFiles;
    private static FileExplorerSettings instance;

    private FileExplorerSettings() {
    }

    /**
     * 获取单例
     * 
     * @return
     */
    public static FileExplorerSettings instance() {
        if (null == instance) {
            instance = new FileExplorerSettings();
        }
        return instance;
    }

    public boolean isShowDotAndHiddenFiles() {
        return showDotAndHiddenFiles;
    }

    public void setShowDotAndHiddenFiles(boolean showDotAndHiddenFiles) {
        this.showDotAndHiddenFiles = showDotAndHiddenFiles;
    }

}
