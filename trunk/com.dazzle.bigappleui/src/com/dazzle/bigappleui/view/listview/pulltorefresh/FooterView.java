package com.dazzle.bigappleui.view.listview.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 上拉加载更多布局
 * 
 * @author xuan
 */
public class FooterView extends LinearLayout {
	public ProgressBar progressBar;
	public TextView textView;

	public FooterView(Context context) {
		super(context);
		initView(context);
	}

	public FooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {

	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public TextView getTextView() {
		return textView;
	}

}
