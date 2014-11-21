package com.dazzle.bigappleui.pullrefresh.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.GridView;

import com.dazzle.bigappleui.pullrefresh.core.ILoadingLayout.State;
import com.dazzle.bigappleui.pullrefresh.core.LoadingLayout;
import com.dazzle.bigappleui.pullrefresh.core.PullToRefreshBase;

/**
 * 这个类实现了GridView下拉刷新，上加载更多，注意不支持滚动到底部加载更多
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:38:59 $
 */
public class PullToRefreshGridView extends PullToRefreshBase<GridView> implements OnScrollListener {
    /** GridView */
    private GridView mGridView;
    /** 滚动的监听器 */
    private OnScrollListener mScrollListener;

    /**
     * 构造方法
     * 
     * @param context
     *            context
     */
    public PullToRefreshGridView(Context context) {
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
    public PullToRefreshGridView(Context context, AttributeSet attrs) {
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
    public PullToRefreshGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected GridView createRefreshableView(Context context, AttributeSet attrs) {
        mGridView = new GridView(context);
        mGridView.setOnScrollListener(this);
        return mGridView;
    }

    /**
     * 设置滑动的监听器
     * 
     * @param l
     *            监听器
     */
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                if (isReadyForPullUp()) {
                    startLoading();
                }
            }
        }

        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mScrollListener) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    /**
     * 设置是否有更多数据的标志
     * 
     * @param hasMoreData
     *            true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                footerLoadingLayout.setState(State.NO_MORE_DATA);
            }
        }
    }

    /**
     * 表示是否还有更多数据
     * 
     * @return true表示还有更多数据
     */
    public boolean hasMoreData() {
        if ((null != getFooterLoadingLayout()) && (getFooterLoadingLayout().getState() == State.NO_MORE_DATA)) {
            return false;
        }

        return true;
    }

    /**
     * 判断第一个child是否完全显示出来
     * 
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        final Adapter adapter = mGridView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        int mostTop = (mGridView.getChildCount() > 0) ? mGridView.getChildAt(0).getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断最后一个child是否完全显示出来
     * 
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final Adapter adapter = mGridView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mGridView.getLastVisiblePosition();

        /**
         * 这个检查条件其实应该是：lastVisiblePosition == lastItemPosition,但是ListView内部会使用一个FooterView导致了位置错乱。
         * 我的解决方案就是减去一个，然后在内部用getBottom()方法再去判断一下
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - mGridView.getFirstVisiblePosition();
            final int childCount = mGridView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mGridView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mGridView.getBottom();
            }
        }

        return false;
    }

}
