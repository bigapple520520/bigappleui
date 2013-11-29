package com.dazzle.bigappleui.slidingmenu;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.dazzle.bigappleui.slidingmenu.CustomViewAbove.OnPageChangeListener;

/**
 * 会替换DcorView下的子View，由SlidingMenu充当<br>
 * 使用时，需要引入layout中的slidingmenumain.xml，和values中的attrs.xml
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-11-13 下午5:03:55 $
 */
public class SlidingMenu extends RelativeLayout {
    private static final String TAG = "SlidingMenu";

    // 手势侧滑检测区域：0边缘、1全屏、2没有
    public static final int TOUCHMODE_MARGIN = 0;
    public static final int TOUCHMODE_FULLSCREEN = 1;
    public static final int TOUCHMODE_NONE = 2;

    // 侧滑菜单模式：0只有左菜单、1只有右菜单、2左右都有
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int LEFT_RIGHT = 2;

    // 滑动载体：0整个窗体滑动、1内容滑动
    public static final int SLIDING_WINDOW = 0;
    public static final int SLIDING_CONTENT = 1;

    // 是否Actionbar一起滑动
    private boolean mActionbarOverlay = false;

    // 主界面和侧滑界面容器
    private final CustomViewAbove mViewAbove;
    private final CustomViewBehind mViewBehind;

    // 事件监听接口
    private OnOpenListener mOpenListener;
    private OnOpenListener mSecondaryOpenListner;
    private OnCloseListener mCloseListener;

