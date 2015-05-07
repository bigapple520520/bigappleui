/* 
 * @(#)AlbumSettings.java    Created on 2014-12-12
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.core;

public class AlbumSettings {
    private static AlbumSettings instance;

    private AlbumSettings() {
    }

    /**
     * 获取单例
     * 
     * @return
     */
    public static AlbumSettings instance() {
        if (null == instance) {
            instance = new AlbumSettings();
        }
        return instance;
    }

    /** 显示小图时限制的宽高 */
    private int thumbnailQualityWidth = 200;
    private int thumbnailQualityHeight = 200;

    public int getThumbnailQualityWidth() {
        return thumbnailQualityWidth;
    }

    public void setThumbnailQualityWidth(int thumbnailQualityWidth) {
        this.thumbnailQualityWidth = thumbnailQualityWidth;
    }

    public int getThumbnailQualityHeight() {
        return thumbnailQualityHeight;
    }

    public void setThumbnailQualityHeight(int thumbnailQualityHeight) {
        this.thumbnailQualityHeight = thumbnailQualityHeight;
    }

}
