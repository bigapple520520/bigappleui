package com.dazzle.bigappleui.pullrefresh.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.webkit.WebView;

import com.dazzle.bigappleui.pullrefresh.core.PullToRefreshBase;

/**
 * 封装了WebView的下拉刷新
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:39:32 $
 */
public class PullToRefreshWebView extends PullToRefreshBase<WebView> {

    public PullToRefreshWebView(Context context) {
        this(context, null);
    }

    public PullToRefreshWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected WebView createRefreshableView(Context context, AttributeSet attrs) {
        WebView webView = new WebView(context);
        return webView;
    }

    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullUp() {
        /**
         * 这里的逻辑是这样的：WebView的高减去这个控件本身的高就是可滑动区间偏移
         */
        float exactContentHeight = FloatMath.floor(mRefreshableView.getContentHeight() * mRefreshableView.getScale());
        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
    }

}
