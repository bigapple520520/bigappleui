/* 
 * @(#)TitleView.java    Created on 2014-12-9
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils.ui.entity;

import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 标题的通用View打包
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-9 下午4:55:10 $
 */
public class TitleView {
    /** 标题布局 */
    public RelativeLayout headLayout;
    /** 标题文字 */
    public TextView titleTextView;
    /** 标题左边文字-一般都是返回 */
    public TextView leftTextView;
    /** 标题右边文字-一般都是确定 */
    public TextView rightTextView;
}
