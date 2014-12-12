/* 
 * @(#)UIHepler.java    Created on 2014-12-9
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.fileexplorer.widget;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazzle.bigappleui.fileexplorer.core.DrawableHelper;
import com.dazzle.bigappleui.fileexplorer.entity.FileExplorerActivityView;
import com.dazzle.bigappleui.fileexplorer.entity.FileInfoListItemView;
import com.dazzle.bigappleui.utils.ui.BaseUIHelper;
import com.dazzle.bigappleui.utils.ui.ColorUtils;
import com.dazzle.bigappleui.utils.ui.entity.TitleView;

/**
 * 选择器界面布局帮助类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-9 下午5:46:15 $
 */
public class FileExplorerUIHepler extends BaseUIHelper {
    /**
     * 动态的界面布局
     * 
     * @param activity
     * @return
     */
    public static FileExplorerActivityView getFileExplorerActivity(Activity activity) {
        // 根
        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);

        // 标题
        TitleView titleView = getTitleView(activity, root);
        titleView.titleTextView.setText("文件选择器");
        titleView.leftTextView.setText("返回");
        titleView.rightTextView.setText("完成");

        titleView.headLayout.setBackgroundColor(DrawableHelper.getTitleBgColor());// 定制颜色
        titleView.leftTextView.setTextColor(DrawableHelper.getTitleTextColor());
        titleView.titleTextView.setTextColor(DrawableHelper.getTitleTextColor());
        titleView.rightTextView.setTextColor(DrawableHelper.getTitleTextColor());

