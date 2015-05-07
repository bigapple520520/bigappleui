package com.dazzle.bigappleui.pullrefresh.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 这个类定义了Header和Footer布局的共通行为，即他们的基类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:32:39 $
 */
public abstract class AbstractLoadingLayout extends FrameLayout implements ILoadingLayout {

    /** 容器布局 */
    private View mContainer;
    /** 记录当前的状态 */
    private State mCurState = State.NONE;
    /** 记录上一次的状态，这样用来比较是否需要调用不同的方法 */
    private State mPreState = State.NONE;

    public AbstractLoadingLayout(Context context) {
        this(context, null);
    }

    public AbstractLoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbstractLoadingLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * 初始化
     * 
     * @param context
     *            context
     * @param attrs
     *            attrs
     */
    protected void init(Context context, AttributeSet attrs) {
        mContainer = createLoadingView(context, attrs);
        if (null == mContainer) {
            throw new NullPointerException("Loading view can not be null.");
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(mContainer, params);
    }

    /**
     * 显示或隐藏这个布局
     * 
     * @param show
     *            true显示false不显示
     */
    public void show(boolean show) {
        // 设置的显示状态和当前一致，什么都不做直接返回
        if (show == (View.VISIBLE == getVisibility())) {
            return;
        }

        ViewGroup.LayoutParams params = mContainer.getLayoutParams();
        if (null != params) {
            if (show) {
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            else {
                params.height = 0;
            }
            setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public void setState(State state) {
        if (mCurState != state) {
            mPreState = mCurState;
            mCurState = state;
            onStateChanged(state, mPreState);
        }
    }

    @Override
    public State getState() {
        return mCurState;
    }

    /**
     * 得到前一个状态
     * 
     * @return 状态
     */
    protected State getPreState() {
        return mPreState;
    }

    /**
     * 当状态改变时调用
     * 
     * @param curState
     *            当前状态
     * @param oldState
     *            老的状态
     */
    protected void onStateChanged(State curState, State oldState) {
        switch (curState) {
        case RESET:
            onReset();
            break;
        case RELEASE_TO_REFRESH:
            onReleaseToRefresh();
            break;
        case PULL_TO_REFRESH:
            onPullToRefresh();
            break;
        case REFRESHING:
            onRefreshing();
            break;
        case NO_MORE_DATA:
            onNoMoreData();
            break;
        default:
            break;
        }
    }

    @Override
    public void onPull(float scale) {
    }

    /**
     * 设置最后更新的时间文本
     * 
     * @param label
     *            时间文本
     */
    public void setLastUpdatedLabel(CharSequence label) {
    }

    /**
     * 当状态设置为{@link State#RESET}时调用
     */
    protected abstract void onReset();

    /**
     * 当状态设置为{@link State#PULL_TO_REFRESH}时调用
     */
    protected abstract void onPullToRefresh();

    /**
     * 当状态设置为{@link State#RELEASE_TO_REFRESH}时调用
     */
    protected abstract void onReleaseToRefresh();

    /**
     * 当状态设置为{@link State#REFRESHING}时调用
     */
    protected abstract void onRefreshing();

    /**
     * 当状态设置为{@link State#NO_MORE_DATA}时调用
     */
    protected abstract void onNoMoreData();

    /**
     * 创建Loading的View，自定义时需要自己去实现
     * 
     * @param context
     *            context
     * @param attrs
     *            attrs
     * @return Loading的View
     */
    protected abstract View createLoadingView(Context context, AttributeSet attrs);

}
