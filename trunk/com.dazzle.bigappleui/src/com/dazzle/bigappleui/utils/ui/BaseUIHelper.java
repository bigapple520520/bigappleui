/* 
 * @(#)BaseUIHelper.java    Created on 2014-12-9
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.utils.ui;

import android.R;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazzle.bigappleui.utils.ui.entity.TitleView;

/**
 * 所有动态布局帮助类的基类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-9 下午3:42:38 $
 */
public class BaseUIHelper {
    protected static int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    protected static int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * dp转化成sp
     * 
     * @param activity
     * @param dp
     *            dp为单位的数值
     * @return
     */
    protected static int getPx(Activity activity, int dp) {
        return (int) DisplayUtils.getPxByDp(activity, dp);
    }

    /**
     * 获取通用标题View对象
     * 
     * @param activity
     * @param parent
     * @return
     */
    protected static TitleView getTitleView(Activity activity, ViewGroup parent) {
        // 标题布局
        RelativeLayout headLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams headLayoutLp = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, (int) DisplayUtils.getPxByDp(activity, 50));
        headLayout.setLayoutParams(headLayoutLp);
        headLayout.setBackgroundColor(Color.parseColor("#414141"));
        parent.addView(headLayout);

        // 标题-左边文字
        final TextView leftTextView = new TextView(activity);
        RelativeLayout.LayoutParams leftTextViewLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        leftTextViewLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        leftTextViewLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftTextView.setLayoutParams(leftTextViewLp);
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
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        headLayout.addView(titleTextView);

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

        TitleView titleView = new TitleView();
        titleView.headLayout = headLayout;
        titleView.leftTextView = leftTextView;
        titleView.rightTextView = rightTextView;
        titleView.titleTextView = titleTextView;
        return titleView;
    }

    /**
     * 获取按下效果资源
     * 
     * @return
     */
    protected static StateListDrawable getSelectorDrawable() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] { R.attr.state_pressed }, new ColorDrawable(ColorUtils.COLOR_EBEBEB));
        return drawable;
    }

    /**
     * 指定按下颜色效果
     * 
     * @param color
     * @return
     */
    protected static StateListDrawable getPressedDrawable(int color) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] { R.attr.state_pressed }, new ColorDrawable(color));
        return drawable;
    }

}
