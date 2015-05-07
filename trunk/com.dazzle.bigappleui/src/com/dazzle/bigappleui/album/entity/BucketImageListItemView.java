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
    /** 缩略图控件 */
    public ImageView imageView;

    /** 选中半透明层 */
    public ImageView imageViewSel;
    /** 选中打钩层 */
    public ImageView hookImageSel;

    // dto
    public ImageItem bucketImage;
}
