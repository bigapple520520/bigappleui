/* 
 * @(#)BucketImageListView.java    Created on 2014-11-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.entity;

import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 图片列表Item的View
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-11 下午3:58:50 $
 */
public class BucketImageListItemView {
    public RelativeLayout root;
    public ImageView imageView;
    public ImageView imageViewSel;

    // dto
    public ImageItem bucketImage;
}
