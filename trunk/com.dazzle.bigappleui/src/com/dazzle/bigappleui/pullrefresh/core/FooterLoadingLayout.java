package com.dazzle.bigappleui.pullrefresh.core;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dazzle.bigappleui.pullrefresh.entity.FooterLoadingLayoutView;

/**
 * 这个类封装了上拉刷新的布局
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午9:51:49 $
 */
public class FooterLoadingLayout extends LoadingLayout {
    /** 进度条 */
    private ProgressBar mProgressBar;
    /** 显示的文本 */
    private TextView mHintView;
    /** 布局对象 */
    private FooterLoadingLayoutView footerLoadingLayoutView;

    public FooterLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    public FooterLoadingLayout(Context context, AttributeSet attrs) {
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
        mProgressBar = footerLoadingLayoutView.progressBar;
        mHintView = footerLoadingLayoutView.textView;
        setState(State.RESET);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        footerLoadingLayoutView = ViewHelper.getFooterLoadingLayoutView((Activity) context);
        View container = footerLoadingLayoutView.root;
        return container;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
    }

    @Override
    public int getContentSize() {
        View view = footerLoadingLayoutView.root;
        if (null != view) {
            return view.getHeight();
        }

        return (int) (getResources().getDisplayMetrics().density * 40);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        mProgressBar.setVisibility(View.GONE);
        mHintView.setVisibility(View.INVISIBLE);

        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mHintView.setText("正在加载中");
    }

    @Override
    protected void onPullToRefresh() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText("上拉可以刷新");
    }

    @Override
    protected void onReleaseToRefresh() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText("松开后刷新");
    }

    @Override
    protected void onRefreshing() {
        mProgressBar.setVisibility(View.VISIBLE);
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText("正在加载中");
    }

    @Override
    protected void onNoMoreData() {
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText("已经到底啦");
    }

}
