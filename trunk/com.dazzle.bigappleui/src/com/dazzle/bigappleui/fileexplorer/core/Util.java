/* 
 * @(#)Util.java    Created on 2014-12-9
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.fileexplorer.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.dazzle.bigappleui.fileexplorer.entity.FileInfo;

/**
 * 文件选择器工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-9 上午10:03:50 $
 */
public class Util {
    private static final String LOG_TAG = "Util";
    private static String ANDROID_SECURE = "/mnt/sdcard/.android_secure";

    // does not include sd card folder
    private static String[] SysFileDirs = new String[] { "miren_browser/imagecaches" };

    /**
     * 判断SD是否准备好
     * 
     * @return
     */
    public static boolean isSDCardReady() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     * 
     * @return
     */
    public static String getSdDirectory() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 判断path1是否包含着path2
     * 
     * @param path1
     * @param path2
     * @return
     */
    public static boolean containsPath(String path1, String path2) {
        String path = path2;
        while (path != null) {
            if (path.equalsIgnoreCase(path1)) {
                return true;
            }

            if (path.equals("/")) {
                break;
            }
            path = new File(path).getParent();
        }

        return false;
    }

    public static String makePath(String path1, String path2) {
        if (path1.endsWith(File.separator)) {
            return path1 + path2;
        }

        return path1 + File.separator + path2;
    }

    /**
     * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过 appInfo.publicSourceDir = apkPath;来修正这个问题，详情参见:
     * http://code.google.com/p/android/issues/detail?id=9151
     * 
     * @param context
     * @param apkPath
     * @return
     */
    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            }
            catch (OutOfMemoryError e) {
                Log.e(LOG_TAG, e.toString());
            }
        }
        return null;
    }

    /**
     * 获取文件名称
     * 
     * @param filename
     * @return
     */
    public static String getNameFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(0, dotPosition);
        }
        return "";
    }

    /**
     * 判断文件能否显示
     * 
     * @param path
     * @return
     */
    public static boolean shouldShowFile(String path) {
        return shouldShowFile(new File(path));
    }

    /**
     * 判断文件能否显示
     * 
     * @param file
     * @return
     */
    public static boolean shouldShowFile(File file) {
        boolean show = FileExplorerSettings.instance().isShowDotAndHiddenFiles();
        if (show) {
            return true;
        }

        if (file.isHidden()) {
            return false;
        }

        if (file.getName().startsWith(".")) {
            return false;
        }

        String sdFolder = getSdDirectory();
        for (String s : SysFileDirs) {
            if (file.getPath().startsWith(makePath(sdFolder, s))) {
                return false;
            }
        }

        return true;
    }

    // comma separated number
    public static String convertNumber(long number) {
        return String.format("%,d", number);
    }

    // storage, G M K B
    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        }
        else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        }
        else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        }
        else {
            return String.format("%d B", size);
        }
    }

    /**
     * 判断是否是正常文件
     * 
     * @param fullName
     * @return
     */
    public static boolean isNormalFile(String fullName) {
        return !fullName.equals(ANDROID_SECURE);
    }

    /**
     * 获取文件的后缀名
     * 
     * @param filename
     * @return
     */
    public static String getExtFromFilename(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return "";
        }

        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition <= 0) {
            return "";
        }

        return filename.substring(dotPosition + 1);
    }

    /**
     * 加载指定文件夹下的所有文件
     * 
     * @param path
     * @param fileSortHelper
     * @param fileInfoList
     * @return
     */
    public static boolean reloadFileList(Context context, String path, FileSortHelper fileSortHelper,
            List<FileInfo> fileInfoList) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return false;
        }
        fileInfoList.clear();

        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return true;
        }

        for (File child : listFiles) {
            String absolutePath = child.getAbsolutePath();
            if (Util.isNormalFile(absolutePath) && Util.shouldShowFile(absolutePath)) {
                FileInfo fileInfo = Util.getFileInfo(context, child, FileExplorerSettings.instance()
                        .isShowDotAndHiddenFiles());
                if (null != fileInfo) {
                    fileInfoList.add(fileInfo);
                }
            }
        }

        Collections.sort(fileInfoList, fileSortHelper.getComparator());
        return true;
    }

    /**
     * 获取文件的基本信息
     * 
     * @param file
     * @param showHidden
     * @return
     */
    public static FileInfo getFileInfo(Context context, File file, boolean showHidden) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.canRead = file.canRead();
        fileInfo.canWrite = file.canWrite();
        fileInfo.isHidden = file.isHidden();
        fileInfo.fileName = file.getName();
        fileInfo.fileNameExt = Util.getExtFromFilename(file.getName());
        fileInfo.modifiedDate = file.lastModified();
        fileInfo.isDir = file.isDirectory();
        fileInfo.filePath = file.getPath();

        if (fileInfo.isDir) {
            fileInfo.icon = DrawableHelper.getDefalutFolderIcon();
            int count = 0;

            File[] files = file.listFiles();

            // 文件不允许访问
            if (null == files) {
                return null;
            }

            for (File child : files) {
                if ((!child.isHidden() || showHidden) && Util.isNormalFile(child.getAbsolutePath())) {
                    count++;
                }
            }
            fileInfo.count = count;
        }
        else {
            fileInfo.icon = DrawableHelper.getIconByExt(fileInfo.fileNameExt);
            fileInfo.fileSize = file.length();
        }

        // 如果是APK，尝试获取引用图标
        if ("apk".equalsIgnoreCase(fileInfo.fileNameExt)) {
            Drawable apkIcon = getApkIcon(context, fileInfo.filePath);
            if (null != apkIcon) {
                fileInfo.icon = apkIcon;
            }
        }

        return fileInfo;
    }

    /**
     * 根据long值时间转换成有好时间
     * 
     * @param timeLong
     *            距离1970年的毫秒数
     * @return
     */
    public static String getDateByLong(long timeLong) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timeLong));
    }

    // //////////////////////////////////////获取SD卡基本信息//////////////////////////////////////////////////
    /**
     * SD基本信息
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-12-12 下午1:13:14 $
     */
    public static class SDCardInfo {
        public long total;
        public long free;
    }

    /**
     * 获取SD基本信息
     * 
     * @return
     */
    public static SDCardInfo getSDCardInfo() {
        String sDcString = android.os.Environment.getExternalStorageState();

        if (sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File pathFile = android.os.Environment.getExternalStorageDirectory();

            try {
                android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());

                // 获取SDCard上BLOCK总数
                long nTotalBlocks = statfs.getBlockCount();

                // 获取SDCard上每个block的SIZE
                long nBlocSize = statfs.getBlockSize();

                // 获取可供程序使用的Block的数量
                long nAvailaBlock = statfs.getAvailableBlocks();

                // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
                long nFreeBlock = statfs.getFreeBlocks();

                SDCardInfo info = new SDCardInfo();
                // 计算SDCard 总容量大小MB
                info.total = nTotalBlocks * nBlocSize;

                // 计算 SDCard 剩余大小MB
                info.free = nAvailaBlock * nBlocSize;

                return info;
            }
            catch (IllegalArgumentException e) {
                Log.e(LOG_TAG, e.toString());
            }
        }

        return null;
    }

}
