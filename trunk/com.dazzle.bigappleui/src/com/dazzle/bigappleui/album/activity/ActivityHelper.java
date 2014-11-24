/* 
 * @(#)ActivityHelper.java    Created on 2014-11-10
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.album.activity;

import android.R;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazzle.bigappleui.album.entity.BucketActivityView;
import com.dazzle.bigappleui.album.entity.BucketImageActivityView;
import com.dazzle.bigappleui.album.entity.BucketImageListItemView;
import com.dazzle.bigappleui.album.entity.BucketListItemView;
import com.dazzle.bigappleui.utils.ColorUtils;
import com.dazzle.bigappleui.utils.DisplayUtils;
import com.dazzle.bigappleui.view.img.HookImageView;

/**
 * Activity创建帮助类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-10 上午10:04:23 $
 */
public abstract class ActivityHelper {

    /**
     * 布局选择相册的那个界面
     * 
     * @param activity
     * @return
     */
    public static BucketActivityView getBucketActivityView(Activity activity) {
        // 根
        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);

        // 标题
        RelativeLayout headLayout = new RelativeLayout(activity);
        LinearLayout.LayoutParams headLayoutLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) DisplayUtils.getPxByDp(activity, 50));
        headLayout.setLayoutParams(headLayoutLp);
        headLayout.setBackgroundColor(Color.parseColor("#414141"));
        root.addView(headLayout);

        // 标题-左边文字
        final TextView leftTextView = new TextView(activity);
        RelativeLayout.LayoutParams leftTextViewLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        leftTextViewLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        leftTextViewLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftTextView.setLayoutParams(leftTextViewLp);
        leftTextView.setText("返回");
        leftTextView.setTextColor(Color.WHITE);
        leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        leftTextView.setPadding(getPx(activity, 10), leftTextView.getPaddingTop(), getPx(activity, 10),
                leftTextView.getPaddingBottom());
        leftTextView.setGravity(Gravity.CENTER);
        headLayout.addView(leftTextView);

        // 标题-右边文字
        final TextView rightTextView = new TextView(activity);
        RelativeLayout.LayoutParams rightTextViewLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rightTextViewLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rightTextViewLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rightTextView.setLayoutParams(rightTextViewLp);
        rightTextView.setText("完成");
        rightTextView.setTextColor(Color.WHITE);
        rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        rightTextView.setPadding(getPx(activity, 10), leftTextView.getPaddingTop(), getPx(activity, 10),
                leftTextView.getPaddingBottom());
        rightTextView.setGravity(Gravity.CENTER);
        headLayout.addView(rightTextView);

        // 标题-中间标题
        TextView titleTextView = new TextView(activity);
        RelativeLayout.LayoutParams titleTextViewLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleTextViewLp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        titleTextView.setLayoutParams(titleTextViewLp);
        titleTextView.setText("相册选择");
        titleTextView.setTextColor(Color.parseColor("#FFFFFF"));
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        headLayout.addView(titleTextView);

        // 内容GridView
        GridView gridView = new GridView(activity);
        LinearLayout.LayoutParams gridViewLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        gridViewLp.leftMargin = getPx(activity, 10);
        gridViewLp.rightMargin = getPx(activity, 10);
        gridViewLp.topMargin = getPx(activity, 10);
        gridViewLp.bottomMargin = getPx(activity, 10);
        gridView.setLayoutParams(gridViewLp);
        gridView.setHorizontalSpacing(getPx(activity, 20));
        gridView.setVerticalSpacing(getPx(activity, 20));
        gridView.setNumColumns(2);
        gridView.setVerticalScrollBarEnabled(false);
        root.addView(gridView);

        // 设置按下效果
        leftTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // 设置按下变色效果
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    leftTextView.setBackgroundColor(ColorUtils.getColor(ColorUtils.TOP_BTN_COLOR_PRESSED));
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    leftTextView.setBackgroundColor(ColorUtils.getColor(ColorUtils.TOP_BTN_COLOR_NORMAL));
                    break;
                }
                return false;
            }
        });
        rightTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // 设置按下变色效果
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    rightTextView.setBackgroundColor(ColorUtils.getColor(ColorUtils.TOP_BTN_COLOR_PRESSED));
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    rightTextView.setBackgroundColor(ColorUtils.getColor(ColorUtils.TOP_BTN_COLOR_NORMAL));
                    break;
                }
                return false;
            }
        });

        BucketActivityView bucketActivityView = new BucketActivityView();
        bucketActivityView.root = root;
        bucketActivityView.headLayout = headLayout;
        bucketActivityView.leftTextView = leftTextView;
        bucketActivityView.titleTextView = titleTextView;
        bucketActivityView.rightTextView = rightTextView;
        bucketActivityView.gridView = gridView;
        return bucketActivityView;
    }

    /**
     * 获取相册列表项目View
     * 
     * @param activity
     * @return
     */
    public static BucketListItemView getBucketListItemView(Activity activity) {
        // 根
        LinearLayout root = new LinearLayout(activity);
        root.setBackgroundColor(Color.WHITE);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(getPx(activity, 10), getPx(activity, 10), getPx(activity, 10), getPx(activity, 10));

        // 图片
        ImageView imageView = new ImageView(activity);
        LinearLayout.LayoutParams imageViewLp = new LinearLayout.LayoutParams(getPx(activity, 156),
                getPx(activity, 128));
        imageView.setLayoutParams(imageViewLp);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        root.addView(imageView);

        // 相册名称
        TextView nameTextView = new TextView(activity);
        LinearLayout.LayoutParams nameTextViewLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        nameTextView.setLayoutParams(nameTextViewLp);
        nameTextView.setTextColor(Color.BLACK);
        nameTextView.setEllipsize(TruncateAt.END);
        nameTextView.setGravity(Gravity.LEFT);
        nameTextView.setSingleLine(true);
        root.addView(nameTextView);

        // 相册数量
        TextView countTextView = new TextView(activity);
        LinearLayout.LayoutParams countTextViewLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        countTextView.setLayoutParams(countTextViewLp);
        countTextView.setTextColor(Color.BLACK);
        countTextView.setGravity(Gravity.LEFT);
        root.addView(countTextView);

        BucketListItemView bucketListItemView = new BucketListItemView();
        bucketListItemView.root = root;
        bucketListItemView.imageView = imageView;
        bucketListItemView.nameTextView = nameTextView;
        bucketListItemView.countTextView = countTextView;
        return bucketListItemView;
    }

    /**
     * 获取某个相册的所有图片
     * 
     * @param activity
     * @return
     */
    public static BucketImageActivityView getBucketImageActivityView(Activity activity) {
        // 根
        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);

        // 标题
        RelativeLayout headLayout = new RelativeLayout(activity);
        LinearLayout.LayoutParams headLayoutLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int) DisplayUtils.getPxByDp(activity, 50));
        headLayout.setLayoutParams(headLayoutLp);
        headLayout.setBackgroundColor(Color.parseColor("#414141"));
        root.addView(headLayout);

        // 标题-左边文字
        final TextView leftTextView = new TextView(activity);
        RelativeLayout.LayoutParams leftTextViewLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        leftTextViewLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        leftTextViewLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftTextView.setLayoutParams(leftTextViewLp);
        leftTextView.setText("返回");
        leftTextView.setTextColor(Color.WHITE);
        leftTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        leftTextView.setPadding(getPx(activity, 10), leftTextView.getPaddingTop(), getPx(activity, 10),
                leftTextView.getPaddingBottom());
        leftTextView.setGravity(Gravity.CENTER);
        headLayout.addView(leftTextView);

        // 标题-右边文字
        final TextView rightTextView = new TextView(activity);
        RelativeLayout.LayoutParams rightTextViewLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rightTextViewLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rightTextViewLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rightTextView.setLayoutParams(rightTextViewLp);
        rightTextView.setText("完成");
        rightTextView.setTextColor(Color.WHITE);
        rightTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        rightTextView.setPadding(getPx(activity, 10), leftTextView.getPaddingTop(), getPx(activity, 10),
                leftTextView.getPaddingBottom());
        rightTextView.setGravity(Gravity.CENTER);
        headLayout.addView(rightTextView);

        // 标题-中间标题
        TextView titleTextView = new TextView(activity);
        RelativeLayout.LayoutParams titleTextViewLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleTextViewLp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        titleTextView.setLayoutParams(titleTextViewLp);
        titleTextView.setText("相册选择");
        titleTextView.setTextColor(Color.parseColor("#FFFFFF"));
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        headLayout.addView(titleTextView);

        // 内容GridView
        GridView gridView = new GridView(activity);
        LinearLayout.LayoutParams gridViewLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(gridViewLp);
        gridView.setPadding(getPx(activity, 8), getPx(activity, 8), getPx(activity, 8), getPx(activity, 8));
        gridView.setHorizontalSpacing(getPx(activity, 8));
        gridView.setVerticalSpacing(getPx(activity, 8));
        gridView.setNumColumns(3);
        gridView.setVerticalScrollBarEnabled(false);
        gridView.setSelector(R.color.transparent);// 去掉按下黄色这么难看的效果
        root.addView(gridView);

        // 设置按下效果
        leftTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // 设置按下变色效果
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    leftTextView.setBackgroundColor(ColorUtils.getColor(ColorUtils.TOP_BTN_COLOR_PRESSED));
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    leftTextView.setBackgroundColor(ColorUtils.getColor(ColorUtils.TOP_BTN_COLOR_NORMAL));
                    break;
                }
                return false;
            }
        });
        rightTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                // 设置按下变色效果
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    rightTextView.setBackgroundColor(ColorUtils.getColor(ColorUtils.TOP_BTN_COLOR_PRESSED));
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    rightTextView.setBackgroundColor(ColorUtils.getColor(ColorUtils.TOP_BTN_COLOR_NORMAL));
                    break;
                }
                return false;
            }
        });

        BucketImageActivityView bucketImageActivityView = new BucketImageActivityView();
        bucketImageActivityView.root = root;
        bucketImageActivityView.headLayout = headLayout;
        bucketImageActivityView.leftTextView = leftTextView;
        bucketImageActivityView.titleTextView = titleTextView;
        bucketImageActivityView.rightTextView = rightTextView;
        bucketImageActivityView.gridView = gridView;
        return bucketImageActivityView;
    }

    /**
     * 图片列表设配器布局
     * 
     * @param activity
     * @return
     */
    public static BucketImageListItemView getBucketImageListItemView(Activity activity) {
        // 根
        RelativeLayout root = new RelativeLayout(activity);

        // 图
        ImageView imageView = new ImageView(activity);
        RelativeLayout.LayoutParams imageViewLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(imageViewLp);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        root.addView(imageView);

        // 选中后的图
        ImageView imageViewSel = new HookImageView(activity);
        RelativeLayout.LayoutParams imageViewSelLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageViewSel.setLayoutParams(imageViewSelLp);
        imageViewSel.setBackgroundColor(Color.parseColor("#60000000"));
        imageViewSel.setVisibility(View.GONE);
        root.addView(imageViewSel);

        BucketImageListItemView bucketImageListItemView = new BucketImageListItemView();
        bucketImageListItemView.root = root;
        bucketImageListItemView.imageView = imageView;
        bucketImageListItemView.imageViewSel = imageViewSel;
        return bucketImageListItemView;
    }

    private static int getPx(Activity activity, int dp) {
        return (int) DisplayUtils.getPxByDp(activity, dp);
    }

}
