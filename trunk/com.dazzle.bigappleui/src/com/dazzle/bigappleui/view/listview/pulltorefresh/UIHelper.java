/* 
 * @(#)CreateViewUtils.java    Created on 2014-11-17
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.listview.pulltorefresh;

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

import com.dazzle.bigappleui.utils.ui.BaseUIHelper;
import com.dazzle.bigappleui.utils.ui.drawable.ArrowDrawable;

/**
 * 创建View帮助工具
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午10:08:09 $
 */
public abstract class UIHelper extends BaseUIHelper {

	/**
	 * 获取下拉刷新尾部布局
	 * 
	 * @param activity
	 * @return
	 */
	public static FooterView getFooterView(Activity activity) {
		FooterView footerView = new FooterView(activity);
		footerView.setOrientation(LinearLayout.HORIZONTAL);

		/** 内容布局 */
		LinearLayout contentLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams footerContentLp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, getPx(activity, 60));
		contentLayout.setLayoutParams(footerContentLp);
		contentLayout.setGravity(Gravity.CENTER);
		footerView.addView(contentLayout);

		/** 内容布局 ->进度条 */
		ProgressBar progressBar = new ProgressBar(activity);
		LinearLayout.LayoutParams progressBarLp = new LinearLayout.LayoutParams(
				getPx(activity, 28), getPx(activity, 28));
		progressBarLp.setMargins(0, 0, getPx(activity, 8), 0);
		progressBar.setLayoutParams(progressBarLp);
		contentLayout.addView(progressBar);

		/** 内容布局 ->提示文本 */
		TextView textView = new TextView(activity);
		LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(textViewLp);
		textView.setTextColor(Color.parseColor("#999999"));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		contentLayout.addView(textView);

		footerView.progressBar = progressBar;
		footerView.textView = textView;
		return footerView;
	}

	/**
	 * 获取头部布局
	 * 
	 * @return
	 */
	public static HeaderView getHeaderView(Activity activity) {
		HeaderView headerView = new HeaderView(activity);
		headerView.setOrientation(LinearLayout.VERTICAL);
		/** 内容布局 */
		RelativeLayout contentLayout = new RelativeLayout(activity);
		LinearLayout.LayoutParams headerContentLayoutLp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, getPx(activity, 60));
		contentLayout.setLayoutParams(headerContentLayoutLp);
		contentLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		headerView.addView(contentLayout);
		/** 内容布局 ->文字提示布局 */
		RelativeLayout textLayout = new RelativeLayout(activity);
		int textLayoutId = 1;
		textLayout.setId(textLayoutId);
		RelativeLayout.LayoutParams headerTextLayoutLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		headerTextLayoutLp.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		textLayout.setLayoutParams(headerTextLayoutLp);
		contentLayout.addView(textLayout);
		/** 内容布局 ->文字提示布局->上拉下拉可以刷新提示语 */
		TextView pullToRefreshHint = new TextView(activity);
		int pullToRefreshHintId = 2;
		pullToRefreshHint.setId(pullToRefreshHintId);
		RelativeLayout.LayoutParams headerTextHintLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		headerTextHintLp.addRule(RelativeLayout.ALIGN_PARENT_TOP,
				RelativeLayout.TRUE);
		pullToRefreshHint.setLayoutParams(headerTextHintLp);
		pullToRefreshHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		pullToRefreshHint.setTextColor(Color.parseColor("#999999"));
		pullToRefreshHint.setText("下拉可以刷新");
		textLayout.addView(pullToRefreshHint);
		/** 内容布局 ->文字提示布局->最后更新时间 :XXX提示 */
		TextView lastUpdateHint = new TextView(activity);
		RelativeLayout.LayoutParams headerTextTimeTextLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		headerTextTimeTextLp.addRule(RelativeLayout.BELOW, pullToRefreshHintId);
		lastUpdateHint.setLayoutParams(headerTextTimeTextLp);
		lastUpdateHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		lastUpdateHint.setTextColor(Color.parseColor("#999999"));
		lastUpdateHint.setPadding(getPx(activity, 2), getPx(activity, 6), 0, 0);
		textLayout.addView(lastUpdateHint);
		/** 内容布局->箭头图标 */
		ImageView arrow = new ImageView(activity);
		RelativeLayout.LayoutParams arrowLp = new RelativeLayout.LayoutParams(
				getPx(activity, 40), getPx(activity, 40));
		arrowLp.addRule(RelativeLayout.LEFT_OF, textLayoutId);
		arrowLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		arrowLp.setMargins(0, 0, getPx(activity, 8), 0);
		arrow.setLayoutParams(arrowLp);
		arrow.setImageDrawable(new ArrowDrawable());
		contentLayout.addView(arrow);
		/** 内容布局->进度圈 */
		ProgressBar progressBar = new ProgressBar(activity);
		RelativeLayout.LayoutParams progressBarLp = new RelativeLayout.LayoutParams(
				getPx(activity, 28), getPx(activity, 28));
		progressBarLp.addRule(RelativeLayout.LEFT_OF, textLayoutId);
		progressBarLp.addRule(RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.TRUE);
		progressBarLp.setMargins(0, 0, getPx(activity, 8), 0);
		progressBar.setLayoutParams(progressBarLp);
		progressBar.setVisibility(View.INVISIBLE);
		contentLayout.addView(progressBar);

		headerView.pullToRefreshHint = pullToRefreshHint;
		headerView.lastUpdateHint = lastUpdateHint;
		headerView.arrow = arrow;
		headerView.progressBar = progressBar;
		return headerView;
	}

}
