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

    @Override
    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        ScrollView scrollView = new ScrollView(context);
        return scrollView;
    }

    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullUp() {
        // ScrollView只能放一个LinearLayout布局
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild) {
            /**
             * 逻辑是这样的：scrollViewChild.getHeight()是ScrollView孩子的高，这里就是LinearLayout布局，例如有800<br>
             * getHeight()是取到ScrollView的高，一般如果产生了滚动他的高会小于他的孩子的高，例如：500<br>
             * 他们两者相减就是滚动的可区间，例如这里可以滚动的区间是300<br>
             * 当我们向上滑动时getScrollY取到的是正数并随着View向上滚值变大，直到达到300后，就表明滚动到了最底部，这里返回true
             */
            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }

        return false;
    }

}
