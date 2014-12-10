/* 
 * @(#)Util.java    Created on 2014-12-9
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.fileexplorer.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    public static final String ROOT_DIR = "/";

    public static boolean isSDCardReady() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // if path1 contains path2
    public static boolean containsPath(String path1, String path2) {
        String path = path2;
        while (path != null) {
            if (path.equalsIgnoreCase(path1)) {
                return true;
            }

            if (path.equals(GlobalConsts.ROOT_PATH)) {
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

    public static String getSdDirectory() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static boolean isNormalFile(String fullName) {
        return !fullName.equals(ANDROID_SECURE);
    }

    public static FileInfo GetFileInfo(String filePath) {
        File lFile = new File(filePath);
        if (!lFile.exists()) {
            return null;
        }

        FileInfo lFileInfo = new FileInfo();
        lFileInfo.canRead = lFile.canRead();
        lFileInfo.canWrite = lFile.canWrite();
        lFileInfo.isHidden = lFile.isHidden();
        lFileInfo.fileName = Util.getNameFromFilepath(filePath);
        lFileInfo.modifiedDate = lFile.lastModified();
        lFileInfo.isDir = lFile.isDirectory();
        lFileInfo.filePath = filePath;
        lFileInfo.fileSize = lFile.length();
        return lFileInfo;
    }

    public static FileInfo GetFileInfo(File f, boolean showHidden) {
        FileInfo lFileInfo = new FileInfo();
        String filePath = f.getPath();
        File lFile = new File(filePath);
        lFileInfo.canRead = lFile.canRead();
        lFileInfo.canWrite = lFile.canWrite();
        lFileInfo.isHidden = lFile.isHidden();
        lFileInfo.fileName = f.getName();
        lFileInfo.modifiedDate = lFile.lastModified();
        lFileInfo.isDir = lFile.isDirectory();
        lFileInfo.filePath = filePath;
        if (lFileInfo.isDir) {
            int lCount = 0;

            File[] files = lFile.listFiles();

            // null means we cannot access this dir
            if (files == null) {
                return null;
            }

            for (File child : files) {
                if ((!child.isHidden() || showHidden) && Util.isNormalFile(child.getAbsolutePath())) {
                    lCount++;
                }
            }
            lFileInfo.count = lCount;

        }
        else {

            lFileInfo.fileSize = lFile.length();

        }
        return lFileInfo;
    }

    /*
     * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过 appInfo.publicSourceDir = apkPath;来修正这个问题，详情参见:
     * http://code.google.com/p/android/issues/detail?id=9151
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

    public static String getExtFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(dotPosition + 1, filename.length());
        }
        return "";
    }

    public static String getNameFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(0, dotPosition);
        }
        return "";
    }

    public static String getPathFromFilepath(String filepath) {
        int pos = filepath.lastIndexOf('/');
        if (pos != -1) {
            return filepath.substring(0, pos);
        }
        return "";
    }

    public static String getNameFromFilepath(String filepath) {
        int pos = filepath.lastIndexOf('/');
        if (pos != -1) {
            return filepath.substring(pos + 1);
        }
        return "";
    }

    // return new file path if successful, or return null
    public static String copyFile(String src, String dest) {
        File file = new File(src);
        if (!file.exists() || file.isDirectory()) {
            Log.v(LOG_TAG, "copyFile: file not exist or is directory, " + src);
            return null;
        }
        FileInputStream fi = null;
        FileOutputStream fo = null;
        try {
            fi = new FileInputStream(file);
            File destPlace = new File(dest);
            if (!destPlace.exists()) {
                if (!destPlace.mkdirs()) {
                    return null;
                }
            }

            String destPath = Util.makePath(dest, file.getName());
            File destFile = new File(destPath);
            int i = 1;
            while (destFile.exists()) {
                String destName = Util.getNameFromFilename(file.getName()) + " " + i++ + "."
                        + Util.getExtFromFilename(file.getName());
                destPath = Util.makePath(dest, destName);
                destFile = new File(destPath);
            }

            if (!destFile.createNewFile()) {
                return null;
            }

            fo = new FileOutputStream(destFile);
            int count = 102400;
            byte[] buffer = new byte[count];
            int read = 0;
            while ((read = fi.read(buffer, 0, count)) != -1) {
                fo.write(buffer, 0, read);
            }

            // TODO: set access privilege

            return destPath;
        }
        catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "copyFile: file not found, " + src);
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "copyFile: " + e.toString());
        }
        finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (fo != null) {
                    fo.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    // does not include sd card folder
    private static String[] SysFileDirs = new String[] { "miren_browser/imagecaches" };

    public static boolean shouldShowFile(String path) {
        return shouldShowFile(new File(path));
    }

    public static boolean shouldShowFile(File file) {
        boolean show = Settings.instance().getShowDotAndHiddenFiles();
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

    public static boolean setText(View view, int id, String text) {
        TextView textView = (TextView) view.findViewById(id);
        if (textView == null) {
            return false;
        }

        textView.setText(text);
        return true;
    }

    public static boolean setText(View view, int id, int text) {
        TextView textView = (TextView) view.findViewById(id);
        if (textView == null) {
            return false;
        }

        textView.setText(text);
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

    public static class SDCardInfo {
        public long total;

        public long free;
    }

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

    public static String formatDateString(Context context, long time) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        Date date = new Date(time);
        return dateFormat.format(date) + " " + timeFormat.format(date);
    }

    public static HashSet<String> sDocMimeTypesSet = new HashSet<String>() {
        {
            add("text/plain");
            add("text/plain");
            add("application/pdf");
            add("application/msword");
            add("application/vnd.ms-excel");
            add("application/vnd.ms-excel");
        }
    };

    public static String sZipFileMimeType = "application/zip";

    public static int CATEGORY_TAB_INDEX = 0;
    public static int SDCARD_TAB_INDEX = 1;

    public static boolean reloadFileList(String path, FileSortHelper fileSortHelper, List<FileInfo> fileInfoList) {
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
                FileInfo lFileInfo = Util.GetFileInfo(child, Settings.instance().getShowDotAndHiddenFiles());
                if (lFileInfo != null) {
                    fileInfoList.add(lFileInfo);
                }
            }
        }

        Collections.sort(fileInfoList, fileSortHelper.getComparator());
        return true;
    }

    /**
     * 根据long值时间转换成有好时间
     * 
     * @param timeLong
     * @return
     */
    public static String getDateByLong(long timeLong) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timeLong));
    }

}
