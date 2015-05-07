package com.dazzle.bigappleui.pullrefresh.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dazzle.bigappleui.pullrefresh.core.ILoadingLayout.State;

/**
 * 这个实现了下拉刷新和上拉加载更多的功能
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-13 下午4:09:11 $
 */
public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {

    /** 回滚的时间 */
    private static final int SCROLL_DURATION = 150;
    /** 阻尼系数 */
    private static final float OFFSET_RADIO = 2.5f;
    /** 上一次移动的点 */
    private float mLastMotionY = -1;
    /** 下拉刷新和加载更多的监听器 */
    private OnRefreshListener<T> mRefreshListener;

    /** 可刷新View的包装布局 */
    private FrameLayout mRefreshableViewWrapper;
    /** 下拉刷新的布局 */
    private AbstractLoadingLayout mHeaderLayout;
    /** 上拉加载更多的布局 */
    private AbstractLoadingLayout mFooterLayout;

    /** HeaderView的高度 */
    private int mHeaderHeight;
    /** FooterView的高度 */
    private int mFooterHeight;

    /** 下拉刷新是否可用 */
    private boolean mPullRefreshEnabled = true;
    /** 上拉加载是否可用 */
    private boolean mPullLoadEnabled = false;
    /** 判断滑动到底部加载是否可用 */
    private boolean mScrollLoadEnabled = false;

    /** 是否截断touch事件 */
    private boolean mInterceptEventEnable = true;
    /** 表示是否消费了touch事件，如果是，则不调用父类的onTouchEvent方法 */
    private boolean mIsHandledTouchEvent = false;

    /** 移动点的保护范围值 */
    private int mTouchSlop;

    /** 下拉的状态 */
    private State mPullDownState = State.NONE;
    /** 上拉的状态 */
    private State mPullUpState = State.NONE;
    /** 可以下拉刷新的View */
    protected T mRefreshableView;
    /** 平滑滚动的Runnable */
    private SmoothScrollRunnable mSmoothScrollRunnable;

    /**
     * 构造方法
     * 
     * @param context
     *            context
     */
    public PullToRefreshBase(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * 构造方法
     * 
     * @param context
     *            context
     * @param attrs
     *            attrs
     */
    public PullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
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
    public PullToRefreshBase(Context context, AttributeSet attrs, int defStyle) {
        // super(context, attrs, defStyle);//低版本的没有这个构造器
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化
     * 
     * @param context
     *            context
     */
    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.VERTICAL);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mRefreshableView = createRefreshableView(context, attrs);
        mHeaderLayout = createHeaderLoadingLayout(context, attrs);
        mFooterLayout = createFooterLoadingLayout(context, attrs);

        if (null == mRefreshableView) {
            throw new NullPointerException("Refreshable view can not be null.");
        }

        addRefreshableView(context, mRefreshableView);
        addHeaderAndFooter(context);
    }

    @Override
    protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 当footer布局和header布局变动时，需要修改padding值，让他们处于不可见状态
        refreshLoadingViewsSize();
        // 重新设置包装器的高度
        refreshRefreshableViewSize(w, h);
    }

    @Override
    public void setOrientation(int orientation) {
        // 这个控件虽然是继承了LinearLayout，但是只能支持垂直VERTICAL布局哦
        if (LinearLayout.VERTICAL != orientation) {
            throw new IllegalArgumentException("This class only supports VERTICAL orientation.");
        }

        super.setOrientation(orientation);
    }

    @Override
    public final boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isInterceptTouchEventEnabled()) {
            return false;
        }

        if (!isPullLoadEnabled() && !isPullRefreshEnabled()) {
            return false;
        }

        final int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsHandledTouchEvent = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && mIsHandledTouchEvent) {
            return true;
        }

        switch (action) {
        case MotionEvent.ACTION_DOWN:
            mLastMotionY = event.getY();
            mIsHandledTouchEvent = false;
            break;

        case MotionEvent.ACTION_MOVE:
            final float deltaY = event.getY() - mLastMotionY;
            final float absDiff = Math.abs(deltaY);
            // 这里有三个条件：
            // 1，位移差大于mTouchSlop，这是为了防止细微滑动，而此时用户是本不想滑动的误差
            // 2，isPullRefreshing()，如果当前正在下拉刷新的话，是允许向上滑动，并把刷新的HeaderView挤上去
            // 3，isPullLoading()，如果正在上拉加载更多，是允许向下滑动，并把刷新的footer挤下去
            if (absDiff > mTouchSlop || isPullRefreshing() || isPullLoading()) {
                mLastMotionY = event.getY();
                // 第一个显示出来，Header已经显示或拉下
                if (isPullRefreshEnabled() && isReadyForPullDown()) {
                    // 1，Math.abs(getScrollY()) > 0：表示当前滑动的偏移量的绝对值大于0，表示当前HeaderView滑出来了或完全
                    // 不可见，存在这样一种case，当正在刷新时并且RefreshableView已经滑到顶部，向上滑动，那么我们期望的结果是
                    // 依然能向上滑动，直到HeaderView完全不可见
                    // 2，deltaY > 0.5f：表示下拉的值大于0.5f
                    mIsHandledTouchEvent = (Math.abs(getScrollYValue()) > 0 || deltaY > 0.5f);
                    // 如果截断事件，我们则仍然把这个事件交给刷新View去处理，典型的情况是让ListView/GridView将按下
                    // Child的Selector隐藏，防止事件冲突，因为当mIsHandledTouchEvent返回true时系统不会再把事件传递给子控件了
                    if (mIsHandledTouchEvent) {
                        mRefreshableView.onTouchEvent(event);
                    }
                }
                else if (isPullLoadEnabled() && isReadyForPullUp()) {
                    // 原理如上
                    mIsHandledTouchEvent = (Math.abs(getScrollYValue()) > 0 || deltaY < -0.5f);
                }
            }
            break;
        default:
            break;
        }

        return mIsHandledTouchEvent;
    }

    @Override
    public final boolean onTouchEvent(MotionEvent ev) {
        boolean handled = false;
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mLastMotionY = ev.getY();
            mIsHandledTouchEvent = false;
            break;

        case MotionEvent.ACTION_MOVE:
            final float deltaY = ev.getY() - mLastMotionY;
            mLastMotionY = ev.getY();
            if (isPullRefreshEnabled() && isReadyForPullDown()) {
                pullHeaderLayout(deltaY / OFFSET_RADIO);
                handled = true;
            }
            else if (isPullLoadEnabled() && isReadyForPullUp()) {
                pullFooterLayout(deltaY / OFFSET_RADIO);
                handled = true;
            }
            else {
                mIsHandledTouchEvent = false;
            }
            break;

        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            if (mIsHandledTouchEvent) {
                mIsHandledTouchEvent = false;
                // 当第一个显示出来时
                if (isReadyForPullDown()) {
                    if (mPullRefreshEnabled && (mPullDownState == State.RELEASE_TO_REFRESH)) {
                        // 下拉到一定程度触发的
                        startRefreshing();
                        handled = true;
                    }
                    resetHeaderLayout();
                }
                else if (isReadyForPullUp()) {
                    if (isPullLoadEnabled() && (mPullUpState == State.RELEASE_TO_REFRESH)) {
                        // 上拉到一定程度触发的
                        startLoading();
                        handled = true;
                    }
                    resetFooterLayout();
                }
            }
            break;
        default:
            break;
        }

        return handled;
    }

    /**
     * 开始刷新，通常用于调用者主动刷新，典型的情况是进入界面，开始主动刷新，这个刷新并不是由用户拉动引起的
     * 
     * @param smoothScroll
     *            表示是否有平滑滚动，true表示平滑滚动，false表示无平滑滚动
     * @param delayMillis
     *            延迟时间
     */
    public void doPullRefreshing(final boolean smoothScroll, final long delayMillis) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                int newScrollValue = -mHeaderHeight;
                int duration = smoothScroll ? SCROLL_DURATION : 0;

                startRefreshing();
                smoothScrollTo(newScrollValue, duration, 0);
            }
        }, delayMillis);
    }

    /**
     * 得到平滑滚动的时间，派生类可以重写这个方法来控件滚动时间
     * 
     * @return 返回值时间为毫秒
     */
    protected long getSmoothScrollDuration() {
        return SCROLL_DURATION;
    }

    // //////////////////////////////////////////创建头部或者尾部方法///////////////////////////////////////////
    /**
     * 创建Header的布局
     * 
     * @param context
     *            context
     * @param attrs
     *            属性
     * @return LoadingLayout对象
     */
    protected AbstractLoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new HeaderLoadingLayout(context);
    }

    /**
     * 创建Footer的布局
     * 
     * @param context
     *            context
     * @param attrs
     *            属性
     * @return LoadingLayout对象
     */
    protected AbstractLoadingLayout createFooterLoadingLayout(Context context, AttributeSet attrs) {
        return new FooterLoadingLayout(context);
    }

    // ///////////////////////////////调整布局方法////////////////////////////////////////////////////
    /**
     * 主要设置padding的top值和bottom值，这样可使头部和尾部布局处于不可见状态
     */
    private void refreshLoadingViewsSize() {
        mHeaderHeight = (null != mHeaderLayout) ? mHeaderLayout.getMeasuredHeight() : 0;
        mFooterHeight = (null != mFooterLayout) ? mFooterLayout.getMeasuredHeight() : 0;
        setPadding(getPaddingLeft(), getPaddingTop() - mHeaderHeight, getPaddingRight(), getPaddingBottom()
                - mFooterHeight);
    }

    /**
     * 重新设置刷新包装器的高度，使其到正常高度<br>
     * 因为在addRefreshableView方法中我们为了确保footer的高度测量正常，而把包装器的高度设置了一个很小的值
     * 
     * @param width
     *            当前容器的宽度
     * @param height
     *            当前容器的宽度
     */
    protected void refreshRefreshableViewSize(int width, int height) {
        if (null != mRefreshableViewWrapper) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mRefreshableViewWrapper.getLayoutParams();
            if (lp.height != height) {
                lp.height = height;
                mRefreshableViewWrapper.requestLayout();
            }
        }
    }

    // //////////////////////////添加头部，尾部，中间View方法//////////////////////////////////////////////////
    /**
     * 将刷新View添加到当前容器中
     * 
     * @param context
     *            context
     * @param refreshableView
     *            可以刷新的View
     */
    protected void addRefreshableView(Context context, T refreshableView) {
        // 创建一个包装容器，并把刷新容器放入包装容器中去
        mRefreshableViewWrapper = new FrameLayout(context);
        mRefreshableViewWrapper.addView(refreshableView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        // 这里把包装器的高度设置为一个很小的值，它的高度最终会在onSizeChanged()方法中设置为MATCH_PARENT
        // 这样做的原因是，如果此是它的height是MATCH_PARENT，那么在测量footer时，footer得到的高度就是0，所以，我们先设置高度很小
        // 我们就可以得到footer的正常高度，当onSizeChanged后，包装器的高度又会变为正常。
        addView(mRefreshableViewWrapper, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
    }

    /**
     * 添加Header和Footer布局
     * 
     * @param context
     *            context
     */
    protected void addHeaderAndFooter(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mHeaderLayout, 0, params);
        addView(mFooterLayout, -1, params);
    }

    // ///////////////////////////////拉动头部或者尾部方法////////////////////////////////////////////////////////
    /**
     * 拉动Header Layout时调用
     * 
     * @param delta
     *            移动的距离
     */
    protected void pullHeaderLayout(float delta) {
        // 向上滑动，并且当前scrollY为0时，不滑动
        int oldScrollY = getScrollYValue();
        if (delta < 0 && (oldScrollY - delta) >= 0) {
            setScrollTo(0, 0);
            return;
        }

        // 向下滑动布局
        setScrollBy(0, -(int) delta);

        if (null != mHeaderLayout && 0 != mHeaderHeight) {
            float scale = Math.abs(getScrollYValue()) / (float) mHeaderHeight;
            mHeaderLayout.onPull(scale);
        }

        // 未处于刷新状态，更新箭头
        int scrollY = Math.abs(getScrollYValue());
        if (isPullRefreshEnabled() && !isPullRefreshing()) {
            if (scrollY > mHeaderHeight) {
                mPullDownState = State.RELEASE_TO_REFRESH;
            }
            else {
                mPullDownState = State.PULL_TO_REFRESH;
            }

            mHeaderLayout.setState(mPullDownState);
            onStateChanged(mPullDownState, true);
        }
    }

    /**
     * 拉Footer时调用
     * 
     * @param delta
     *            移动的距离
     */
    protected void pullFooterLayout(float delta) {
        int oldScrollY = getScrollYValue();
        if (delta > 0 && (oldScrollY - delta) <= 0) {
            setScrollTo(0, 0);
            return;
        }

        setScrollBy(0, -(int) delta);

        if (null != mFooterLayout && 0 != mFooterHeight) {
            float scale = Math.abs(getScrollYValue()) / (float) mFooterHeight;
            mFooterLayout.onPull(scale);
        }

        int scrollY = Math.abs(getScrollYValue());
        if (isPullLoadEnabled() && !isPullLoading()) {
            if (scrollY > mFooterHeight) {
                mPullUpState = State.RELEASE_TO_REFRESH;
            }
            else {
                mPullUpState = State.PULL_TO_REFRESH;
            }

            mFooterLayout.setState(mPullUpState);
            onStateChanged(mPullUpState, false);
        }
    }

    // ///////////////////////////////////////设置头部或者尾部方法///////////////////////////////////////////

    /**
     * 重置header,规则是：<br>
     * 如果不是正在加载中状态，header设置到不可见<br>
     * 如果是正在加载状态：（1）小于等于header高设置header不可见，（2）Y偏移大于header高显示header可见
     */
    protected void resetHeaderLayout() {
        final int scrollY = Math.abs(getScrollYValue());
        final boolean refreshing = isPullRefreshing();

        if (!refreshing) {
            smoothScrollTo(0);
        }
        else {
            if (scrollY <= mHeaderHeight) {
                smoothScrollTo(0);
            }
            else {
                smoothScrollTo(-mHeaderHeight);
            }
        }
    }

    /**
     * 重置footer,规则是：<br>
     * 如果不是正在加载中状态，footer设置到不可见<br>
     * 如果是正在加载状态：（1）小于等于footer高设置footer不可见，（2）Y偏移大于footer高显示footer可见
     */
    protected void resetFooterLayout() {
        int scrollY = Math.abs(getScrollYValue());
        boolean isPullLoading = isPullLoading();

        if (!isPullLoading) {
            smoothScrollTo(0);
        }
        else {
            if (scrollY <= mFooterHeight) {
                smoothScrollTo(0);
            }
            else {
                smoothScrollTo(mFooterHeight);
            }
        }
    }

    // ////////////////////////////////////////////判断是否在刷新或者加载更多方法//////////////////////////////////

    /**
     * 判断是否正在下拉刷新
     * 
     * @return true正在刷新，否则false
     */
    protected boolean isPullRefreshing() {
        return (mPullDownState == State.REFRESHING);
    }

    /**
     * 是否正的上拉加载更多
     * 
     * @return true正在加载更多，否则false
     */
    protected boolean isPullLoading() {
        return (mPullUpState == State.REFRESHING);
    }

    // /////////////////////////////////////////开始刷新或者加载更多方法///////////////////////////////////////////
    /**
     * 开始刷新，当下拉松开后被调用
     */
    protected void startRefreshing() {
        // 如果正在刷新
        if (isPullRefreshing()) {
            return;
        }

        mPullDownState = State.REFRESHING;
        onStateChanged(State.REFRESHING, true);

        if (null != mHeaderLayout) {
            mHeaderLayout.setState(State.REFRESHING);
        }

        if (null != mRefreshListener) {
            // 因为滚动到回原始位置需要一定时间，所以我们需要等回滚完后才执行刷新回调
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshListener.onPullDownToRefresh(PullToRefreshBase.this);
                }
            }, getSmoothScrollDuration());
        }
    }

    /**
     * 开始加载更多，上拉松开后调用
     */
    protected void startLoading() {
        // 如果正在加载
        if (isPullLoading()) {
            return;
        }

        mPullUpState = State.REFRESHING;
        onStateChanged(State.REFRESHING, false);

        if (null != mFooterLayout) {
            mFooterLayout.setState(State.REFRESHING);
        }

        if (null != mRefreshListener) {
            // 因为滚动回原始位置的时间是200，我们需要等回滚完后才执行加载回调
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshListener.onPullUpToRefresh(PullToRefreshBase.this);
                }
            }, getSmoothScrollDuration());
        }
    }

    /**
     * 得到当前Y的滚动值
     * 
     * @return 滚动值
     */
    private int getScrollYValue() {
        return getScrollY();
    }

    // /////////////////////////////////////在Y轴方向滚动///////////////////////////////////////////////////
    /**
     * 设置滚动位置
     * 
     * @param x
     *            滚动到的x位置
     * @param y
     *            滚动到的y位置
     */
    private void setScrollTo(int x, int y) {
        scrollTo(x, y);
    }

    /**
     * 设置滚动的偏移
     * 
     * @param x
     *            滚动x位置
     * @param y
     *            滚动y位置
     */
    private void setScrollBy(int x, int y) {
        scrollBy(x, y);
    }

    /**
     * 平滑滚动到指定位置，不进行延时
     * 
     * @param newScrollValue
     *            滚动的值
     */
    private void smoothScrollTo(int newScrollValue) {
        smoothScrollTo(newScrollValue, getSmoothScrollDuration(), 0);
    }

    /**
     * 平滑滚动到到指定位置
     * 
     * @param newScrollValue
     *            滚动的值
     * @param duration
     *            滚动时候
     * @param delayMillis
     *            延迟时间，0代表不延迟
     */
    private void smoothScrollTo(int newScrollValue, long duration, long delayMillis) {
        if (null != mSmoothScrollRunnable) {
            mSmoothScrollRunnable.stop();
        }

        int oldScrollValue = this.getScrollYValue();
        if (oldScrollValue != newScrollValue) {
            mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration);

            if (delayMillis > 0) {
                postDelayed(mSmoothScrollRunnable, delayMillis);
            }
            else {
                post(mSmoothScrollRunnable);
            }
        }
    }

    // /////////////////////////////////设置是否截断touch事件/////////////////////////////////////////
    /**
     * 设置是否截断touch事件
     * 
     * @param enabled
     *            true截断，false不截断
     */
    private void setInterceptTouchEventEnabled(boolean enabled) {
        mInterceptEventEnable = enabled;
    }

    /**
     * 标志是否截断touch事件
     * 
     * @return true截断，false不截断
     */
    private boolean isInterceptTouchEventEnabled() {
        return mInterceptEventEnable;
    }

    // ////////////////////////////////////////////内部类以及接口/////////////////////////////////////////////////
    /**
     * 实现了平滑滚动的Runnable
     * 
     * @author Li Hong
     * @since 2013-8-22
     */
    final class SmoothScrollRunnable implements Runnable {
        /** 动画效果 */
        private final Interpolator mInterpolator;
        /** 结束Y */
        private final int mScrollToY;
        /** 开始Y */
        private final int mScrollFromY;
        /** 滑动时间 */
        private final long mDuration;
        /** 是否继续运行 */
        private boolean mContinueRunning = true;
        /** 开始时刻 */
        private long mStartTime = -1;
        /** 当前Y */
        private int mCurrentY = -1;

        /**
         * 构造方法
         * 
         * @param fromY
         *            开始Y
         * @param toY
         *            结束Y
         * @param duration
         *            动画时间
         */
        public SmoothScrollRunnable(int fromY, int toY, long duration) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mDuration = duration;
            mInterpolator = new DecelerateInterpolator();
        }

        @Override
        public void run() {
            /**
             * 如果间隔时间小于0，我们就直接定位到目的地
             */
            if (mDuration <= 0) {
                setScrollTo(0, mScrollToY);
                return;
            }

            /**
             * 这个任务类第一次执行时，先设置开始时间为当前时间，之后会开始计算Y轴需要滑动的距离
             */
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            }
            else {

                /**
                 * We do do all calculations in long to reduce software float calculations. We use 1000 as it gives us
                 * good accuracy and small rounding errors
                 */
                final long oneSecond = 1000; // SUPPRESS CHECKSTYLE
                long normalizedTime = (oneSecond * (System.currentTimeMillis() - mStartTime)) / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, oneSecond), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator.getInterpolation(normalizedTime / (float) oneSecond));
                mCurrentY = mScrollFromY - deltaY;

                setScrollTo(0, mCurrentY);
            }

            // 如果当前的Y轴位置还没有达到预定位置，继续post该任务到UI线程执行，达到持续滑动中...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                PullToRefreshBase.this.postDelayed(this, 16);// SUPPRESS CHECKSTYLE
            }
        }

        /**
         * 停止滑动
         */
        public void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }

    // /////////////////////////////////IPullToRefresh实现////////////////////////////////////////////////////////
    @Override
    public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
        mPullRefreshEnabled = pullRefreshEnabled;
    }

    @Override
    public void setPullLoadEnabled(boolean pullLoadEnabled) {
        mPullLoadEnabled = pullLoadEnabled;
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        mScrollLoadEnabled = scrollLoadEnabled;
    }

    @Override
    public boolean isPullRefreshEnabled() {
        return mPullRefreshEnabled && (null != mHeaderLayout);
    }

    @Override
    public boolean isPullLoadEnabled() {
        return mPullLoadEnabled && (null != mFooterLayout);
    }

    @Override
    public boolean isScrollLoadEnabled() {
        return mScrollLoadEnabled;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener<T> refreshListener) {
        mRefreshListener = refreshListener;
    }

    @Override
    public void onPullDownRefreshComplete() {
        if (isPullRefreshing()) {
            mPullDownState = State.RESET;
            onStateChanged(State.RESET, true);

            resetHeaderLayout();
            setInterceptTouchEventEnabled(false);

            // 回滚到正常状态有个时间间隔，在这个过程中我们要禁止进入拦截事件方法
            // 因为如果不禁止，那么在回滚的过程中如果用户又发起刷新操作，而这时上一个回滚完成时会把状态设置成RESET
            // 那么后面那次的刷新操作的状态就会被污染，我们不希望这样的
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setInterceptTouchEventEnabled(true);
                    mHeaderLayout.setState(State.RESET);
                }
            }, getSmoothScrollDuration());
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        if (isPullLoading()) {
            mPullUpState = State.RESET;
            onStateChanged(State.RESET, false);

            resetFooterLayout();
            setInterceptTouchEventEnabled(false);

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setInterceptTouchEventEnabled(true);
                    mFooterLayout.setState(State.RESET);
                }
            }, getSmoothScrollDuration());
        }
    }

    @Override
    public T getRefreshableView() {
        return mRefreshableView;
    }

    @Override
    public AbstractLoadingLayout getHeaderLoadingLayout() {
        return mHeaderLayout;
    }

    @Override
    public AbstractLoadingLayout getFooterLoadingLayout() {
        return mFooterLayout;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        if (null != mHeaderLayout) {
            mHeaderLayout.setLastUpdatedLabel(label);
        }

        if (null != mFooterLayout) {
            mFooterLayout.setLastUpdatedLabel(label);
        }
    }

    // //////////////////////////////////子类可继承方法//////////////////////////////////////////////////////
    /**
     * 当状态发生变化时调用
     * 
     * @param state
     *            状态
     * @param isPullDown
     *            是否是下拉刷新
     */
    protected void onStateChanged(State state, boolean isPullDown) {
    }

    // ////////////////////////////////子类必须继承方法/////////////////////////////////////////////////////
    /**
     * 创建可以刷新的View
     * 
     * @param context
     *            context
     * @param attrs
     *            属性
     * @return View
     */
    protected abstract T createRefreshableView(Context context, AttributeSet attrs);

    /**
     * 判断刷新的View是否滑动到顶部
     * 
     * @return true表示已经滑动到顶部，否则false
     */
    protected abstract boolean isReadyForPullDown();

    /**
     * 判断刷新的View是否滑动到底
     * 
     * @return true表示已经滑动到底部，否则false
     */
    protected abstract boolean isReadyForPullUp();

    /**
     * 定义了下拉刷新和上拉加载更多的接口
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-11-20 上午10:35:48 $
     */
    public interface OnRefreshListener<V extends View> {

        /**
         * 下拉松手后会被调用
         * 
         * @param refreshView
         *            刷新的View
         */
        void onPullDownToRefresh(final PullToRefreshBase<V> refreshView);

        /**
         * 加载更多时会被调用或上拉时调用
         * 
         * @param refreshView
         *            刷新的View
         */
        void onPullUpToRefresh(final PullToRefreshBase<V> refreshView);
    }

}
