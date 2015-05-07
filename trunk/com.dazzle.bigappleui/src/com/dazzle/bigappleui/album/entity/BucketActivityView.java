/* 
 * @(#)BucketActivityView.java    Created on 2014-11-10
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.entity;

import android.widget.GridView;
import android.widget.LinearLayout;

import com.dazzle.bigappleui.utils.ui.entity.TitleView;

/**
 * 相册选择界面的View
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-10 上午10:15:35 $
 */
public class BucketActivityView {
    public LinearLayout root;
    public TitleView titleView;
    public GridView gridView;

    public BucketActivityView() {
    }

    public BucketActivityView(LinearLayout root, TitleView titleView, GridView gridView) {
        this.root = root;
        this.titleView = titleView;
        this.gridView = gridView;
    }

}
