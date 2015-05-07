package com.dazzle.bigappleui.slidingmenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.dazzle.bigappleui.slidingmenu.CustomViewAbove.OnPageChangeListener;

/**
 * 这个类是侧滑界面容器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-12-5 上午9:51:45 $
 */
public class SlidingMenu extends RelativeLayout {
    private static final String TAG = "SlidingMenu";

    /** 当前打开位置状态：0左侧菜单打开、1主界面打开、2右侧菜单打开 */
    public static final int POSITION_OPEN = 0;
    public static final int POSITION_CLOSE = 1;
    public static final int POSITION_SECONDARY_OPEN = 2;

    /** 手势侧滑检测区域：0边缘、1全屏、2没有（即不能进行侧滑） */
    public static final int TOUCHMODE_MARGIN = 0;
    public static final int TOUCHMODE_FULLSCREEN = 1;
    public static final int TOUCHMODE_NONE = 2;

    /** 侧滑菜单模式：0只有左菜单、1只有右菜单、2左右都有 */
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int LEFT_RIGHT = 2;

    /** 侧滑的主界面 */
    private final CustomViewAbove mViewAbove;
    /** 测试的左右菜单 */
    private final CustomViewBehind mViewBehind;

    /** 左侧界面打开时回调 */
    private OnOpenListener mOpenListener;
    /** 右侧界面打开时回调 */
    private OnOpenListener mSecondaryOpenListner;
    /** 主界面打开时回调 */
    private OnCloseListener mCloseListener;

    private final Handler mHandler = new Handler();

