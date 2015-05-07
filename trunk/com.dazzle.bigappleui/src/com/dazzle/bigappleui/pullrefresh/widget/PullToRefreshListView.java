package com.dazzle.bigappleui.pullrefresh.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ListView;

import com.dazzle.bigappleui.pullrefresh.core.FooterLoadingLayout;
import com.dazzle.bigappleui.pullrefresh.core.ILoadingLayout.State;
import com.dazzle.bigappleui.pullrefresh.core.AbstractLoadingLayout;
import com.dazzle.bigappleui.pullrefresh.core.PullToRefreshBase;

/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:39:08 $
 */
public class PullToRefreshListView extends PullToRefreshBase<ListView> implements OnScrollListener {

    /** ListView控件 */
    private ListView mListView;
    /** 用于滑到底部自动加载的Footer，这个footer会加到ListView的底部，在setScrollLoadEnabled的时候被调用 */
    private AbstractLoadingLayout mLoadMoreFooterLayout;
    /** 滚动的监听器 */
    private OnScrollListener mScrollListener;

    private View temp;// 仅仅用来给footer占个位置，不然footer显示不出来，有大神知道更nb的解决方案请赐教

    public PullToRefreshListView(Context context) {
        this(context, null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ListView createRefreshableView(Context context, AttributeSet attrs) {
        mListView = new ListView(context);
        mListView.setOnScrollListener(this);

        // 这个也是出于无奈，好像要先占个位置，然后后面的footer才能显示出来
        temp = new View(context);
        mListView.addFooterView(temp);

        return mListView;
    }

    @Override
    public AbstractLoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }
        else {
            return super.getFooterLoadingLayout();
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
            AbstractLoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                footerLoadingLayout.setState(State.NO_MORE_DATA);
            }
        }
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
    protected void startLoading() {
        super.startLoading();

        // 设置到加载中状态
        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.REFRESHING);
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        // 设置到闲置状态
        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.RESET);
        }
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 创建一个Footer布局
            if (null == mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
            }

            // 把Footer布局添加到ListView的FooterView中
            if (null == mLoadMoreFooterLayout.getParent()) {
                mListView.addFooterView(mLoadMoreFooterLayout, null, false);
                mListView.removeFooterView(temp);
            }
            mLoadMoreFooterLayout.show(true);
        }
        else {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    /**
     * 表示是否还有更多数据
     * 
     * @return true表示还有更多数据
     */
    private boolean hasMoreData() {
        if ((null != mLoadMoreFooterLayout) && (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA)) {
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
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
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
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        /**
         * 这个检查条件其实应该是：lastVisiblePosition == lastItemPosition,但是ListView内部会使用一个FooterView导致了位置错乱。
         * 我的解决方案就是减去一个，然后在内部用getBottom()方法再去判断一下
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - mListView.getFirstVisiblePosition();
            final int childCount = mListView.getChildCount();
            // 如果childCount - 1比childIndex还小，说明这个ListView的所有item还没显示满屏
            final int index = Math.min(childIndex, childCount - 1);
            // 取到最后显示的item的View
            final View lastVisibleChild = mListView.getChildAt(index);
            if (lastVisibleChild != null) {
                // 如果最后一个显示的item的View的底边距上高度小于ListView的底边距上高度了，那么就算滚动到最后了
                return lastVisibleChild.getBottom() <= mListView.getBottom();
            }
        }

        return false;
    }

    // ///////////////////////////////////////以下是OnScrollListener接口的实现///////////////////////////////////////
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

}
