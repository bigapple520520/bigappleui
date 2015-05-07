/* 
 * @(#)ImageBucket.java    Created on 2014-11-7
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.entity;

import java.util.List;

/**
 * 这个类表示一个相册的实体对象
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-7 下午2:45:27 $
 */
public class ImageBucket {
    /** 相册id */
    public String bucketId;
    /** 相册名称 */
    public String bucketName;
    /** 相册中的图片列表 */
    public List<ImageItem> imageList;
}