        // 导航部分
        HorizontalScrollView navigationScrollView = new HorizontalScrollView(activity);
        LinearLayout.LayoutParams navigationScrollViewLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, getPx(activity, 40));
        navigationScrollView.setLayoutParams(navigationScrollViewLp);
        navigationScrollView.setBackgroundColor(ColorUtils.getColor("#EBEBEB"));
        navigationScrollView.setHorizontalScrollBarEnabled(false);

        LinearLayout navigationLayout = new LinearLayout(activity);
        FrameLayout.LayoutParams navigationLayoutLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, getPx(activity, 40));
        navigationLayout.setLayoutParams(navigationLayoutLp);
        navigationLayout.setPadding(getPx(activity, 10), 0, getPx(activity, 10), 0);
        navigationScrollView.addView(navigationLayout);
        root.addView(navigationScrollView);

        // 导航条和列表之间的分割线
        View divider = new View(activity);
        LinearLayout.LayoutParams dividerLp = new LinearLayout.LayoutParams(MATCH_PARENT, getPx(activity, 1));
        divider.setLayoutParams(dividerLp);
        divider.setBackgroundColor(Color.GRAY);
        root.addView(divider);

        // 内容部分
        FrameLayout container = new FrameLayout(activity);
        LinearLayout.LayoutParams containerLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        container.setLayoutParams(containerLp);

        ListView fileListView = new ListView(activity);// 数据列表
        FrameLayout.LayoutParams fileListViewLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        fileListView.setLayoutParams(fileListViewLp);
        fileListView.setDividerHeight(0);
        fileListView.setFadingEdgeLength(0);
        fileListView.setVerticalScrollBarEnabled(false);
        fileListView.setCacheColorHint(ColorUtils.COLOR_00000000);
        container.addView(fileListView);

        TextView noDataTextView = new TextView(activity);// 没有文件提示
        FrameLayout.LayoutParams noDataTextViewLp = new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        noDataTextView.setLayoutParams(noDataTextViewLp);
        noDataTextView.setVisibility(View.GONE);
        noDataTextView.setText("没有文件");
        noDataTextView.setGravity(Gravity.CENTER);
        noDataTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
        noDataTextView.setTextColor(Color.GRAY);
        container.addView(noDataTextView);

        root.addView(container);

        // 封装View对象
        FileExplorerActivityView fileExplorerActivityView = new FileExplorerActivityView();
        fileExplorerActivityView.root = root;
        fileExplorerActivityView.titleView = titleView;
        fileExplorerActivityView.navigationScrollView = navigationScrollView;
        fileExplorerActivityView.navigationLayout = navigationLayout;
        fileExplorerActivityView.container = container;
        fileExplorerActivityView.fileListView = fileListView;
        fileExplorerActivityView.noDataTextView = noDataTextView;

        return fileExplorerActivityView;
    }

    /**
     * 文件列表中的Item的View
     * 
     * @param activity
     * @return
     */
    public static FileInfoListItemView getFileInfoListItemView(Activity activity) {
        int fileIconId = 1;
        int selectImageViewId = 2;

        // 根
        final RelativeLayout root = new RelativeLayout(activity);
        root.setBackgroundColor(Color.WHITE);
        root.setBackgroundDrawable(getSelectorDrawable());

        // 左边文件图标
        ImageView fileIcon = new ImageView(activity);
        fileIcon.setId(fileIconId);
        RelativeLayout.LayoutParams fileIconLp = new RelativeLayout.LayoutParams(getPx(activity, 40), getPx(activity,
                40));
        fileIconLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        fileIconLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        fileIconLp.setMargins(getPx(activity, 10), 0, 0, 0);
        fileIcon.setLayoutParams(fileIconLp);
        root.addView(fileIcon);

        // 中间文字区域
        LinearLayout textLayout = new LinearLayout(activity);
        RelativeLayout.LayoutParams textLayoutLp = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        textLayoutLp.addRule(RelativeLayout.RIGHT_OF, fileIconId);
        textLayoutLp.addRule(RelativeLayout.LEFT_OF, selectImageViewId);
        textLayout.setLayoutParams(textLayoutLp);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        int textLayoutPadding = getPx(activity, 10);
        textLayout.setPadding(textLayoutPadding, textLayoutPadding, textLayoutPadding, textLayoutPadding);
        root.addView(textLayout);

        TextView textView1 = new TextView(activity);// 主标题
        LinearLayout.LayoutParams textView1Lp = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textView1.setLayoutParams(textView1Lp);
        textView1.setTextColor(Color.BLACK);
        textView1.setTextSize(16);
        textLayout.addView(textView1);

        TextView textView2 = new TextView(activity);// 副标题
        LinearLayout.LayoutParams textView2Lp = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textView2.setLayoutParams(textView2Lp);
        textView2.setTextColor(Color.GRAY);
        textView2.setTextSize(14);
        textLayout.addView(textView2);

        // 右边选中控件
        ImageView selectImageView = new ImageView(activity);
        selectImageView.setId(selectImageViewId);
        RelativeLayout.LayoutParams selectImageViewLp = new RelativeLayout.LayoutParams(getPx(activity, 30), getPx(
                activity, 30));
        selectImageViewLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        selectImageViewLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        selectImageViewLp.setMargins(0, 0, getPx(activity, 10), 0);
        selectImageView.setLayoutParams(selectImageViewLp);
        root.addView(selectImageView);

        // 分割线
        View divider = new View(activity);
        RelativeLayout.LayoutParams dividerLp = new RelativeLayout.LayoutParams(MATCH_PARENT, getPx(activity, 1));
        dividerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        divider.setLayoutParams(dividerLp);
        divider.setBackgroundColor(ColorUtils.getColor("#EBEBEB"));
        root.addView(divider);

        // 打包
        FileInfoListItemView fileInfoListItemView = new FileInfoListItemView();
        fileInfoListItemView.root = root;
        fileInfoListItemView.fileIcon = fileIcon;
        fileInfoListItemView.textView1 = textView1;
        fileInfoListItemView.textView2 = textView2;
        fileInfoListItemView.selectImageView = selectImageView;
        return fileInfoListItemView;
    }

    /**
     * 获取导航item
     * 
     * @param activity
     * @param text
     * @return
     */
    public static TextView getNavigationItem(Activity activity, String text, String filePath,
            OnClickListener onClickListener) {
        TextView textView = new TextView(activity);
        LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(WRAP_CONTENT, getPx(activity, 40));
        textView.setLayoutParams(textViewLp);

        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setTextColor(Color.GRAY);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setBackgroundDrawable(getPressedDrawable(ColorUtils.COLOR_D4D4D4));

        if (!TextUtils.isEmpty(filePath)) {
            textView.setTag(filePath);
        }

        if (null != onClickListener) {
            textView.setOnClickListener(onClickListener);
        }

        return textView;
    }

}
