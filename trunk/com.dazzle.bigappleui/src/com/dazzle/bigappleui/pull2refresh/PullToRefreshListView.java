package com.dazzle.bigappleui.pull2refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dazzle.bigappleui.utils.M;

/**
 * 下拉刷新控件，使用时，需要引入<br>
 * 一个箭头图片：pull2refresh_arrow.png<br>
 * 一个头部布局文件：pull2refresh_footer.xml<br>
 * 一个尾部布局文件：pull2refresh_head.xml（这个不是必须的，只有想用尾部刷新时，引入即可）<br>
 * 请使用：pullrefresh包下的widget中的PullToRefreshListView，那个效果更好，扩展性也不错
 * 
 * @author xuan
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {
    private static final String TAG = "PullToRefreshListView";
    public static final boolean DEBUG = false;

    // 表示着列表正处于哪种状态
    private final static int PULL_TO_REFRESH = 0;// 下拉刷新标志
    private final static int RELEASE_TO_REFRESH = 1;// 松开刷新标志
    private final static int REFRESHING = 2;// 正在刷新标志
    private final static int DONE = 3;// 刷新完成标志
    private int state;

    // 头部布局的一些对象
    private LinearLayout headView;// 头部的布局对象
    private TextView tipsTextview;// 下拉时的文字提醒文本
    private TextView lastUpdatedTextView;// 最后一次更新的文本提示
    private ImageView arrowImageView;// 箭头图标对象
    private ProgressBar progressBar;// 加载圈对象

    // 用来设置箭头图标动画效果
    private RotateAnimation animation;// 箭头由下到上
    private RotateAnimation reverseAnimation;// 箭头有上到下恢复

    // 记录状态切换时的一些标记
    private boolean isRecored;// 表示是否正在记录startY，用于保证startY的值在一个完整的touch事件中只被记录一次
    private int startY;// 移动开始时记录的Y高度

    private boolean isBack;// 标志着那个刷新箭头是否需要设置翻转回去
    private int firstItemIndex;// 记录着第一个可见列表项目的标志，在滚动条监听事件中被设置
    private int currentScrollState;// 记录着当前滚动条的状态，在滚动条监听事件中被设置
    private int visibleItemCount;// 可见item数

    // 头部的一些测量距离参数
    private int headContentHeight;
    private int headContentOriginalTopPadding;

    // 触发刷新的接口
    public OnRefreshListener refreshListener;

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        // 设置箭头有下朝上的动画
        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(100);
        animation.setFillAfter(true);

        // 设置箭头由上朝下恢复的动画
        reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(100);
        reverseAnimation.setFillAfter(true);

        // 获取头部布局
        headView = (LinearLayout) LayoutInflater.from(context).inflate(M.layout(context, "pull2refresh_head"), null);
        arrowImageView = (ImageView) headView.findViewById(M.id(context, "pull2refresh_head_arrowimageview"));// 箭头图片
        arrowImageView.setMinimumWidth(50);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar) headView.findViewById(M.id(context, "pull2refresh_head_progressbar"));// 进度圈
        tipsTextview = (TextView) headView.findViewById(M.id(context, "pull2refresh_head_tipstextview"));// 提示文本
        lastUpdatedTextView = (TextView) headView.findViewById(M.id(context, "pull2refresh_head_lastupdatedtextview"));// 最后一次更新时间提醒

        // 头部的原始paddingTop值
        headContentOriginalTopPadding = headView.getPaddingTop();
        measureView(headView);

        // 隐藏头部
        headContentHeight = headView.getMeasuredHeight();
        headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(),
                headView.getPaddingBottom());
        headView.invalidate();

        addHeaderView(headView);
        setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
        this.firstItemIndex = firstVisiableItem;
        this.visibleItemCount = visibleItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.currentScrollState = scrollState;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (firstItemIndex == 0 && !isRecored) {
                startY = (int) event.getY();
                isRecored = true;
            }
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            if (DEBUG) {
                Log.d(TAG, "ACTION_UP:" + state);
            }

            // 当抬起时，如果是正在刷新就不管
            if (state != REFRESHING) {
                if (state == DONE) {
                    // 当抬起时，如果是DONE(已经完成状态)，就什么都不做
                }
                else if (state == PULL_TO_REFRESH) {
                    // 当抬起时，如果是PULL_To_REFRESH(下拉刷新提示)，就直接到DONE状态，只改变一下头就可以，不用去加载数据
                    state = DONE;
                    changeHeaderViewByState();
                }
                else if (state == RELEASE_TO_REFRESH) {
                    // 当抬起时，如果是RELEASE_To_REFRESH(松开刷新提示)，就到REFRESHING(刷新中)状态，改变头，并触发刷新事件
                    state = REFRESHING;
                    changeHeaderViewByState();
                    onRefresh();
                }
            }

            isRecored = false;
            isBack = false;
            break;
        case MotionEvent.ACTION_MOVE:
            if (DEBUG) {
                Log.d(TAG, "ACTION_MOVE:" + state);
            }

            int tempY = (int) event.getY();
            if (!isRecored && firstItemIndex == 0) {
                isRecored = true;
                startY = tempY;
            }

            // 如果不是正在刷新，并正在记录startY状态，就要执行下面一系列的变化
            if (state != REFRESHING && isRecored) {
                // 滑动时RELEASE_TO_REFRESH状态下，转变有3种情况：1.上推又到PULL_TO_REFRESH（下拉刷新）状态 2.上推回到done状态
                // 3.一直往下拉不用管
                if (state == RELEASE_TO_REFRESH) {
                    if ((tempY - startY < headContentHeight + 20) && (tempY - startY) > 0) {
                        state = PULL_TO_REFRESH;
                        changeHeaderViewByState();
                    }
                    else if (tempY - startY <= 0) {
                        state = DONE;
                        changeHeaderViewByState();
                    }
                    else {
                    }
                }

                // 滑动时PULL_TO_REFRESH状态下，转变有两种情况：1.下拉到RELEASE_TO_REFRESH（松开刷新）状态 2.上推回到done状态
                else if (state == PULL_TO_REFRESH) {
                    // if (tempY - startY >= headContentHeight + 20 && currentScrollState == SCROLL_STATE_TOUCH_SCROLL)
                    // {
                    if (tempY - startY >= headContentHeight + 20) {
                        state = RELEASE_TO_REFRESH;
                        isBack = true;
                        changeHeaderViewByState();
                    }
                    else if (tempY - startY <= 0) {
                        state = DONE;
                        changeHeaderViewByState();
                    }
                }

                // 滑动时DONE状态下，转变只有一种情况：1.下拉到PULL_TO_REFRESH（下拉刷新）状态
                else if (state == DONE) {
                    if (tempY - startY > 0) {
                        state = PULL_TO_REFRESH;
                        changeHeaderViewByState();
                    }
                }

                // PULL_TO_REFRESH状态下,需要根据下拉距离实时更新头部的Padding距离
                if (state == PULL_TO_REFRESH) {
                    int topPadding = ((-1 * headContentHeight + (tempY - startY)));
                    headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(),
                            headView.getPaddingBottom());
                    headView.invalidate();
                }

                // RELEASE_TO_REFRESH状态下，需要根据下拉距离实时更新头部的Padding距离
                if (state == RELEASE_TO_REFRESH) {
                    int topPadding = ((tempY - startY - headContentHeight));
                    headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(),
                            headView.getPaddingBottom());
                    headView.invalidate();
                }
            }
            break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 点击刷新，这种情况就是供，列表外部有个按钮，然后点击就可以刷新列表
     */
    public void clickRefresh() {
        setSelection(0);
        state = REFRESHING;
        changeHeaderViewByState();
        onRefresh();
    }

    /**
     * 设置刷新监听器
     * 
     * @param refreshListener
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    /**
     * 当数据加载完成后，调用该方法可以手动设置列表为done状态
     * 
     * @param update
     */
    public void onRefreshComplete(String update) {
        lastUpdatedTextView.setText(update);
        onRefreshComplete();
    }

    /**
     * 设置列表为done状态
     */
    public void onRefreshComplete() {
        state = DONE;
        changeHeaderViewByState();
    }

    // 触发刷新事件
    private void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    public int getVisibleItemCount() {
        return visibleItemCount;
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();

        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        }
        else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        child.measure(childWidthSpec, childHeightSpec);
    }

    // 当状态改变时候，调用该方法，以更新头部的显示界面
    private void changeHeaderViewByState() {
        switch (state) {
        case RELEASE_TO_REFRESH:
            // 松开可以刷新状态
            arrowImageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            tipsTextview.setVisibility(View.VISIBLE);
            lastUpdatedTextView.setVisibility(View.VISIBLE);

            arrowImageView.clearAnimation();
            arrowImageView.startAnimation(animation);

            tipsTextview.setText("松开可以刷新");
            break;
        case PULL_TO_REFRESH:
            // 下拉刷新状态
            progressBar.setVisibility(View.GONE);
            tipsTextview.setVisibility(View.VISIBLE);
            lastUpdatedTextView.setVisibility(View.VISIBLE);
            arrowImageView.clearAnimation();
            arrowImageView.setVisibility(View.VISIBLE);
            if (isBack) {
                // 把箭头设置回去
                isBack = false;
                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(reverseAnimation);
            }
            tipsTextview.setText("下拉可以刷新");
            break;
        case REFRESHING:
            // 正在刷新状态
            headView.setPadding(headView.getPaddingLeft(), headContentOriginalTopPadding, headView.getPaddingRight(),
                    headView.getPaddingBottom());
            headView.invalidate();

            progressBar.setVisibility(View.VISIBLE);
            arrowImageView.clearAnimation();
            arrowImageView.setVisibility(View.GONE);

            tipsTextview.setText("加载中...");
            lastUpdatedTextView.setVisibility(View.GONE);
            break;
        case DONE:
            // 完成状态，隐藏头部
            headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(),
                    headView.getPaddingBottom());
            headView.invalidate();

            progressBar.setVisibility(View.GONE);
            arrowImageView.clearAnimation();
            arrowImageView.setImageResource(M.drawable(getContext(), "pull2refresh_arrow"));

            tipsTextview.setText("下拉可以刷新");
            lastUpdatedTextView.setVisibility(View.VISIBLE);
            break;
        }
    }

    /**
     * 下拉刷新监听
     * 
     * @author xuan
     * 
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

}
