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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazzle.bigappleui.pullrefresh.core.FooterLoadingLayout.FooterViewWraper;
import com.dazzle.bigappleui.pullrefresh.core.HeaderLoadingLayout.HeaderViewWraper;
import com.dazzle.bigappleui.utils.ui.BaseUIHelper;
import com.dazzle.bigappleui.view.img.ArrowImageView;

/**
 * 创建View帮助工具
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午10:08:09 $
 */
public abstract class PullToRefreshUIHelper extends BaseUIHelper {

    /**
     * 获取下拉刷新尾部布局
     * 
     * @param activity
     * @return
     */
    public static FooterViewWraper getFooterViewWraper(Activity activity) {
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

        FooterViewWraper footerViewWraper = new FooterViewWraper();
        footerViewWraper.root = root;
        footerViewWraper.footerContent = footerContent;
        footerViewWraper.progressBar = progressBar;
        footerViewWraper.textView = textView;
        return footerViewWraper;
    }

    /**
     * 获取头部布局
     * 
     * @return
     */
    public static HeaderViewWraper getHeaderViewWraper(Activity activity) {
        int headerTextHintId = 1;
        int headerTextTimeHintId = 2;
        int headerTextLayoutId = 3;

        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout headerContentLayout = new RelativeLayout(activity);
        LinearLayout.LayoutParams headerContentLayoutLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, getPx(activity, 60));
        headerContentLayout.setLayoutParams(headerContentLayoutLp);
        headerContentLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        root.addView(headerContentLayout);

        RelativeLayout headerTextLayout = new RelativeLayout(activity);
        headerTextLayout.setId(headerTextLayoutId);
        RelativeLayout.LayoutParams headerTextLayoutLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerTextLayoutLp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        headerTextLayout.setLayoutParams(headerTextLayoutLp);
        headerContentLayout.addView(headerTextLayout);

        /** 头部提示部分文字 */
        TextView headerTextHint = new TextView(activity);
        headerTextHint.setId(headerTextHintId);
        RelativeLayout.LayoutParams headerTextHintLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerTextHintLp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        headerTextHint.setLayoutParams(headerTextHintLp);
        headerTextHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        headerTextHint.setTextColor(Color.parseColor("#999999"));
        headerTextHint.setText("下拉可以刷新");
        headerTextLayout.addView(headerTextHint);

        TextView headerTextTimeHint = new TextView(activity);
        headerTextTimeHint.setId(headerTextTimeHintId);
        RelativeLayout.LayoutParams headerTextTimeHintLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerTextTimeHintLp.addRule(RelativeLayout.BELOW, headerTextHintId);
        headerTextTimeHint.setLayoutParams(headerTextTimeHintLp);
        headerTextTimeHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        headerTextTimeHint.setTextColor(Color.parseColor("#999999"));
        headerTextTimeHint.setText("最后更新时间 :");
        headerTextTimeHint.setPadding(0, getPx(activity, 6), 0, 0);
        headerTextLayout.addView(headerTextTimeHint);

        TextView headerTextTimeText = new TextView(activity);
        RelativeLayout.LayoutParams headerTextTimeTextLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerTextTimeTextLp.addRule(RelativeLayout.BELOW, headerTextHintId);
        headerTextTimeTextLp.addRule(RelativeLayout.RIGHT_OF, headerTextTimeHintId);
        headerTextTimeText.setLayoutParams(headerTextTimeTextLp);
        headerTextTimeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        headerTextTimeText.setTextColor(Color.parseColor("#999999"));
        headerTextTimeText.setPadding(getPx(activity, 2), getPx(activity, 6), 0, 0);
        headerTextLayout.addView(headerTextTimeText);

        /** 箭头图标 */
        ImageView arrow = new ArrowImageView(activity);
        RelativeLayout.LayoutParams arrowLp = new RelativeLayout.LayoutParams(getPx(activity, 40), getPx(activity, 40));
        arrowLp.addRule(RelativeLayout.LEFT_OF, headerTextLayoutId);
        arrowLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        arrowLp.setMargins(0, 0, getPx(activity, 8), 0);
        arrow.setLayoutParams(arrowLp);
        headerContentLayout.addView(arrow);

        /** 进度圈 */
        ProgressBar progressBar = new ProgressBar(activity);
        RelativeLayout.LayoutParams progressBarLp = new RelativeLayout.LayoutParams(getPx(activity, 28), getPx(
                activity, 28));
        progressBarLp.addRule(RelativeLayout.LEFT_OF, headerTextLayoutId);
        progressBarLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        progressBarLp.setMargins(0, 0, getPx(activity, 8), 0);
        progressBar.setLayoutParams(progressBarLp);
        progressBar.setVisibility(View.INVISIBLE);
        headerContentLayout.addView(progressBar);

        HeaderViewWraper headerViewWraper = new HeaderViewWraper();
        headerViewWraper.root = root;
        headerViewWraper.headerContentLayout = headerContentLayout;
        headerViewWraper.headerTextLayout = headerTextLayout;
        headerViewWraper.headerTextHint = headerTextHint;
        headerViewWraper.headerTextTimeHint = headerTextTimeHint;
        headerViewWraper.headerTextTimeText = headerTextTimeText;
        headerViewWraper.arrow = arrow;
        headerViewWraper.progressBar = progressBar;
        return headerViewWraper;
    }

}
