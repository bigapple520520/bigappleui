package com.dazzle.bigappleui.view.tab.sw;

import com.dazzle.bigappleui.utils.ui.DisplayUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 默认的tabHost实现，用户可以继承他复写部分方法，而达到自定义的效果
 * 
 * @author xuan
 */
public class DefaultTabHost implements ITabHost {
	
	@Override
	public LinearLayout getTabsLayout(Context context) {
		LinearLayout tabsLayout = new LinearLayout(context);
        LinearLayout.LayoutParams tabsLayoutLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        		 (int) DisplayUtils.getPxByDp((Activity) context, 50));
        tabsLayout.setLayoutParams(tabsLayoutLp);
        tabsLayout.setOrientation(LinearLayout.HORIZONTAL);
        return tabsLayout;
	}
	
	@Override
	public FrameLayout getIndicatorLayout(Context context) {
		FrameLayout indicateLayout = new FrameLayout(context);
        LinearLayout.LayoutParams indicateLayoutLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        		(int) DisplayUtils.getPxByDp((Activity) context, 5));
        indicateLayout.setLayoutParams(indicateLayoutLp);
        indicateLayout.setBackgroundColor(Color.WHITE);
        return indicateLayout;
	}

	@Override
	public View getIndicator(Context context) {
		View indicator = new View(context);
        FrameLayout.LayoutParams indicatorLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        indicator.setLayoutParams(indicatorLp);
        indicator.setBackgroundColor(Color.BLACK);
        return indicator;
	}
	
	@Override
	public FrameLayout getTabContentsLayout(Context context) {
		FrameLayout tabContentsLayout = new FrameLayout(context);
        LinearLayout.LayoutParams tabContentsLayoutLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tabContentsLayout.setLayoutParams(tabContentsLayoutLp);
        return tabContentsLayout;
	}
	
	@Override
	public View getDividerFromIndicatorToTabContents(Context context) {
		return null;
	}

}
