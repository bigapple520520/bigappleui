/* 
 * @(#)CreateViewUtils.java    Created on 2014-11-5
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.photoview.app.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazzle.bigappleui.view.photoview.PhotoView;
import com.dazzle.bigappleui.view.photoview.app.core.HackyViewPager;

/**
 * 动态创建布局工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-5 下午4:49:56 $
 */
public abstract class CreateViewHelper {

    // //////////////////////////////单张图片显示的Framgment的内容布局////////////////////////////////
    /**
     * 获取单张图片显示的Fragment控件的布局
     * 
     * @param context
     * @return
     */
    public static WraperFragmentView getFragmentView(Context context) {
        RelativeLayout root = new RelativeLayout(context);
        root.setBackgroundColor(Color.BLACK);

        // 缩放图片控件
        PhotoView photoView = new PhotoView(context);
        photoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        root.addView(photoView);

        // 加载提示进度条
        ProgressBar progressBar = new ProgressBar(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        progressBar.setLayoutParams(lp);
        progressBar.setVisibility(View.GONE);
        root.addView(progressBar);

        WraperFragmentView wraperFragmentView = new WraperFragmentView();
        wraperFragmentView.root = root;
        wraperFragmentView.photoView = photoView;
        wraperFragmentView.progressBar = progressBar;
        return wraperFragmentView;
    }

    // ////////////////////////////////的内容布局/////////////////////////////////////
    /**
     * 获取Activity的布局
     * 
     * @param context
     * @return
     */
    public static WraperActivityView getWraperActivityView(Context context) {
        RelativeLayout root = new RelativeLayout(context);
        root.setBackgroundColor(Color.BLACK);

        // 所图切换viewPager
        HackyViewPager hackyViewPager = new HackyViewPager(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        hackyViewPager.setLayoutParams(lp);
        hackyViewPager.setBackgroundColor(Color.BLACK);
        hackyViewPager.setId(99999999);
        root.addView(hackyViewPager);

        // 显示当前图片位置
        TextView textView = new TextView(context);
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        textView.setLayoutParams(lp);
        textView.setTextSize(18);
        textView.setTextColor(Color.WHITE);
        root.addView(textView);

        WraperActivityView wraperActivityView = new WraperActivityView();
        wraperActivityView.root = root;
        wraperActivityView.hackyViewPager = hackyViewPager;
        wraperActivityView.textView = textView;
        return wraperActivityView;
    }

}
