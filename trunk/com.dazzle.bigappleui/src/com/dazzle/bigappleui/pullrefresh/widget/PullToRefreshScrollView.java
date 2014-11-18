package com.dazzle.bigappleui.pullrefresh.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.dazzle.bigappleui.pullrefresh.core.PullToRefreshBase;

/**
 * 封装了ScrollView的下拉刷新
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:39:17 $
 */
public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView> {
    /**
     * 构造方法
     * 
     * @param context
     *            context
     */
    public PullToRefreshScrollView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     * 
     * @param context
     *            context
     * @param attrs
     *            attrs
     */
    public PullToRefreshScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     * 
     * @param context
     *            context
     * @param attrs
     *            attrs
     * @param defStyle
     *            defStyle
     */
    public PullToRefreshScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#createRefreshableView(android.content.Context,
     *      android.util.AttributeSet)
     */
    @Override
    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        ScrollView scrollView = new ScrollView(context);
        return scrollView;
    }

    /**
     * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#isReadyForPullDown()
     */
    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }

    /**
     * @see com.nj1s.lib.pullrefresh.PullToRefreshBase#isReadyForPullUp()
     */
    @Override
    protected boolean isReadyForPullUp() {
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild) {
            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }

        return false;
    }
}