    // ////////////////////////////////////////////SlidingMenu构造//////////////////////////////////////////////////////
    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Activity activity, int slideStyle) {
        this(activity, null);
        this.attachToActivity(activity, slideStyle);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewBehind = new CustomViewBehind(context);
        addView(mViewBehind, behindParams);

        LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewAbove = new CustomViewAbove(context);
        addView(mViewAbove, aboveParams);

        // 相互引用
        mViewAbove.setCustomViewBehind(mViewBehind);
        mViewBehind.setCustomViewAbove(mViewAbove);

        mViewAbove.setOnPageChangeListener(new OnPageChangeListener() {
            public static final int POSITION_OPEN = 0;// 左侧菜单打开
            public static final int POSITION_CLOSE = 1;// 菜单都关闭，显示主界面
            public static final int POSITION_SECONDARY_OPEN = 2;// 右侧菜单打开

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == POSITION_OPEN && mOpenListener != null) {
                    mOpenListener.onOpen();
                }
                else if (position == POSITION_CLOSE && mCloseListener != null) {
                    mCloseListener.onClose();
                }
                else if (position == POSITION_SECONDARY_OPEN && mSecondaryOpenListner != null) {
                    mSecondaryOpenListner.onOpen();
                }
            }
        });

        // 获取xml中设置的自定义属性，放到TypedArray容器中，下面就是针对xml中设置的属性赋值调整
        // TypedArray ta = context.obtainStyledAttributes(attrs,
        // ResourceResidUtils.getIdsByName(context, "styleable", "SlidingMenu"));

        // 设置左右侧滑模式，可在代码中设置
        // int mode = ta.getInt(ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_mode"), LEFT);
        // setMode(mode);
        setMode(LEFT);

        // 设置主界面view，可在代码中设置
        // int viewAbove = ta.getResourceId(ResourceResidUtils.getResidByStyleableName(context,
        // "SlidingMenu_viewAbove"),
        // -1);
        // if (viewAbove != -1) {
        // setContent(viewAbove);
        // }
        // else {
        // setContent(new FrameLayout(context));
        // }
        setContent(new FrameLayout(context));

        // int viewBehind = ta.getResourceId(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_viewBehind"), -1);
        // if (viewBehind != -1) {
        // setMenu(viewBehind);
        // }
        // else {
        // setMenu(new FrameLayout(context));
        // }
        setMenu(new FrameLayout(context));

        // 设置主界面时，怎样能侧滑出来
        // int touchModeAbove = ta.getInt(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_touchModeAbove"), TOUCHMODE_MARGIN);
        // setTouchModeAbove(touchModeAbove);
        setTouchModeAbove(TOUCHMODE_MARGIN);

        // 设置菜单界面时，怎样能侧滑回去
        // int touchModeBehind = ta.getInt(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_touchModeBehind"), TOUCHMODE_MARGIN);
        // setTouchModeBehind(touchModeBehind);
        setTouchModeBehind(TOUCHMODE_MARGIN);

        // 设置侧滑出来的菜单的宽度和显示时的偏移量，两个必须得有一个，两个都设置的话，先设置偏移量
        // int offsetBehind = (int) ta.getDimension(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_behindOffset"), -1);
        // int widthBehind = (int) ta.getDimension(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_behindWidth"), -1);
        // if (offsetBehind != -1 && widthBehind != -1) {
        // throw new IllegalStateException("Cannot set both behindOffset and behindWidth for a SlidingMenu");
        // }
        // else if (offsetBehind != -1) {
        // setBehindOffset(offsetBehind);
        // }
        // else if (widthBehind != -1) {
        // setBehindWidth(widthBehind);
        // }
        // else {
        // setBehindOffset(0);
        // }

        setBehindOffset(0);

        // 菜单时滚动条的透明度
        // float scrollOffsetBehind = ta.getFloat(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_behindScrollScale"), 0.33f);
        // setBehindScrollScale(scrollOffsetBehind);
        setBehindScrollScale(0.33f);

        // 阴影部分设置
        // int shadowRes = ta.getResourceId(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_shadowDrawable"), -1);
        // if (shadowRes != -1) {
        // setShadowDrawable(shadowRes);
        // }

        // int shadowWidth = (int) ta.getDimension(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_shadowWidth"), 0);
        // setShadowWidth(shadowWidth);
        setShadowWidth(0);

        // 设置滑出滑入部分是否淡入淡出
        // boolean fadeEnabled = ta.getBoolean(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_fadeEnabled"), true);
        // setFadeEnabled(fadeEnabled);
        setFadeEnabled(true);

        // float fadeDeg = ta.getFloat(ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_fadeDegree"),
        // 0.33f);
        // setFadeDegree(fadeDeg);
        setFadeDegree(0.33f);

        // 大概选中时的状态设置吧
        // boolean selectorEnabled = ta.getBoolean(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_selectorEnabled"), false);
        // setSelectorEnabled(selectorEnabled);
        setSelectorEnabled(false);

        // int selectorRes = ta.getResourceId(
        // ResourceResidUtils.getResidByStyleableName(context, "SlidingMenu_selectorDrawable"), -1);
        // if (selectorRes != -1) {
        // setSelectorDrawable(selectorRes);
        // }

        // ta.recycle();
    }

    // //////////////////////////////把SlidingMenu挂到顶级DcorView下///////////////////////////////////////////////////
    public void attachToActivity(Activity activity, int slideStyle) {
        attachToActivity(activity, slideStyle, false);
    }

    /**
     * 把SlidingMenu挂到顶级DcorView下
     * 
     * @param activity
     * @param slideStyle
     * @param actionbarOverlay
     *            Actionbar是否跟着一起动
     */
    public void attachToActivity(Activity activity, int slideStyle, boolean actionbarOverlay) {
        if (slideStyle != SLIDING_WINDOW && slideStyle != SLIDING_CONTENT) {
            throw new IllegalArgumentException("slideStyle must be either SLIDING_WINDOW or SLIDING_CONTENT");
        }

        if (getParent() != null) {
            throw new IllegalStateException("This SlidingMenu appears to already be attached");
        }

        // get the window background
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
        int background = a.getResourceId(0, 0);
        a.recycle();

        switch (slideStyle) {
        case SLIDING_WINDOW:
            mActionbarOverlay = false;
            ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
            ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);

            // save ActionBar themes that have transparent assets
            decorChild.setBackgroundResource(background);
            decor.removeView(decorChild);
            decor.addView(this);
            setContent(decorChild);
            break;
        case SLIDING_CONTENT:
            mActionbarOverlay = actionbarOverlay;

            // take the above view out of
            ViewGroup contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
            View content = contentParent.getChildAt(0);
            contentParent.removeView(content);
            contentParent.addView(this);
            setContent(content);

            // save people from having transparent backgrounds
            if (content.getBackground() == null) {
                content.setBackgroundResource(background);
            }
            break;
        }
    }

    // ////////////////////////////////////////主界面View，绑定到mViewAbove/////////////////////////////////////////////
    public void setContent(int res) {
        setContent(LayoutInflater.from(getContext()).inflate(res, null));
    }

    public void setContent(View view) {
        mViewAbove.setContent(view);
        showContent();
    }

    public View getContent() {
        return mViewAbove.getContent();
    }

    // ////////////////////////////////////////左右侧滑界面View，绑定到mViewBehind//////////////////////////////////////
    public void setMenu(int res) {
        setMenu(LayoutInflater.from(getContext()).inflate(res, null));
    }

    public void setMenu(View v) {
        mViewBehind.setContent(v);
    }

    public View getMenu() {
        return mViewBehind.getContent();
    }

    public void setSecondaryMenu(int res) {
        setSecondaryMenu(LayoutInflater.from(getContext()).inflate(res, null));
    }

    public void setSecondaryMenu(View v) {
        mViewBehind.setSecondaryContent(v);
    }

    public View getSecondaryMenu() {
        return mViewBehind.getSecondaryContent();
    }

    // ///////////////////////////////////////设置是否侧滑/////////////////////////////////////////////////////////////
    public void setSlidingEnabled(boolean b) {
        mViewAbove.setSlidingEnabled(b);
    }

    public boolean isSlidingEnabled() {
        return mViewAbove.isSlidingEnabled();
    }

    // ///////////////////////////////////////设置侧滑模式/////////////////////////////////////////////////////////////
    public void setMode(int mode) {
        if (mode != LEFT && mode != RIGHT && mode != LEFT_RIGHT) {
            throw new IllegalStateException("SlidingMenu mode must be LEFT, RIGHT, or LEFT_RIGHT");
        }
        mViewBehind.setMode(mode);
    }

    public int getMode() {
        return mViewBehind.getMode();
    }

    /**
     * 设置SlidingMenu是否为static模式，如果true，就不允许侧滑
     * 
     * @param b
     */
    public void setStatic(boolean b) {
        if (b) {
            setSlidingEnabled(false);
            mViewAbove.setCustomViewBehind(null);
            mViewAbove.setCurrentItem(1);
        }
        else {
            mViewAbove.setCurrentItem(1);
            mViewAbove.setCustomViewBehind(mViewBehind);
            setSlidingEnabled(true);
        }
    }

    // /////////////////////////////////////////显示左右菜单和主界面方法/////////////////////////////////////////////////
    public void showMenu() {
        showMenu(true);
    }

    public void showMenu(boolean animate) {
        mViewAbove.setCurrentItem(0, animate);
    }

    public void showSecondaryMenu() {
        showSecondaryMenu(true);
    }

    public void showSecondaryMenu(boolean animate) {
        mViewAbove.setCurrentItem(2, animate);
    }

    public void showContent() {
        showContent(true);
    }

    public void showContent(boolean animate) {
        mViewAbove.setCurrentItem(1, animate);
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean animate) {
        if (isMenuShowing()) {
            showContent(animate);
        }
        else {
            showMenu(animate);
        }
    }

    public boolean isMenuShowing() {
        return mViewAbove.getCurrentItem() == 0 || mViewAbove.getCurrentItem() == 2;
    }

    public boolean isSecondaryMenuShowing() {
        return mViewAbove.getCurrentItem() == 2;
    }

    // ///////////////////////////////////////设置菜单和主界面的偏移量//////////////////////////////////////////////////
    public int getBehindOffset() {
        return ((RelativeLayout.LayoutParams) mViewBehind.getLayoutParams()).rightMargin;
    }

    public void setBehindOffset(int i) {
        mViewBehind.setWidthOffset(i);
    }

    /**
     * 可以使用xml中的数值，例如需要用dp做单位的
     * 
     * @param resID
     */
    public void setBehindOffsetRes(int resID) {
        int i = (int) getContext().getResources().getDimension(resID);
        setBehindOffset(i);
    }

    public void setAboveOffset(int i) {
        mViewAbove.setAboveOffset(i);
    }

    public void setAboveOffsetRes(int resID) {
        int i = (int) getContext().getResources().getDimension(resID);
        setAboveOffset(i);
    }

    @SuppressWarnings("deprecation")
    public void setBehindWidth(int i) {
        int width;
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        try {
            Class<?> cls = Display.class;
            Class<?>[] parameterTypes = { Point.class };
            Point parameter = new Point();
            Method method = cls.getMethod("getSize", parameterTypes);
            method.invoke(display, parameter);
            width = parameter.x;
        }
        catch (Exception e) {
            width = display.getWidth();
        }
        setBehindOffset(width - i);
    }

    public void setBehindWidthRes(int res) {
        int i = (int) getContext().getResources().getDimension(res);
        setBehindWidth(i);
    }

    // //////////////////////////////////////////滚动条设置/////////////////////////////////////////////////////////////
    public float getBehindScrollScale() {
        return mViewBehind.getScrollScale();
    }

    public int getTouchmodeMarginThreshold() {
        return mViewBehind.getMarginThreshold();
    }

    public void setTouchmodeMarginThreshold(int touchmodeMarginThreshold) {
        mViewBehind.setMarginThreshold(touchmodeMarginThreshold);
    }

    public void setBehindScrollScale(float f) {
        if (f < 0 && f > 1) {
            throw new IllegalStateException("ScrollScale must be between 0 and 1");
        }
        mViewBehind.setScrollScale(f);
    }

    public void setBehindCanvasTransformer(CanvasTransformer t) {
        mViewBehind.setCanvasTransformer(t);
    }

    // //////////////////////////////////////////侧滑模式///////////////////////////////////////////////////////////
    public int getTouchModeAbove() {
        return mViewAbove.getTouchMode();
    }

    public void setTouchModeAbove(int i) {
        if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN && i != TOUCHMODE_NONE) {
            throw new IllegalStateException("TouchMode must be set to either"
                    + "TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE.");
        }
        mViewAbove.setTouchMode(i);
    }

    public void setTouchModeBehind(int i) {
        if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN && i != TOUCHMODE_NONE) {
            throw new IllegalStateException("TouchMode must be set to either"
                    + "TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE.");
        }
        mViewBehind.setTouchMode(i);
    }

    // ///////////////////////////////////////////阴影处理/////////////////////////////////////////////////////////////
    public void setShadowDrawable(int resId) {
        setShadowDrawable(getContext().getResources().getDrawable(resId));
    }

    public void setShadowDrawable(Drawable d) {
        mViewBehind.setShadowDrawable(d);
    }

    public void setSecondaryShadowDrawable(int resId) {
        setSecondaryShadowDrawable(getContext().getResources().getDrawable(resId));
    }

    public void setSecondaryShadowDrawable(Drawable d) {
        mViewBehind.setSecondaryShadowDrawable(d);
    }

    public void setShadowWidthRes(int resId) {
        setShadowWidth((int) getResources().getDimension(resId));
    }

    public void setShadowWidth(int pixels) {
        mViewBehind.setShadowWidth(pixels);
    }

    // ///////////////////////////////////////侧滑时，是否渐变界面///////////////////////////////////////////////////////
    public void setFadeEnabled(boolean b) {
        mViewBehind.setFadeEnabled(b);
    }

    public void setFadeDegree(float f) {
        mViewBehind.setFadeDegree(f);
    }

    // ///////////////////////////////////////////选中处理/////////////////////////////////////////////////////////////
    public void setSelectorEnabled(boolean b) {
        mViewBehind.setSelectorEnabled(true);
    }

    public void setSelectedView(View v) {
        mViewBehind.setSelectedView(v);
    }

    public void setSelectorDrawable(int res) {
        mViewBehind.setSelectorBitmap(BitmapFactory.decodeResource(getResources(), res));
    }

    public void setSelectorBitmap(Bitmap b) {
        mViewBehind.setSelectorBitmap(b);
    }

    // ///////////////////////////当全屏侧滑模式时，添加删除忽略不触发侧滑的View/////////////////////////////////////////
    public void addIgnoredView(View v) {
        mViewAbove.addIgnoredView(v);
    }

    public void removeIgnoredView(View v) {
        mViewAbove.removeIgnoredView(v);
    }

    public void clearIgnoredViews() {
        mViewAbove.clearIgnoredViews();
    }

    // ///////////////////////////////////////////侧滑到各种状态的监听设置////////////////////////////////////////////
    /**
     * 左菜单被开始打开
     * 
     * @param listener
     */
    public void setOnOpenListener(OnOpenListener listener) {
        mOpenListener = listener;
    }

    /**
     * 右菜单被开始打开
     * 
     * @param listener
     */
    public void setSecondaryOnOpenListner(OnOpenListener listener) {
        mSecondaryOpenListner = listener;
    }

    /**
     * 左右菜单被开始关闭
     * 
     * @param listener
     */
    public void setOnCloseListener(OnCloseListener listener) {
        mCloseListener = listener;
    }

    /**
     * 菜单被打开后
     * 
     * @param listener
     */
    public void setOnOpenedListener(OnOpenedListener listener) {
        mViewAbove.setOnOpenedListener(listener);
    }

    /**
     * 菜单被关闭后
     * 
     * @param listener
     */
    public void setOnClosedListener(OnClosedListener listener) {
        mViewAbove.setOnClosedListener(listener);
    }

    public static class SavedState extends BaseSavedState {
        private final int mItem;

        public SavedState(Parcelable superState, int item) {
            super(superState);
            mItem = item;
        }

        private SavedState(Parcel in) {
            super(in);
            mItem = in.readInt();
        }

        public int getItem() {
            return mItem;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mItem);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState, mViewAbove.getCurrentItem());
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mViewAbove.setCurrentItem(ss.getItem());
    }

    @SuppressLint("NewApi")
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        int leftPadding = insets.left;
        int rightPadding = insets.right;
        int topPadding = insets.top;
        int bottomPadding = insets.bottom;
        if (!mActionbarOverlay) {
            Log.v(TAG, "setting padding!");
            setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        }
        return true;
    }

    private final Handler mHandler = new Handler();

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void manageLayers(float percentOpen) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }

        boolean layer = percentOpen > 0.0f && percentOpen < 1.0f;
        final int layerType = layer ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;

        if (layerType != getContent().getLayerType()) {
            mHandler.post(new Runnable() {
                public void run() {
                    Log.v(TAG, "changing layerType. hardware? " + (layerType == View.LAYER_TYPE_HARDWARE));
                    getContent().setLayerType(layerType, null);
                    getMenu().setLayerType(layerType, null);
                    if (getSecondaryMenu() != null) {
                        getSecondaryMenu().setLayerType(layerType, null);
                    }
                }
            });
        }
    }

    // /////////////////////////////////////////////事件监听接口////////////////////////////////////////////////////////
    public interface OnOpenListener {
        public void onOpen();
    }

    public interface OnOpenedListener {
        public void onOpened();
    }

    public interface OnCloseListener {
        public void onClose();
    }

    public interface OnClosedListener {
        public void onClosed();
    }

    public interface CanvasTransformer {
        public void transformCanvas(Canvas canvas, float percentOpen);
    }

    public CustomViewAbove getmViewAbove() {
        return mViewAbove;
    }

    public CustomViewBehind getmViewBehind() {
        return mViewBehind;
    }

}
