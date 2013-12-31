package com.dazzle.bigappleui.view;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 侧滑删除view
 * 
 * @author xuan
 */
public class SwipeView extends FrameLayout {
    public static final int CURSCREEN_CONTENT = 0;// 当前界面
    public static final int CURSCREEN_BEHIND = 1;// 侧滑后出来的界面

    private static final int SNAP_VELOCITY = 600;// 单位px，每秒滑过的px距离

    private View mContent;
    private View mBehind;

    private int behindWidth = 150;// 偏移量单位px

    private final int touchSlop;// 触发后小于改距离的，不移动
    private final Scroller scroller;// 使切view的时候效果比较平滑
    private VelocityTracker velocityTracker;// 计算手势的一些速率等的工具类
    private float lastMotionX;// 记录最后一次x坐标值

    private int touchState = TOUCH_STATE_REST;
    private static final int TOUCH_STATE_REST = 0;// 空闲状态
    private static final int TOUCH_STATE_SCROLLING = 1;// 正在滚动状态

    private SwipeCompleteListener swipeCompleteListener;

    private int curScreen;// 记录当前屏幕的位置，从0表示content，1表示behind

    private boolean canSwipe = true;

    public SwipeView(Context context) {
        this(context, null, 0);
    }

    public SwipeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
        scroller = new Scroller(context);

        setFocusableInTouchMode(true);
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus || CURSCREEN_BEHIND == curScreen) {
                    snapToScreen(CURSCREEN_CONTENT);
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (null != mContent) {
            mContent.layout(0, 0, mContent.getMeasuredWidth(), mContent.getMeasuredHeight());
        }

        if (null != mBehind) {
            mBehind.layout(mContent.getMeasuredWidth(), 0, mContent.getMeasuredWidth() + behindWidth,
                    mContent.getMeasuredHeight());
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!canSwipe) {
            return false;
        }

        if (!hasFocus()) {
            requestFocus();
        }

        final int action = ev.getAction();
        // 如果事件是正在移动，且触发状态不在空闲状态，就拦截事件，不让事件往子控件传递
        if ((action == MotionEvent.ACTION_MOVE) && (touchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            lastMotionX = x;
            touchState = scroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
            break;
        case MotionEvent.ACTION_MOVE:
            final int xDiff = (int) Math.abs(lastMotionX - x);
            if (xDiff > touchSlop) {
                touchState = TOUCH_STATE_SCROLLING;
            }
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            touchState = TOUCH_STATE_REST;
            break;
        }

        return touchState != TOUCH_STATE_REST;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canSwipe) {
            return false;
        }

        if (null == velocityTracker) {
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

            int wantToOffset = getScrollX() + deltaX;
            if (wantToOffset >= 0 && wantToOffset <= behindWidth && null != mBehind) {
                scrollBy(deltaX, 0);
            }
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            final VelocityTracker vt = velocityTracker;
            vt.computeCurrentVelocity(1000);
            int velocityX = (int) vt.getXVelocity();

            if (velocityX > SNAP_VELOCITY && curScreen == CURSCREEN_BEHIND) {
                // 快速向右滑动
                snapToScreen(CURSCREEN_CONTENT);
            }
            else if (velocityX < -SNAP_VELOCITY && curScreen == CURSCREEN_CONTENT) {
                // 快速向左滑动
                snapToScreen(CURSCREEN_BEHIND);
            }
            else {
                snapToDestination();
            }

            if (null != velocityTracker) {
                velocityTracker.recycle();
                velocityTracker = null;
            }
            touchState = TOUCH_STATE_REST;
            break;
        }

        return false;
    }

    /**
     * 添加子界面
     * 
     * @param content
     * @param bebind
     */
    public void addContentAndBehind(View content, View behind) {
        setContent(content);
        setBehind(behind);
    }

    /**
     * 根据当前中间位置，跳转到偏向的界面
     */
    public void snapToDestination() {
        final int destScreen = (getScrollX() + behindWidth / 2) / behindWidth;
        snapToScreen(destScreen);
    }

    /**
     * 平滑的切换到指定屏幕，0到content界面，1到behind界面
     * 
     * @param whichScreen
     */
    public void snapToScreen(int whichScreen) {
        // 保证是0或者1
        if (whichScreen >= 1) {
            whichScreen = CURSCREEN_BEHIND;
        }
        else {
            whichScreen = CURSCREEN_CONTENT;
        }

        // 滚动没有在指定whichScreen位置上，就进行滚动设置，设置到要滚动到的位置上
        if (getScrollX() != (whichScreen * behindWidth)) {
            // 计算公式：当前位置+delta=目的地位置
            int delta = whichScreen * behindWidth - getScrollX();
            scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);

            if (curScreen != whichScreen && null != swipeCompleteListener) {
                swipeCompleteListener.whichScreen(whichScreen);
            }

            curScreen = whichScreen;
            invalidate();
        }
    }

    public void toggle() {
        if (curScreen == CURSCREEN_CONTENT) {
            snapToScreen(CURSCREEN_BEHIND);
        }
        else if (curScreen == CURSCREEN_BEHIND) {
            snapToScreen(CURSCREEN_CONTENT);
        }
    }

    /**
     * 设置侧滑完成后监听回调
     * 
     * @param swipeCompleteListener
     */
    public void setSwipeCompleteListener(SwipeCompleteListener swipeCompleteListener) {
        this.swipeCompleteListener = swipeCompleteListener;
    }

    public View getContent() {
        return mContent;
    }

    /**
     * 设置主界面
     * 
     * @param content
     */
    public void setContent(View content) {
        if (null == content) {
            return;
        }

        this.mContent = content;
        addView(mContent);
    }

    public View getBehind() {
        return mBehind;
    }

    /**
     * 设置侧滑后出现界面
     * 
     * @param behind
     */
    public void setBehind(View behind) {
        if (null == behind) {
            return;
        }

        this.mBehind = behind;
        addView(mBehind);
    }

    public int getBehindWidth() {
        return behindWidth;
    }

    /**
     * 用资源文件中的dp单位设置侧滑后出现的宽度
     * 
     * @param resid
     */
    public void setBehindWidthRes(int resid) {
        this.behindWidth = (int) getResources().getDimension(resid);
    }

    public void setBehindWidth(int behindWidth) {
        this.behindWidth = behindWidth;
    }

    public int getCurScreen() {
        return curScreen;
    }

    /**
     * 侧滑完成监听，which：0表示content界面，1表示behind界面
     * 
     * @author xuan
     */
    public interface SwipeCompleteListener {
        public void whichScreen(int which);
    }

    public boolean isCanSwipe() {
        return canSwipe;
    }

    public void setCanSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
    }

}
