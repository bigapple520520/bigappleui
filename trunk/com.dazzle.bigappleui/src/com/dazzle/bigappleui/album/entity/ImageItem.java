/* 
 * @(#)ImageItem.java    Created on 2014-11-7
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.entity;

import java.io.Serializable;

/**
 * 表示一张图片的实体类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 下午2:43:21 $
 */
public class ImageItem implements Serializable {
    private static final long serialVersionUID = 9054513023521353382L;

    /** 图片id */
    public String imageId;
    /** 缩略图路径 */
    public String thumbnailPath;
    /** 原图路径 */
    public String imagePath;
    /** 创建时间 ，距离1970年的秒数 */
    public String dateAdded = "0";
    /** 最近被修改时间，距离1970年的秒数 */
    public String dateModified = "0";
}
