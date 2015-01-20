package com.dazzle.bigappleui.pullrefresh.core;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 这个类封装了上拉加载更多的布局
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午9:51:49 $
 */
public class FooterLoadingLayout extends AbstractLoadingLayout {
    /** 进度条 */
    private ProgressBar mProgressBar;
    /** 显示的文本 */
    private TextView mHintView;
    /** 布局对象 */
    private FooterViewWraper footerViewWraper;

    public FooterLoadingLayout(Context context) {
        super(context);
    }

    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        footerViewWraper = PullToRefreshUIHelper.getFooterViewWraper((Activity) context);
        mProgressBar = footerViewWraper.progressBar;
        mHintView = footerViewWraper.textView;
        return footerViewWraper.root;
    }

    @Override
    protected void onReset() {
        mProgressBar.setVisibility(View.GONE);
        mHintView.setVisibility(View.GONE);
    }

    @Override
    protected void onPullToRefresh() {
        mProgressBar.setVisibility(View.GONE);
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText("上拉可以刷新");
    }

    @Override
    protected void onReleaseToRefresh() {
        mProgressBar.setVisibility(View.GONE);
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText("松开后刷新");
    }

    @Override
    protected void onRefreshing() {
        mProgressBar.setVisibility(View.VISIBLE);
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText("正在加载...");
    }

    @Override
    protected void onNoMoreData() {
        mProgressBar.setVisibility(View.GONE);
        mHintView.setVisibility(View.VISIBLE);
        mHintView.setText("已经到底啦");
    }

    /**
     * Footer布局对象打包
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2015-1-20 下午7:28:20 $
     */
    static class FooterViewWraper {
        public LinearLayout root;
        public LinearLayout footerContent;
        public ProgressBar progressBar;
        public TextView textView;
    }

}
