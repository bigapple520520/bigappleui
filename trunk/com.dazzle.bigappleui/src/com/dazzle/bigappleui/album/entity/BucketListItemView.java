/* 
 * @(#)BucketListItemView.java    Created on 2014-11-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.entity;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 这个是相册列表中的Item项目View
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-11 下午2:11:55 $
 */
public class BucketListItemView {
    /** 根 */
    public LinearLayout root;
    /** 相册图片 */
    public ImageView imageView;
    /** 相册名称 */
    public TextView nameTextView;
    /** 相册里面含有图片数量 */
    public TextView countTextView;
}
