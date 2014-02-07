/* 
 * @(#)BehindView.java    Created on 2014-1-8
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.slidingupdown;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 背面拉出来的View
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-8 下午7:48:16 $
 */
public class BehindView extends ViewGroup {
	private SlidingUpDown slidingUpDown;

	private View mContent;// 从下出现的view
	private AboveView mAboveView;

	private final Paint mFadePaint = new Paint();
	private float mFadeDegree;
	private boolean mFadeEnabled;

	public BehindView(Context context) {
		this(context, null);
	}

	public BehindView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BehindView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		setMeasuredDimension(width, height);

		final int contentWidthMeasureSpec = getChildMeasureSpec(
				widthMeasureSpec, 0, width);
		final int contentHeightMeasureSpec = getChildMeasureSpec(
				heightMeasureSpec, 0, height);
		mContent.measure(contentWidthMeasureSpec, contentHeightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		mContent.layout(0, 0, width, height);
	}

	public void setContent(View view) {
		if (null != mContent) {
			removeView(mContent);
		}
		mContent = view;
		addView(mContent);
	}

	public View getContent() {
		return mContent;
	}

	public View getAboveView() {
		return mAboveView;
	}

	public void setAboveView(AboveView above) {
		this.mAboveView = above;
	}

	public SlidingUpDown getSlidingUpDown() {
		return slidingUpDown;
	}

	public void setSlidingUpDown(SlidingUpDown slidingUpDown) {
		this.slidingUpDown = slidingUpDown;
	}

	/**
	 * 根据比例在content绘制遮罩层渐变
	 * 
	 * @param content
	 *            AboveView对象
	 * @param canvas
	 * @param openPercent
	 */
	public void drawFade(View content, Canvas canvas, float openPercent) {
		if (!mFadeEnabled) {
			return;
		}

		final int alpha = (int) (mFadeDegree * 255 * Math.abs(1 - openPercent));
		mFadePaint.setColor(Color.argb(alpha, 0, 0, 0));

		int top = 0;
		int bottom = 0;
		if (slidingUpDown.getMode() == SlidingUpDown.MODE_UP) {
			top = content.getBottom();
			bottom = content.getBottom() + getHeight();
		} else if (slidingUpDown.getMode() == SlidingUpDown.MODE_DOWN) {
			top = content.getTop() - getHeight();
			bottom = content.getTop();
		} else if (slidingUpDown.getMode() == SlidingUpDown.MODE_UP_DOWN) {
			top = content.getBottom();
			bottom = content.getBottom() + getHeight();
			canvas.drawRect(0, top, getWidth(), bottom, mFadePaint);

			top = content.getTop() - getHeight();
			bottom = content.getTop();
		}
		canvas.drawRect(0, top, getWidth(), bottom, mFadePaint);
	}

	public float getFadeDegree() {
		return mFadeDegree;
	}

	public void setFadeDegree(float fadeDegree) {
		this.mFadeDegree = fadeDegree;
	}

	public boolean isFadeEnabled() {
		return mFadeEnabled;
	}

	public void setFadeEnabled(boolean fadeEnabled) {
		this.mFadeEnabled = fadeEnabled;
	}

}
