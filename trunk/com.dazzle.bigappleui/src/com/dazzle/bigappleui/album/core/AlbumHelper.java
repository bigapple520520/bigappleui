/* 
 * @(#)AlbumHelper.java    Created on 2014-11-7
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import com.dazzle.bigappleui.album.entity.Bucket;
import com.dazzle.bigappleui.album.entity.BucketImage;

/**
 * 获取相册数据帮助类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 下午2:42:16 $
 */
public class AlbumHelper {
    public final static String TAG = "AlbumHelper";

    private static AlbumHelper instance;

    private Context context;
    private boolean hasBuildImagesBucketList = false;// 是否创建了图片集
    private Map<String, Bucket> bucketMap = new HashMap<String, Bucket>();

    /**
     * 获取单例
     * 
     * @return
     */
    public static AlbumHelper instance() {
        if (null == instance) {
            throw new NullPointerException("请先调用初始化方法：init(Context context)");
        }

        return instance;
    }

    /**
     * 初始化
     * 
     * @param context
     */
    public synchronized static void init(Context context) {
        if (null == instance) {
            if (context instanceof Activity) {
                instance = new AlbumHelper();
                instance.context = ((Activity) context).getApplication();
            }
            else if (context instanceof Application) {
                instance = new AlbumHelper();
                instance.context = context;
            }
            else {
                throw new IllegalArgumentException("初始化init传入Context类型必须是Activity或者Application");
            }
        }
    }

    /**
     * 得到图片集
     * 
     * @param refresh
     * @return
     */
    public Map<String, Bucket> getImagesBucketMap(boolean needRefresh) {
        if (needRefresh || (!needRefresh && !hasBuildImagesBucketList)) {
            bucketMap.clear();
            buildImagesBucketList();
        }
        return bucketMap;
    }

    /**
     * 得到图片集
     * 
     * @param refresh
     * @return
     */
    public Map<String, Bucket> getImagesBucketMapSortByDatemodify(boolean needRefresh) {
        if (needRefresh || (!needRefresh && !hasBuildImagesBucketList)) {
            bucketMap.clear();
            buildImagesBucketList();
            sortByDatemodify();
        }
        return bucketMap;
    }

    /**
     * 根据修改时间排序
     */
    public void sortByDatemodify() {
        for (Entry<String, Bucket> entry : bucketMap.entrySet()) {
            Bucket imageBucket = entry.getValue();
            if (null != imageBucket.imageList) {
                Collections.sort(imageBucket.imageList, new Comparator<BucketImage>() {
                    @Override
                    public int compare(BucketImage imageItem1, BucketImage imageItem2) {
                        long dateModifyLong1 = Long.valueOf(imageItem1.dateModify);
                        long dateModifyLong2 = Long.valueOf(imageItem2.dateModify);
                        return dateModifyLong1 < dateModifyLong2 ? 1 : -1;
                    }
                });
            }
        }
    }

    /**
     * 获取缩略图的map集合，其中key是原图的id，value是缩略图的路径
     * 
     * @return
     */
    public HashMap<String, String> getImageId2ThumbnailPathMap() {
        String[] columns = { Thumbnails.IMAGE_ID, Thumbnails.DATA };
        Cursor cursor = context.getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, columns, null, null, null);
        HashMap<String, String> imageId2ThumbnailPathMap = new HashMap<String, String>();
        try {
            if (cursor.moveToFirst()) {
                int imageId;
                String thumbnailPath;
                int imageIdColumnIndex = cursor.getColumnIndex(Thumbnails.IMAGE_ID);// 缩略图对应的原图编号
                int thumbnailPathColumnIndex = cursor.getColumnIndex(Thumbnails.DATA);// 缩略图的路径
                do {
                    imageId = cursor.getInt(imageIdColumnIndex);
                    thumbnailPath = cursor.getString(thumbnailPathColumnIndex);
                    imageId2ThumbnailPathMap.put(String.valueOf(imageId), thumbnailPath);
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        finally {
            cursor.close();
        }

        return imageId2ThumbnailPathMap;
    }

    // 获取所有图片的数据
    private void buildImagesBucketList() {
        // 获取缩略图的map
        HashMap<String, String> imageId2ThumbnailPathMap = getImageId2ThumbnailPathMap();

        // 获取大图数据，组装进缩略
        String columns[] = new String[] { Media._ID, Media.BUCKET_ID, Media.DATA, Media.BUCKET_DISPLAY_NAME };
        Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                // 获取指定列的索引
                int imageIdColumn = cursor.getColumnIndexOrThrow(Media._ID);// 相册原图的编号
                int bucketIdColumn = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);// 对应文件夹的编号
                int bucketDisplayNameColumn = cursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);// 文件夹的名称
                int imagePathColumn = cursor.getColumnIndexOrThrow(Media.DATA);// 原图的路径
                do {
                    String imageId = cursor.getString(imageIdColumn);
                    String bucketId = cursor.getString(bucketIdColumn);
                    String bucketName = cursor.getString(bucketDisplayNameColumn);
                    String imagePath = cursor.getString(imagePathColumn);
                    Bucket bucket = bucketMap.get(bucketId);
                    if (null == bucket) {
                        bucket = new Bucket();
                        bucketMap.put(bucketId, bucket);
                        bucket.imageList = new ArrayList<BucketImage>();
                        bucket.bucketName = bucketName;
                        bucket.bucketId = bucketId;
                    }
                    BucketImage imageItem = new BucketImage();
                    imageItem.imageId = imageId;
                    imageItem.imagePath = imagePath;
                    imageItem.thumbnailPath = imageId2ThumbnailPathMap.get(imageId);
                    bucket.imageList.add(imageItem);
                }
                while (cursor.moveToNext());
            }
            hasBuildImagesBucketList = true;
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        finally {
            cursor.close();
        }
    }

}