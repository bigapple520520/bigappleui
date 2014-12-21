package com.dazzle.bigappleui.view.tab.sw;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * TabHost接口定义
 * 
 * @author xuan
 *
 */
public interface ITabHost {
	/**
	 * 获取Tab的背景容器
	 * 
	 * @param context
	 * @return
	 */
	LinearLayout getTabsLayout(Context context);

	/**
	 * 获取指示器的布局容器
	 * 
	 * @param context
	 * @return
	 */
	FrameLayout getIndicatorLayout(Context context);
	
	/**
	 * 获取指示器
	 * 
	 * @param context
	 * @return
	 */
	View getIndicator(Context context);
	
	/**
	 * 获取tab的内容容器布局
	 * 
	 * @param context
	 * @return
	 */
	FrameLayout getTabContentsLayout(Context context);
	
	/**
	 * 获取指示器和tab内容页之间的分割线，设置null表示没有
	 * 
	 * @param context
	 * @return
	 */
	View getDividerFromIndicatorToTabContents(Context context);
	
}
