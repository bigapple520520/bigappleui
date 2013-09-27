package com.dazzle.bigappleui.pull2refresh;

import android.content.Context;
import android.util.AttributeSet;
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

import com.dazzle.bigappleui.utils.ResourceResidUtils;

/**
 * 下拉刷新控件，使用时，需要引入<br>
 * 一个箭头图片：pull2refresh_arrow.png<br>
 * 一个头部布局文件：pull2refresh_footer.xml<br>
 * 一个尾部布局文件：pull2refresh_head.xml（这个不是必须的，只有想用尾部刷新时，引入即可）
 * 
 * @author xuan
 */
public class Pull2RefreshListView extends ListView implements OnScrollListener {

    // 表示着列表正处于哪种状态
    private final static int PULL_To_REFRESH = 0;// 下拉刷新标志
    private final static int RELEASE_To_REFRESH = 1;// 松开刷新标志
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
    private boolean isBack;// 标志者那个刷新箭头是否需要设置翻转回去
    private int startY;// 移动开始时记录的Y高度
    private int firstItemIndex;// 记录着第一个可见列表项目的标志，在滚动条监听事件中被设置
    private int currentScrollState;// 记录着当前滚动条的状态，在滚动条监听事件中被设置

    // 头部的一些测量距离参数
    private int headContentHeight;
    private int headContentOriginalTopPadding;

    // 触发刷新的接口
    public OnRefreshListener refreshListener;

    public Pull2RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Pull2RefreshListView(Context context, AttributeSet attrs, int defStyle) {
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
        headView = (LinearLayout) LayoutInflater.from(context).inflate(
                ResourceResidUtils.getResidByLayoutName(context, "pull2refresh_head"), null);
        arrowImageView = (ImageView) headView.findViewById(ResourceResidUtils.getResidByIdName(context,
                "pull2refresh_head_arrowimageview"));// 箭头图片
        arrowImageView.setMinimumWidth(50);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar) headView.findViewById(ResourceResidUtils.getResidByIdName(context,
                "pull2refresh_head_progressbar"));// 进度圈
        tipsTextview = (TextView) headView.findViewById(ResourceResidUtils.getResidByIdName(context,
                "pull2refresh_head_tipstextview"));// 提示文本
        lastUpdatedTextView = (TextView) headView.findViewById(ResourceResidUtils.getResidByIdName(context,
                "pull2refresh_head_lastupdatedtextview"));// 最后一次更新时间提醒

        // 获取头部一开始的padding，下面隐藏头部要用的
        headContentOriginalTopPadding = headView.getPaddingTop();

        // 测量头部的一些参数
        measureView(headView);

        // 之所以取头部本身的高度，就是要正好隐藏头部（padding设置成负的自身高度就ok了）
        headContentHeight = headView.getMeasuredHeight();

