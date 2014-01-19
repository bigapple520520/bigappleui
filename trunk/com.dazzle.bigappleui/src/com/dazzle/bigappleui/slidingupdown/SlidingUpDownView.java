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
public class SlidingUpDownView extends FrameLayout {
	// 下拉或者上拉模式
	public static final int MODE_NONE = 0;// 不能上下拉
	public static final int MODE_UP = 1;// 上拉
	public static final int MODE_DOWN = 2;// 下拉
	public static final int MODE_UP_DOWN = 3;// 上下都可拉

	private final AboveView aboveView;
	private final BehindView behindView;

	private SlidingUpDownListener slidingUpDownListener;

	public SlidingUpDownView(Context context) {
		this(context, null);
	}

	public SlidingUpDownView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingUpDownView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		behindView = new BehindView(context);
		addView(behindView, behindParams);

		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		aboveView = new AboveView(context);
		addView(aboveView, aboveParams);

		setAbove(new FrameLayout(context));
		setDownBehind(new FrameLayout(context));

		// 相互引用
		aboveView.setSlidingUpDownView(this);
		behindView.setSlidingUpDownView(this);
	}

	public void setAbove(View above) {
		aboveView.setAbove(above);
	}

	public void setUpBehind(View upBehind) {
	}

	public void setDownBehind(View downBehind) {
		behindView.setDownBehind(downBehind);
	}

	public SlidingUpDownListener getSlidingUpDownListener() {
		return slidingUpDownListener;
	}

	public void setSlidingUpDownListener(
			SlidingUpDownListener slidingUpDownListener) {
		this.slidingUpDownListener = slidingUpDownListener;
	}

	// /////////////////////////////////////////above的状态///////////////////////////////////////////////
	public void snapAboveToUp() {
		aboveView.snapToUp();
	}

	public boolean isAboveLocationUp() {
		return aboveView.isLocationUp();
	}

	public void snapAvoveToDown() {
		aboveView.snapToDown();
	}

	public boolean isAboveLocationDown() {
		return aboveView.isLocationDown();
	}

	public void snapAboveToMiddle() {
		aboveView.snapToMiddle();
	}

	public boolean isAboveLocationMiddle() {
		return aboveView.isLocationMiddle();
	}

}
