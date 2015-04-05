package com.dazzle.bigappleui.pullrefresh.core;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 这个类封装了下拉刷新的布局
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:31:50 $
 */
public class HeaderLoadingLayout extends AbstractLoadingLayout {
	/** 旋转动画时间 */
	private static final int ROTATE_ANIM_DURATION = 150;
	/** 箭头图片 */
	private ImageView mArrowImageView;
	/** 进度条 */
	private ProgressBar mProgressBar;
	/** 状态提示TextView */
	private TextView mHintTextView;
	/** 最后更新时间的TextView */
	private TextView mHeaderTimeView;
	/** 最后更新时间的标题 */
	private TextView mHeaderTimeViewTitle;
	/** 向上的动画 */
	private Animation mRotateUpAnim;
	/** 向下的动画 */
	private Animation mRotateDownAnim;

	/** header部分的布局 */
	private HeaderViewWraper headerViewWraper;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public HeaderLoadingLayout(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public HeaderLoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 *            context
	 */
	private void init(Context context) {
		mArrowImageView = headerViewWraper.arrow;
		mHintTextView = headerViewWraper.headerTextHint;
		mProgressBar = headerViewWraper.progressBar;
		mHeaderTimeView = headerViewWraper.headerTextTimeText;
		mHeaderTimeViewTitle = headerViewWraper.headerTextTimeHint;

		float pivotValue = 0.5f; // SUPPRESS CHECKSTYLE
		float toDegree = -180f; // SUPPRESS CHECKSTYLE
		// 初始化旋转动画
		mRotateUpAnim = new RotateAnimation(0.0f, toDegree,
				Animation.RELATIVE_TO_SELF, pivotValue,
				Animation.RELATIVE_TO_SELF, pivotValue);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(toDegree, 0.0f,
				Animation.RELATIVE_TO_SELF, pivotValue,
				Animation.RELATIVE_TO_SELF, pivotValue);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		// 如果最后更新的时间的文本是空的话，隐藏前面的标题
		mHeaderTimeViewTitle
				.setVisibility(TextUtils.isEmpty(label) ? View.INVISIBLE
						: View.VISIBLE);
		mHeaderTimeView.setText(label);
	}

	@Override
	protected View createLoadingView(Context context, AttributeSet attrs) {
		headerViewWraper = PullToRefreshUIHelper
				.getHeaderViewWraper((Activity) context);
		return headerViewWraper.root;
	}

	@Override
	protected void onNoMoreData() {
	}

	@Override
	protected void onStateChanged(State curState, State oldState) {
		mArrowImageView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		super.onStateChanged(curState, oldState);
	}

	@Override
	protected void onReset() {
		mArrowImageView.clearAnimation();
		mHintTextView.setText("下拉可以刷新");
	}

	@Override
	protected void onPullToRefresh() {
		if (State.RELEASE_TO_REFRESH == getPreState()) {
			mArrowImageView.clearAnimation();
			mArrowImageView.startAnimation(mRotateDownAnim);
		}

		mHintTextView.setText("下拉可以刷新");
	}

	@Override
	protected void onReleaseToRefresh() {
		mArrowImageView.clearAnimation();
		mArrowImageView.startAnimation(mRotateUpAnim);
		mHintTextView.setText("松开后刷新");
	}

	@Override
	protected void onRefreshing() {
		mArrowImageView.clearAnimation();
		mArrowImageView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.VISIBLE);
		mHintTextView.setText("正在加载...");
	}

	static class HeaderViewWraper {
		public LinearLayout root;

		public RelativeLayout headerContentLayout;

		/** 头部提示语部分 */
		public RelativeLayout headerTextLayout;
		public TextView headerTextHint;
		public TextView headerTextTimeHint;
		public TextView headerTextTimeText;

		public ImageView arrow;
		public ProgressBar progressBar;
	}

}
