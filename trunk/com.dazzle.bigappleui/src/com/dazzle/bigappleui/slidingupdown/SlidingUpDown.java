/* 
 * @(#)PullUpDownView.java    Created on 2014-1-8
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.slidingupdown;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 下拉或者上拉惊喜哦
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-8 下午7:46:07 $
 */
public class SlidingUpDown extends FrameLayout {
	public static final int MODE_NONE = 0;// 不能上下拉
	public static final int MODE_UP = 1;// 上拉
	public static final int MODE_DOWN = 2;// 下拉
	public static final int MODE_UP_DOWN = 3;// 上下都可拉

	private final AboveView mAboveView;
	private final BehindView mBehindView;

	private SlidingUpDownListener slidingUpDownListener;
	private OpenPercentListener openPercentListener;

	public SlidingUpDown(Context context) {
		this(context, null);
	}

	public SlidingUpDown(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingUpDown(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mBehindView = new BehindView(context);
		addView(mBehindView, behindParams);

		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mAboveView = new AboveView(context);
		addView(mAboveView, aboveParams);

		setAboveContent(new FrameLayout(context));
		setBehindContent(new FrameLayout(context));

		// 相互引用
		mAboveView.setSlidingUpDown(this);
		mBehindView.setSlidingUpDown(this);
		mAboveView.setBehindView(mBehindView);
		mBehindView.setAboveView(mAboveView);

		// 各种参数可配置
		mAboveView.setMode(MODE_UP);

		mBehindView.setFadeEnabled(true);
		mBehindView.setFadeDegree(0.8f);
	}

	// ///////////////////////////////////////设置渐变效果///////////////////////////////////////////////////////
	public void setFadeEnabled(boolean b) {
		mBehindView.setFadeEnabled(b);
	}

	public void setFadeDegree(float f) {
		mBehindView.setFadeDegree(f);
	}

	// /////////////////////////////////////////////////设置主界面和背后界面/////////////////////////////////////////////
	public void setAboveContent(View view) {
		mAboveView.setContent(view);
	}

	public void setBehindContent(View view) {
		mBehindView.setContent(view);
	}

	// ///////////////////////////////////////////监听设置/////////////////////////////////////////////////
	public SlidingUpDownListener getSlidingUpDownListener() {
		return slidingUpDownListener;
	}

	public void setSlidingUpDownListener(
			SlidingUpDownListener slidingUpDownListener) {
		this.slidingUpDownListener = slidingUpDownListener;
	}

	public OpenPercentListener getOpenPercentListener() {
		return openPercentListener;
	}

	public void setOpenPercentListener(OpenPercentListener openPercentListener) {
		this.openPercentListener = openPercentListener;
	}

	// ///////////////////////////////////设置上拉还是下拉模式/////////////////////////////////////////////
	public void setMode(int mode) {
		mAboveView.setMode(mode);
	}

	public int getMode() {
		return mAboveView.getMode();
	}

	// /////////////////////////////////////////above的状态///////////////////////////////////////////////
	public void snapToUp() {
		mAboveView.snapToUp();
	}

	public boolean isLocationUp() {
		return mAboveView.isLocationUp();
	}

	public void snapAToDown() {
		mAboveView.snapToDown();
	}

	public boolean isLocationDown() {
		return mAboveView.isLocationDown();
	}

	public void snapToMiddle() {
		mAboveView.snapToMiddle();
	}

	public boolean isAboveLocationMiddle() {
		return mAboveView.isLocationMiddle();
	}

	/**
	 * 滑动的监听，0表示above滑动到上面，1表示above正好在中间，2表示above正好在下面
	 * 
	 * @author xuan
	 * @version $Revision: 1.0 $, $Date: 2014-1-17 下午7:21:49 $
	 */
	public interface SlidingUpDownListener {
		public void whichScreen(int which);
	}

	/**
	 * 打开过程的监听
	 * 
	 * @author xuan
	 */
	public interface OpenPercentListener {
		public void openPercent(float percent);
	}

}
