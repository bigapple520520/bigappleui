package com.dazzle.bigappleui.slidingmenu.app;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.dazzle.bigappleui.slidingmenu.SlidingMenu;

public interface SlidingActivityBase {

    /**
     * 设置侧滑view
     * 
     * @param view
     * @param layoutParams
     */
    public void setBehindContentView(View view, LayoutParams layoutParams);

    public void setBehindContentView(View view);

    public void setBehindContentView(int layoutResID);

    /**
     * 获取侧滑菜单
     * 
     * @return
     */
    public SlidingMenu getSlidingMenu();

    /**
     * 打开或者关闭侧滑
     */
    public void toggle();

    /**
     * 关闭侧滑显示主界面
     */
    public void showContent();

    /**
     * 显示侧滑菜单
     */
    public void showMenu();

    /**
     * 显示二级侧滑菜单
     */
    public void showSecondaryMenu();

    /**
     * 设置ActionBar是否跟着一起滑动
     * 
     * @param slidingActionBarEnabled
     */
    public void setSlidingActionBarEnabled(boolean slidingActionBarEnabled);

}
