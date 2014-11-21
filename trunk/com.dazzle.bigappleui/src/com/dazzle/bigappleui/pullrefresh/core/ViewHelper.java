/* 
 * @(#)CreateViewUtils.java    Created on 2014-11-17
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.pullrefresh.core;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dazzle.bigappleui.pullrefresh.entity.FooterLoadingLayoutView;
import com.dazzle.bigappleui.utils.DisplayUtils;

/**
 * 创建View帮助工具
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午10:08:09 $
 */
public abstract class ViewHelper {

    /**
     * 获取下拉刷新尾部布局
     * 
     * @param activity
     * @return
     */
    public static FooterLoadingLayoutView getFooterLoadingLayoutView(Activity activity) {
        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.HORIZONTAL);

        // 创建footerContent布局
        LinearLayout footerContent = new LinearLayout(activity);
        LinearLayout.LayoutParams footerContentLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, getPx(activity, 60));
        footerContent.setLayoutParams(footerContentLp);
        footerContent.setGravity(Gravity.CENTER);
        root.addView(footerContent);

        // 创建进度条布局
        ProgressBar progressBar = new ProgressBar(activity);
        LinearLayout.LayoutParams progressBarLp = new LinearLayout.LayoutParams(getPx(activity, 28),
                getPx(activity, 28));
        progressBarLp.setMargins(0, 0, getPx(activity, 8), 0);
        progressBar.setLayoutParams(progressBarLp);
        footerContent.addView(progressBar);

        // 创建提示文本布局
        TextView textView = new TextView(activity);
        LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textViewLp);
        textView.setTextColor(Color.parseColor("#999999"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        footerContent.addView(textView);

        FooterLoadingLayoutView footerLoadingLayoutView = new FooterLoadingLayoutView();
        footerLoadingLayoutView.root = root;
        footerLoadingLayoutView.footerContent = footerContent;
        footerLoadingLayoutView.progressBar = progressBar;
        footerLoadingLayoutView.textView = textView;
        return footerLoadingLayoutView;
    }

    private static int getPx(Activity activity, int dp) {
        return (int) DisplayUtils.getPxByDp(activity, dp);
    }

}
