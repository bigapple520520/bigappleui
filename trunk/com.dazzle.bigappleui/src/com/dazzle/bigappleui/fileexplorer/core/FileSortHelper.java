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

import java.util.Comparator;
import java.util.HashMap;

import com.dazzle.bigappleui.fileexplorer.entity.FileInfo;

/**
 * 文件排列帮助工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-10 上午11:00:32 $
 */
public class FileSortHelper {

    /**
     * 排列方式枚举
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-12-10 上午11:00:59 $
     */
    public enum SortMethod {
        name, size, date, type
    }

    /** 按某种方式排序，默认按时间 */
    private SortMethod useSortMethod = SortMethod.date;
    /** 表示文件排前面还是文件夹排前面，true表示文件排前面false表示文件夹排前面 */
    private boolean isFileFirst;

    /** 多种排序方式Map */
    private HashMap<SortMethod, Comparator<FileInfo>> sortMethod2ComparatorMap = new HashMap<SortMethod, Comparator<FileInfo>>();

    /**
     * 构造方法
     */
    public FileSortHelper() {
        sortMethod2ComparatorMap.put(SortMethod.name, cmpName);
        sortMethod2ComparatorMap.put(SortMethod.size, cmpSize);
        sortMethod2ComparatorMap.put(SortMethod.date, cmpDate);
        sortMethod2ComparatorMap.put(SortMethod.type, cmpType);
    }

    public SortMethod getUseSortMethod() {
        return useSortMethod;
    }

    public void setUseSortMethod(SortMethod useSortMethod) {
        this.useSortMethod = useSortMethod;
    }

    public boolean isFileFirst() {
        return isFileFirst;
    }

    public void setFileFirst(boolean isFileFirst) {
        this.isFileFirst = isFileFirst;
    }

    public Comparator<FileInfo> getComparator() {
        return sortMethod2ComparatorMap.get(useSortMethod);
    }

    /**
     * 文件比较器的基类
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-12-10 上午11:14:08 $
     */
    private abstract class FileComparator implements Comparator<FileInfo> {
        @Override
        public int compare(FileInfo fileInfo1, FileInfo fileInfo2) {
            if (fileInfo1.isDir == fileInfo2.isDir) {
                return doCompare(fileInfo1, fileInfo2);
            }

            if (isFileFirst) {
                return (fileInfo1.isDir ? 1 : -1);
            }
            else {
                return fileInfo1.isDir ? -1 : 1;
            }
        }

        protected abstract int doCompare(FileInfo fileInfo1, FileInfo fileInfo2);
    }

    private Comparator<FileInfo> cmpName = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            return object1.fileName.compareToIgnoreCase(object2.fileName);
        }
    };

    private Comparator<FileInfo> cmpSize = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            return longToCompareInt(object1.fileSize - object2.fileSize);
        }
    };

    private Comparator<FileInfo> cmpDate = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            return longToCompareInt(object2.modifiedDate - object1.modifiedDate);
        }
    };

    private Comparator<FileInfo> cmpType = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            int result = Util.getExtFromFilename(object1.fileName).compareToIgnoreCase(
                    Util.getExtFromFilename(object2.fileName));
            if (result != 0) {
                return result;
            }

            return Util.getNameFromFilename(object1.fileName).compareToIgnoreCase(
                    Util.getNameFromFilename(object2.fileName));
        }
    };

    private int longToCompareInt(long result) {
        return result > 0 ? 1 : (result < 0 ? -1 : 0);
    }

}
