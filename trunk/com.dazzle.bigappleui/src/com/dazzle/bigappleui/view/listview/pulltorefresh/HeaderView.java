package com.dazzle.bigappleui.view.listview.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下拉刷新头部布局
 * 
 * @author xuan
 */
public class HeaderView extends LinearLayout {
	public ImageView arrow;
	public ProgressBar progressBar;
	public TextView pullToRefreshHint;
	public TextView lastUpdateHint;

	public HeaderView(Context context) {
		super(context);
		initView(context);
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {

	}

	public ImageView getArrow() {
		return arrow;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public TextView getPullToRefreshHint() {
		return pullToRefreshHint;
	}

	public TextView getLastUpdateHint() {
		return lastUpdateHint;
	}

}
