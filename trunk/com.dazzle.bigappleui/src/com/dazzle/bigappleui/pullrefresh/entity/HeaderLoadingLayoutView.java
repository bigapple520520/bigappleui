/* 
 * @(#)HeaderLoadingLayoutView.java    Created on 2014-11-21
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.pullrefresh.entity;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 头部的布局对象
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-21 下午6:25:11 $
 */
public class HeaderLoadingLayoutView {
    public LinearLayout root;

    public RelativeLayout headerContentLayout;

    /** 头部提示语部分 */
    public RelativeLayout headerTextLayout;
    public TextView headerTextHint;
    public TextView headerTextTimeHint;
    public TextView headerTextTimeText;

    public ImageView arrow;
    public ProgressBar progressBar;
}
