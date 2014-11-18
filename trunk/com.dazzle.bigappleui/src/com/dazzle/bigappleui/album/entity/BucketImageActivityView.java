/* 
 * @(#)BucketImageActivityView.java    Created on 2014-11-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.entity;

import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 显示某个相册内所有图片的布局
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-11 下午3:38:06 $
 */
public class BucketImageActivityView {
    public LinearLayout root;

    public RelativeLayout headLayout;
    public TextView leftTextView;
    public TextView titleTextView;
    public TextView rightTextView;

    public GridView gridView;

}