        // 高度设置成负数的自身头部高度，相当于把自己隐藏了
        headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(),
                headView.getPaddingBottom());
        headView.invalidate();

        // 添加头部
        addHeaderView(headView);

        // 滚动条的监听事件对象设置成本身
        setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
        // 会记录第一个可见项目下标
        firstItemIndex = firstVisiableItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 会记录滚动条滚动的状态
        currentScrollState = scrollState;
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
            // 失去焦点&取消动作，注意没有break哦，就会执行下面MotionEvent.ACTION_UP状态的东西
        case MotionEvent.ACTION_UP:
            // 当抬起时，如果是正在刷新就不管
            if (state != REFRESHING) {
                if (state == DONE) {
                    // 当抬起时，如果是DONE(已经完成状态)，就什么都不做
                }
                else if (state == PULL_To_REFRESH) {
                    // 当抬起时，如果是PULL_To_REFRESH(下拉刷新提示)，就直接到DONE状态，只改变一下头就可以，不用去加载数据
                    state = DONE;
                    changeHeaderViewByState();
                }
                else if (state == RELEASE_To_REFRESH) {
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
            int tempY = (int) event.getY();
            if (!isRecored && firstItemIndex == 0) {
                // 滑动开始时，就标志正在记录滑动，并记录当时的Y坐标高度
                isRecored = true;
                startY = tempY;
            }

            // 如果不是正在刷新，并正在记录startY状态，就要执行下面一系列的变化
            if (state != REFRESHING && isRecored) {
                // RELEASE_To_REFRESH状态下，有3种情况：1.往上推又到下拉刷新状态 2.直接推到顶部变成done状态
                // 3.一直往下拉不用管
                if (state == RELEASE_To_REFRESH) {
                    if ((tempY - startY < headContentHeight + 20) && (tempY - startY) > 0) {
                        // 如果往上推了，间距小于头的高度了，那就重新回到，下拉刷新状态了
                        state = PULL_To_REFRESH;
                        changeHeaderViewByState();
                    }
                    else if (tempY - startY <= 0) {
                        // 直接推到顶部，那就直接到done状态就可以，不用进行刷新
                        state = DONE;
                        changeHeaderViewByState();
                    }
                    else {
                        // 一直往下拉，就不用管了，只用下面更新头步的paddingTop的值就行了
                    }
                }

                // PULL_To_REFRESH状态下，有两种情况，1.下拉到可以放开刷新状态 2.上推回到done状态
                else if (state == PULL_To_REFRESH) {
                    if (tempY - startY >= headContentHeight + 20 && currentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
                        // 下拉到可以进入RELEASE_TO_REFRESH的状态
                        state = RELEASE_To_REFRESH;
                        isBack = true;
                        changeHeaderViewByState();
                    }
                    else if (tempY - startY <= 0) {
                        // 上推到顶了，转成done状态
                        state = DONE;
                        changeHeaderViewByState();
                    }
                }

                // DONE状态下，如果发现向下移动了，就转到DONE--》PULL_To_REFRESH（由done状态转变到下拉刷新状态）
                else if (state == DONE) {
                    if (tempY - startY > 0) {
                        state = PULL_To_REFRESH;
                        changeHeaderViewByState();
                    }
                }

                // PULL_To_REFRESH状态下,需要根据下拉距离实时更新头部的Padding距离
                if (state == PULL_To_REFRESH) {
                    int topPadding = ((-1 * headContentHeight + (tempY - startY)));
                    headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(),
                            headView.getPaddingBottom());
                    headView.invalidate();
                }

                // RELEASE_To_REFRESH状态下，需要根据下拉距离实时更新头部的Padding距离
                if (state == RELEASE_To_REFRESH) {
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
     * 刷新监听器接口
     * 
     * @author xuan
     * 
     */
    public interface OnRefreshListener {
        public void onRefresh();
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

    // 计算headView的width及height值
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();

        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        case RELEASE_To_REFRESH:
            // 头部改成，松开可以刷新
            arrowImageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            tipsTextview.setVisibility(View.VISIBLE);
            lastUpdatedTextView.setVisibility(View.VISIBLE);

            arrowImageView.clearAnimation();
            arrowImageView.startAnimation(animation);

            tipsTextview.setText("松开可以刷新");
            break;
        case PULL_To_REFRESH:
            // 头部改成，下拉刷新
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
            // 头部改成，正在刷新
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
            // 头部改成，done状态，把头部隐藏了
            headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(),
                    headView.getPaddingBottom());
            headView.invalidate();

            progressBar.setVisibility(View.GONE);
            arrowImageView.clearAnimation();
            // 此处更换到箭头到正常朝上状态
            arrowImageView.setImageResource(ResourceResidUtils.getResidByDrawableName(getContext(),
                    "pull2refresh_arrow"));

            tipsTextview.setText("下拉可以刷新");
            lastUpdatedTextView.setVisibility(View.VISIBLE);
            break;
        }
    }

}
