package com.dazzle.bigappleui.pullrefresh.core;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazzle.bigappleui.R;

/**
 * 这个类封装了下拉刷新的布局，有些低版本的手机不支持，慎用
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:34:15 $
 */
public class RotateLoadingLayout extends LoadingLayout {
    /** 旋转动画的时间 */
    static final int ROTATION_ANIMATION_DURATION = 1200;
    /** 动画插值 */
    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    /** Header的容器 */
    private RelativeLayout mHeaderContainer;
    /** 箭头图片 */
    private ImageView mArrowImageView;
    /** 状态提示TextView */
    private TextView mHintTextView;
    /** 最后更新时间的TextView */
    private TextView mHeaderTimeView;
    /** 最后更新时间的标题 */
    private TextView mHeaderTimeViewTitle;
    /** 旋转的动画 */
    private Animation mRotateAnimation;

    /**
     * 构造方法
     * 
     * @param context
     *            context
     */
    public RotateLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     * 
     * @param context
     *            context
     * @param attrs
     *            attrs
     */
    public RotateLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     * 
     * @param context
     *            context
     */
    private void init(Context context) {
        mHeaderContainer = (RelativeLayout) findViewById(R.id.pull_to_refresh_header_content);
        mArrowImageView = (ImageView) findViewById(R.id.pull_to_refresh_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.pull_to_refresh_header_hint_textview);
        mHeaderTimeView = (TextView) findViewById(R.id.pull_to_refresh_header_time);
        mHeaderTimeViewTitle = (TextView) findViewById(R.id.pull_to_refresh_last_update_time_text);

        mArrowImageView.setScaleType(ScaleType.CENTER);
        mArrowImageView.setImageResource(R.drawable.default_ptr_rotate);

        float pivotValue = 0.5f; // SUPPRESS CHECKSTYLE
        float toDegree = 720.0f; // SUPPRESS CHECKSTYLE
        mRotateAnimation = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateAnimation.setFillAfter(true);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, null);
        return container;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        // 如果最后更新的时间的文本是空的话，隐藏前面的标题
        mHeaderTimeViewTitle.setVisibility(TextUtils.isEmpty(label) ? View.INVISIBLE : View.VISIBLE);
        mHeaderTimeView.setText(label);
    }

    @Override
    public int getContentSize() {
        if (null != mHeaderContainer) {
            return mHeaderContainer.getHeight();
        }

        return (int) (getResources().getDisplayMetrics().density * 60);
    }

    @Override
    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        resetRotation();
        mHintTextView.setText("下拉可以刷新");
    }

    @Override
    protected void onReleaseToRefresh() {
        mHintTextView.setText("松开后刷新");
    }

    @Override
    protected void onPullToRefresh() {
        mHintTextView.setText("下拉可以刷新");
    }

    @Override
    protected void onRefreshing() {
        resetRotation();
        mArrowImageView.startAnimation(mRotateAnimation);
        mHintTextView.setText("正在加载中");
    }

    @Override
    public void onPull(float scale) {
        float angle = scale * 180f; // SUPPRESS CHECKSTYLE
        mArrowImageView.setRotation(angle);
    }

    /**
     * 重置动画
     */
    private void resetRotation() {
        mArrowImageView.clearAnimation();
        mArrowImageView.setRotation(0);
    }

}
