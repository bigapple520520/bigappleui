/* 
 * @(#)ImageItem.java    Created on 2014-11-7
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.entity;

import java.io.Serializable;

/**
 * 一张图片的一个对象
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 下午2:43:21 $
 */
public class BucketImage implements Serializable {
    private static final long serialVersionUID = 9054513023521353382L;

    public String imageId;// 图片id
    public String thumbnailPath;// 缩略图路径
    public String imagePath;// 原图路径
    public String dateAdded = "0";// 被添加时间
    public String dateModify = "0";// 最近被修改时间，距离1970年的秒数
}
