package com.dazzle.bigappleui.slidingmenu.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.dazzle.bigappleui.slidingmenu.SlidingMenu;

/**
 * 侧滑Activity帮助工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-11-13 下午7:06:45 $
 */
public class SlidingActivityHelper {
    private final Activity mActivity;
    private SlidingMenu mSlidingMenu;

    private View mViewAbove;// 主显示界面
    private View mViewBehind;// 侧滑界面

    private boolean mBroadcasting = false;
    private boolean mOnPostCreateCalled = false;
    private boolean mEnableSlide = true;

    public SlidingActivityHelper(Activity activity) {
        mActivity = activity;
    }

    /**
     * 创建SlidingMenu
     * 
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        // mSlidingMenu = (SlidingMenu) LayoutInflater.from(mActivity).inflate(
        // ResourceResidUtils.getResidByLayoutName(mActivity, "slidingmenumain"), null);

        mSlidingMenu = new SlidingMenu(mActivity);
    }

    /**
     * 进一步初始化SlidingMenu，必须在onPostCreate方法里做
     * 
     * @param savedInstanceState
     */
    public void onPostCreate(Bundle savedInstanceState) {
        if (mViewBehind == null || mViewAbove == null) {
            throw new IllegalStateException("Both setBehindContentView must be called "
                    + "in onCreate in addition to setContentView.");
        }

        mOnPostCreateCalled = true;
        mSlidingMenu.attachToActivity(mActivity, mEnableSlide ? SlidingMenu.SLIDING_WINDOW
                : SlidingMenu.SLIDING_CONTENT);

        // 设置上次保存的左右侧滑是否显示
        final boolean open;
        final boolean secondary;
        if (savedInstanceState != null) {
            open = savedInstanceState.getBoolean("SlidingActivityHelper.open");
            secondary = savedInstanceState.getBoolean("SlidingActivityHelper.secondary");
        }
        else {
            open = false;
            secondary = false;
        }

        new Handler().post(new Runnable() {
            public void run() {
                if (open) {
                    if (secondary) {
                        mSlidingMenu.showSecondaryMenu(false);
                    }
                    else {
                        mSlidingMenu.showMenu(false);
                    }
                }
                else {
                    mSlidingMenu.showContent(false);
                }
            }
        });
    }

    /**
     * 设置ActionBar是否跟着一起侧滑
     * 
     * @param slidingActionBarEnabled
     */
    public void setSlidingActionBarEnabled(boolean slidingActionBarEnabled) {
        if (mOnPostCreateCalled) {
            throw new IllegalStateException("enableSlidingActionBar must be called in onCreate.");
        }
        mEnableSlide = slidingActionBarEnabled;
    }

    /**
     * 查找SlidingMenu中的View
     * 
     * @param id
     * @return
     */
    public View findViewById(int id) {
        View v;
        if (mSlidingMenu != null) {
            v = mSlidingMenu.findViewById(id);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    /**
     * 保存侧滑状态
     * 
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("SlidingActivityHelper.open", mSlidingMenu.isMenuShowing());
        outState.putBoolean("SlidingActivityHelper.secondary", mSlidingMenu.isSecondaryMenuShowing());
    }

    /**
     * 设置主显示界面
     * 
     * @param v
     * @param params
     *            没有使用
     */
    public void registerAboveContentView(View v, LayoutParams params) {
        if (!mBroadcasting) {
            mViewAbove = v;
        }
    }

    /**
     * 把View设置到本身的Activity中去
     * 
     * @param v
     */
    public void setContentView(View v) {
        mBroadcasting = true;
        mActivity.setContentView(v);
    }

    public void setBehindContentView(View view, LayoutParams layoutParams) {
        mViewBehind = view;
        mSlidingMenu.setMenu(mViewBehind);
    }

    public SlidingMenu getSlidingMenu() {
        return mSlidingMenu;
    }

    public void toggle() {
        mSlidingMenu.toggle();
    }

    public void showContent() {
        mSlidingMenu.showContent();
    }

    public void showMenu() {
        mSlidingMenu.showMenu();
    }

    public void showSecondaryMenu() {
        mSlidingMenu.showSecondaryMenu();
    }

    /**
     * 按下返回时，如果显示的是菜单，那就切回到主界面
     * 
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mSlidingMenu.isMenuShowing()) {
            showContent();
            return true;
        }
        return false;
    }

}