    // ////////////////////////////////////////////SlidingMenu构造//////////////////////////////////////////////////////
    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Activity activity, int slideStyle) {
        this(activity, null);
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
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
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

        setMode(LEFT);
        setContent(new FrameLayout(context));
        setMenu(new FrameLayout(context));

        setTouchModeAbove(TOUCHMODE_MARGIN);
        setTouchModeBehind(TOUCHMODE_MARGIN);

        setBehindOffset(0);
        setBehindScrollScale(0.33f);

        setShadowWidth(0);

        // 侧滑时菜单的渐变效果
        setFadeEnabled(true);
        setFadeDegree(0.33f);

        setSelectorEnabled(false);
    }

    // ////////////////////////////////////////主界面View，绑定到mViewAbove/////////////////////////////////////////////
    /**
     * 设置主界面
     * 
     * @param res
     *            resid值
     */
    public void setContent(int res) {
        setContent(LayoutInflater.from(getContext()).inflate(res, null));
    }

    /**
     * 设置主界面
     * 
     * @param view
     *            View对象
     */
    public void setContent(View view) {
        mViewAbove.setContent(view);
        showContent();
    }

    /**
     * 获取主界面
     * 
     * @return
     */
    public View getContent() {
        return mViewAbove.getContent();
    }

    // ////////////////////////////////////////左右侧滑界面View，绑定到mViewBehind//////////////////////////////////////
    /**
     * 设置左侧界面
     * 
     * @param res
     *            resid值
     */
    public void setMenu(int res) {
        setMenu(LayoutInflater.from(getContext()).inflate(res, null));
    }

    /**
     * 设置左侧界面
     * 
     * @param v
     *            View对象
     */
    public void setMenu(View v) {
        mViewBehind.setContent(v);
    }

    /**
     * 获取左侧界面
     * 
     * @return
     */
    public View getMenu() {
        return mViewBehind.getContent();
    }

    /**
     * 设置右侧界面
     * 
     * @param res
     *            resId值
     */
    public void setSecondaryMenu(int res) {
        setSecondaryMenu(LayoutInflater.from(getContext()).inflate(res, null));
    }

    /**
     * 设置右侧界面
     * 
     * @param v
     *            View对象
     */
    public void setSecondaryMenu(View v) {
        mViewBehind.setSecondaryContent(v);
    }

    /**
     * 获取右侧界面
     * 
     * @return
     */
    public View getSecondaryMenu() {
        return mViewBehind.getSecondaryContent();
    }

    // ///////////////////////////////////////设置是否侧滑/////////////////////////////////////////////////////////////
    /**
     * 设置是否能进行侧滑
     * 
     * @param b
     *            true可以false不可以
     */
    public void setSlidingEnabled(boolean b) {
        mViewAbove.setSlidingEnabled(b);
    }

    /**
     * 获取是否能进行侧滑
     * 
     * @return
     */
    public boolean isSlidingEnabled() {
        return mViewAbove.isSlidingEnabled();
    }

    // ///////////////////////////////////////设置侧滑模式/////////////////////////////////////////////////////////////
    /**
     * 设置侧滑模式，只能设置：LEFT、RIGHT、LEFT_RIGHT
     * 
     * @param mode
     *            LEFT只能打开左侧界面、RIGHT只能打开右侧界面、LEFT_RIGHT左侧和右侧界面都可以打开
     */
    public void setMode(int mode) {
        if (mode != LEFT && mode != RIGHT && mode != LEFT_RIGHT) {
            throw new IllegalStateException("SlidingMenu mode must be LEFT, RIGHT, or LEFT_RIGHT");
        }
        mViewBehind.setMode(mode);
    }

    /**
     * 获取侧滑模式
     * 
     * @return
     */
    public int getMode() {
        return mViewBehind.getMode();
    }

    /**
     * 设置SlidingMenu是否为static模式，如果true，就不允许侧滑
     * 
     * @param b
     *            true不允许侧滑false允许侧滑
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
    /**
     * 打开左侧界面，含动画
     */
    public void showMenu() {
        showMenu(true);
    }

    /**
     * 打开左侧界面
     * 
     * @param animate
     *            true动画打开false不含
     */
    public void showMenu(boolean animate) {
        mViewAbove.setCurrentItem(POSITION_OPEN, animate);
    }

    /**
     * 打开右侧界面，含动画
     */
    public void showSecondaryMenu() {
        showSecondaryMenu(true);
    }

    /**
     * 打开右侧界面
     * 
     * @param animate
     *            true动画打开false不含
     */
    public void showSecondaryMenu(boolean animate) {
        mViewAbove.setCurrentItem(2, animate);
    }

    /**
     * 打开主界面，含动画
     */
    public void showContent() {
        showContent(true);
    }

    /**
     * 打开主界面
     * 
     * @param animate
     *            true动画打开false不含
     */
    public void showContent(boolean animate) {
        mViewAbove.setCurrentItem(1, animate);
    }

    /**
     * 左侧界面和主界面的toggle处理，含动画
     */
    public void toggle() {
        toggle(true);
    }

    /**
     * 左侧界面和主界面的toggle处理
     * 
     * @param animate
     *            true动画打开false不含
     */
    public void toggle(boolean animate) {
        if (isMenuShowing()) {
            showContent(animate);
        }
        else {
            showMenu(animate);
        }
    }

    /**
     * 判断左侧或者右侧界面处于打开状态
     * 
     * @return
     */
    public boolean isMenuShowing() {
        return mViewAbove.getCurrentItem() == POSITION_OPEN || mViewAbove.getCurrentItem() == POSITION_SECONDARY_OPEN;
    }

    /**
     * 判断右侧界面是否打开状态
     * 
     * @return
     */
    public boolean isSecondaryMenuShowing() {
        return mViewAbove.getCurrentItem() == POSITION_SECONDARY_OPEN;
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
    /**
     * 获取侧滑模式，具体值参看TOUCHMODE_FULLSCREEN、TOUCHMODE_MARGIN、TOUCHMODE_NONE
     * 
     * @return
     */
    public int getTouchModeAbove() {
        return mViewAbove.getTouchMode();
    }

    /**
     * 设置主界面侧滑模式，当前只能设置：TOUCHMODE_FULLSCREEN、TOUCHMODE_MARGIN、TOUCHMODE_NONE这三种模式
     * 
     * @param i
     *            侧滑模式值
     */
    public void setTouchModeAbove(int i) {
        if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN && i != TOUCHMODE_NONE) {
            throw new IllegalStateException("TouchMode must be set to either"
                    + "TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE.");
        }
        mViewAbove.setTouchMode(i);
    }

    /**
     * 设置左右界面侧滑模式，当前只能设置：TOUCHMODE_FULLSCREEN、TOUCHMODE_MARGIN、TOUCHMODE_NONE这三种模式
     * 
     * @param i
     *            侧滑模式值
     */
    public void setTouchModeBehind(int i) {
        if (i != TOUCHMODE_FULLSCREEN && i != TOUCHMODE_MARGIN && i != TOUCHMODE_NONE) {
            throw new IllegalStateException("TouchMode must be set to either"
                    + "TOUCHMODE_FULLSCREEN or TOUCHMODE_MARGIN or TOUCHMODE_NONE.");
        }
        mViewBehind.setTouchMode(i);
    }

    // ///////////////////////////////////////////侧滑界面边界的阴影处理/////////////////////////////////////////////////
    /**
     * 设置左侧界面阴影资源文件的resid
     * 
     * @param resId
     */
    public void setShadowDrawable(int resId) {
        setShadowDrawable(getContext().getResources().getDrawable(resId));
    }

    /**
     * 设置左侧界面阴影资源文件的Drawable对象
     * 
     * @param d
     */
    public void setShadowDrawable(Drawable d) {
        mViewBehind.setShadowDrawable(d);
    }

    /**
     * 设置右侧界面阴影资源文件的resid
     * 
     * @param resId
     */
    public void setSecondaryShadowDrawable(int resId) {
        setSecondaryShadowDrawable(getContext().getResources().getDrawable(resId));
    }

    /**
     * 设置右侧界面阴影资源文件的Drawable对象
     * 
     * @param d
     */
    public void setSecondaryShadowDrawable(Drawable d) {
        mViewBehind.setSecondaryShadowDrawable(d);
    }

    /**
     * 设置左右侧界面阴影宽度，使用resid设置
     * 
     * @param resId
     */
    public void setShadowWidthRes(int resId) {
        setShadowWidth((int) getResources().getDimension(resId));
    }

    /**
     * 设置左右侧界面阴影宽度，使用px值设置
     * 
     * @param pixels
     */
    public void setShadowWidth(int pixels) {
        mViewBehind.setShadowWidth(pixels);
    }

    // ///////////////////////////////////////侧滑时，侧滑界面出来界面的渐变效果///////////////////////////////////////////
    /**
     * 设置是否需要渐变效果
     * 
     * @param b
     *            true有渐变效果false没有
     */
    public void setFadeEnabled(boolean b) {
        mViewBehind.setFadeEnabled(b);
    }

    /**
     * 设置渐变效果的比例
     * 
     * @param f
     *            比例必须在0.0f到1.0f之间
     */
    public void setFadeDegree(float f) {
        mViewBehind.setFadeDegree(f);
    }

    // ///////////////////////////////////////////选中处理/////////////////////////////////////////////////////////////
    public void setSelectorEnabled(boolean b) {
        mViewBehind.setSelectorEnabled(b);
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

    // ///////////////////////////滑动时忽视界面设置////////////////////////////////////////
    /**
     * 添加滑动侧滑时，需要忽视的界面
     * 
     * @param v
     *            忽视界面
     */
    public void addIgnoredView(View v) {
        mViewAbove.addIgnoredView(v);
    }

    /**
     * 删除滑动侧滑时，需要忽视的界面
     * 
     * @param v
     *            忽视界面
     */
    public void removeIgnoredView(View v) {
        mViewAbove.removeIgnoredView(v);
    }

    /**
     * 清理所有滑动侧滑时，需要忽视的界面
     */
    public void clearIgnoredViews() {
        mViewAbove.clearIgnoredViews();
    }

    // ///////////////////////////////////////////侧滑到各种状态的监听设置////////////////////////////////////////////
    /**
     * 设置左侧界面被打开监听
     * 
     * @param listener
     */
    public void setOnOpenListener(OnOpenListener listener) {
        mOpenListener = listener;
    }

    /**
     * 设置右侧界面被打开监听
     * 
     * @param listener
     */
    public void setSecondaryOnOpenListner(OnOpenListener listener) {
        mSecondaryOpenListner = listener;
    }

    /**
     * 设置主界面被打开监听
     * 
     * @param listener
     */
    public void setOnCloseListener(OnCloseListener listener) {
        mCloseListener = listener;
    }

    /**
     * 设置左侧或者右侧界面被开打监听。注意：即使原来本身是打开的，滑动后回到原来打开状态也会被调用。
     * 
     * @param listener
     */
    public void setOnOpenedListener(OnOpenedListener listener) {
        mViewAbove.setOnOpenedListener(listener);
    }

    /**
     * 设置主界面被开打监听。注意：即使原来本身是打开的，滑动后回到原来打开状态也会被调用。
     * 
     * @param listener
     */
    public void setOnClosedListener(OnClosedListener listener) {
        mViewAbove.setOnClosedListener(listener);
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * BaseSavedState这个本身在View中定义，用来保存View的状态，继承它主要用来保存侧滑的当前状态
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-11-25 下午2:58:42 $
     */
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
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mViewAbove.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mViewAbove.setCurrentItem(ss.getItem());
    }

    // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void manageLayers(float percentOpen) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }

        boolean layer = percentOpen > 0.0f && percentOpen < 1.0f;
        final int layerType = layer ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;

        if (layerType != getContent().getLayerType()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.v(TAG, "changing layerType. hardware? " + (layerType == View.LAYER_TYPE_HARDWARE));
                    getContent().setLayerType(layerType, null);
                    getMenu().setLayerType(layerType, (Paint) null);
                    if (getSecondaryMenu() != null) {
                        getSecondaryMenu().setLayerType(layerType, null);
                    }
                }
            });
        }
    }

    // /////////////////////////////////////////////事件监听接口////////////////////////////////////////////////////////
    /**
     * 当状态改变时，且当左右侧菜单被打开时的回调接口
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-11-25 下午2:34:25 $
     */
    public interface OnOpenListener {
        public void onOpen();
    }

    /**
     * 当状态改变时，且主界面被打开时的回调接口
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-11-25 下午2:36:42 $
     */
    public interface OnCloseListener {
        public void onClose();
    }

    /**
     * 左右侧菜单被打开时回调，注意：即使原本左右侧菜单是打开的，进过滑动后处于原来打开位置，这个回调也会被调用的
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-11-25 下午2:38:41 $
     */
    public interface OnOpenedListener {
        public void onOpened();
    }

    /**
     * 主界面被打开时回调。注意：即使原本主界面是打开的，进过滑动后处于原来打开位置，这个回调也会被调用的
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-11-25 下午2:39:46 $
     */
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
