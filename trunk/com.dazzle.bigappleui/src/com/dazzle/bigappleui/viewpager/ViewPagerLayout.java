package com.dazzle.bigappleui.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.dazzle.bigappleui.viewpager.event.OnScrollCompleteListener;
import com.dazzle.bigappleui.viewpager.event.ScrollEvent;
import com.dazzle.bigappleui.viewpager.event.ScrollEventAdapter;

/**
 * 模仿launcher左右滑动的布局控件哦，亲，很好用的，有点想viewPager这个玩样，but，变种过了，时其左右会显示一部分，当前比较流行的显示图片的方式
 * 
 * @author xuan
 */
@Deprecated
public class ViewPagerLayout extends ViewGroup {
    private static final int TOUCH_STATE_REST = 0;// 空闲状态
    private static final int TOUCH_STATE_SCROLLING = 1;// 正在滚动状态
    private int touchState = TOUCH_STATE_REST;// 触摸滚动状态

    private final ScrollEventAdapter scrollEventAdapter;// 监听事件集合

    private final Scroller scroller;// 使切view的时候效果比较平滑
    private VelocityTracker velocityTracker;// 计算手势的一些速率等的工具类

    private int curScreen;// 记录当前屏幕的位置，从0开始
    private final int defaultScreen = 0;// 默认从0开始

    private static final int SNAP_VELOCITY = 600;// 速率

    private final int touchSlop;// 触发后小于改距离的，不移动
    private float lastMotionX;// 记录最后一次x坐标值
    private int offsetSize = 0;// 切换的偏移量

    public ViewPagerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        scrollEventAdapter = new ScrollEventAdapter();
        scroller = new Scroller(context);
        curScreen = defaultScreen;
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);

        // 注释部分是限制布局高宽的类型，不明白什么道理，先注释掉，求高人指点
        // final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        // if (widthMode != MeasureSpec.EXACTLY) {
        // // 控件的宽模式必须是设置成500dp或者FILL_PARENT这样大小已经决定的参数
        // // throw new IllegalStateException("ViewPagerLayout width only can run at EXACTLY mode!");
        // }
        //
        // final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // if (heightMode != MeasureSpec.EXACTLY) {
        // // 控件的高模式必须是设置成500dp或者FILL_PARENT这样大小已经决定的参数
        // // 高和宽同时设置成dp，高就不是EXACTLY了，求高人指点？？？
        // // throw new IllegalStateException("ViewPagerLayout heigh only can run at EXACTLY mode!");
        // }

        // 给他的子View设置同样的高和宽
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }

        scrollTo(curScreen * width, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 每次第一个偏移一下
        int childLeft = offsetSize;

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                // 实际孩子的宽要偏移一下，因为childView.getMeasuredWidth()拿到的布局的宽
                int childWidth = childView.getMeasuredWidth() - 2 * offsetSize;
                childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());

                // 下一次距离父亲左边的距离=上一次距离父亲右边的距离+自身的宽
                childLeft = childLeft + childWidth;
            }
        }
    }

    /**
     * 更具当前停留的位置，决定跳去那边
     */
    public void snapToDestination() {
        int getWidth = getWidth() - 2 * offsetSize;// 计算偏移

        final int screenWidth = getWidth;
        final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
        snapToScreen(destScreen);
    }

    /**
     * 平滑的切换到指定屏幕
     * 
     * @param whichScreen
     */
    public void snapToScreen(int whichScreen) {
        int getWidth = getWidth() - 2 * offsetSize;// 计算偏移

        // 保证号在[0,getChildCount() - 1]范围内
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

        // 如果没有滚动到位，就滚动余下的距离
        if (getScrollX() != (whichScreen * getWidth)) {
            // int delta = whichScreen * getWidth() - getScrollX();

            // 妈的算死我了,后来才TM明白现在子view的宽=getWidth() - 2 *
            // offsetSize,这样就好了么，原来子view=getWidth()这样的
            int delta = whichScreen * getWidth - getScrollX();

            // 从(getScrollX(),0)-->x滚动delta，y滚动0，到达指定位置的时间（以毫秒为单位）
            scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);

            if (curScreen != whichScreen) {
                ScrollEvent scrollEvent = new ScrollEvent(whichScreen);
                scrollEventAdapter.notifyEvent(scrollEvent);
            }
            curScreen = whichScreen;

            // 重绘布局
            invalidate();
        }
    }

    /**
     * 直接切换到指定屏幕
     * 
     * @param whichScreen
     */
    public void setToScreen(int whichScreen) {
        int getWidth = getWidth() - 2 * offsetSize;// 计算偏移

        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        curScreen = whichScreen;
        scrollTo(whichScreen * getWidth, 0);
        ScrollEvent scrollEvent = new ScrollEvent(whichScreen);
        scrollEventAdapter.notifyEvent(scrollEvent);
        invalidate();
    }

    public int getCurScreen() {
        return curScreen;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        final int action = event.getAction();
        final float x = event.getX();

        switch (action) {
        case MotionEvent.ACTION_DOWN:
            if (!scroller.isFinished()) {
                scroller.abortAnimation();
            }
            lastMotionX = x;
            break;
        case MotionEvent.ACTION_MOVE:
            int deltaX = (int) (lastMotionX - x);
            lastMotionX = x;
            scrollBy(deltaX, 0);
            break;
        case MotionEvent.ACTION_UP:
            final VelocityTracker vt = velocityTracker;
            vt.computeCurrentVelocity(1000);
            int velocityX = (int) vt.getXVelocity();

            if (velocityX > SNAP_VELOCITY && curScreen > 0) {
                // 手势结束转到靠左的界面
                snapToScreen(curScreen - 1);
            }
            else if (velocityX < -SNAP_VELOCITY && curScreen < getChildCount() - 1) {
                // 手势结束转到靠右的界面
                snapToScreen(curScreen + 1);
            }
            else {
                snapToDestination();
            }

            if (velocityTracker != null) {
                velocityTracker.recycle();
                velocityTracker = null;
            }
            touchState = TOUCH_STATE_REST;
            break;
        case MotionEvent.ACTION_CANCEL:
            touchState = TOUCH_STATE_REST;
            break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        // 如果事件是正在移动，且触发状态不在空闲状态，就拦截事件，不让事件往子控件传递
        if ((action == MotionEvent.ACTION_MOVE) && (touchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();
        switch (action) {
        case MotionEvent.ACTION_MOVE:
            final int xDiff = (int) Math.abs(lastMotionX - x);
            if (xDiff > touchSlop) {
                touchState = TOUCH_STATE_SCROLLING;
            }
            break;
        case MotionEvent.ACTION_DOWN:
            postInvalidate();
            lastMotionX = x;
            touchState = scroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            postInvalidate();
            touchState = TOUCH_STATE_REST;
            break;
        }

        return touchState != TOUCH_STATE_REST;
    }

    /**
     * 设置滚动后屏幕变化的监听事件
     * 
     * @param listener
     */
    @Deprecated
    public void setOnScrollCompleteLinstenner(OnScrollCompleteListener listener) {
        scrollEventAdapter.addListener(listener);
    }

    /**
     * 添加滚动后屏幕变化的监听事件
     * 
     * @param listener
     */
    public void addOnScrollCompleteLinstenner(OnScrollCompleteListener listener) {
        scrollEventAdapter.addListener(listener);
    }

    public int getOffsetSize() {
        return offsetSize;
    }

    public void setOffsetSize(int offsetSize) {
        this.offsetSize = offsetSize;
    }

}
