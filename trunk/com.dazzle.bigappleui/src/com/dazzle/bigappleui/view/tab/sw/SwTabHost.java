/* 
 * @(#)SwTabHost.java    Created on 2014-12-16
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.tab.sw;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dazzle.bigappleui.view.photoview.app.core.HackyViewPager;

/**
 * 基于LinearLayout的Tab切换控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-16 下午3:44:41 $
 */
public class SwTabHost extends LinearLayout {
    /** 当前位置 */
    private int currentPosition = -1;

    /** tab的View数组 */
    private List<View> tabList = new ArrayList<View>();
    /** tab内容页View数组 */
    private List<View> tabContentList = new ArrayList<View>();
    
    /**tabHost*/
    private ITabHost tabHost;

    /** tab的布局容器 */
    private LinearLayout tabsLayout;
    /**tab的布局容器的高度*/
    private int tabsLayoutHeight = -1;

    /**指示器View*/
    private View indicator;
    /**指示器背景布局*/
    private FrameLayout indicatorLayout;
    /** 指示器View的高度 */
    private int indicatorHeight = -1;
    /** 指示器View的宽度，在onSizeChanged方法里会进行计算 */
    private int indicatorWidth;
    /** 指示器的颜色 */
    private int indicatorColor = -1;
    /** 指示器容器背景颜色 */
    private int indicateLayoutColor = -1;

    /** 具体tab页内容容器 */
    private FrameLayout tabContentsLayout;
    /** 内容可以进行滑动切换 */
    private HackyViewPager viewPage;
    
    /** 供外部调用的监听 */
    private OnPageChangeListener onPageChangeListener;
    /** position位置发送变动通知 */
    private OnPositionChangeListener onPositionChangeListener;

    /**
     * 构造方法
     * 
     * @param context
     */
    public SwTabHost(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     * 
     * @param context
     * @param attrs
     */
    public SwTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 构造方法
     * 
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SwTabHost(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /** 初始化参数 */
    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        tabHost = new DefaultTabHost();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // w只能在这里取到，所以要在这里调整indicator的宽度
        if(null != indicator){
        	FrameLayout.LayoutParams indicatorLp = (FrameLayout.LayoutParams) indicator.getLayoutParams();
            indicatorWidth = w / tabList.size();
            indicatorLp.width = indicatorWidth;
            indicator.setLayoutParams(indicatorLp);
        }
    }

    /**
     * 重新布局安装上
     */
    public void setup() {
        checkSizeEquals();
        if (tabList.isEmpty()) {
            return;
        }

        setupTabs();
        setupIndicate();
        setupDividerFromIndicatorToTabContents();
        setupContents();
    }

    /**
     * 初始位置，之后以需要延时是为了让SwTabHost有足够时间绘制，然后得到indicateWidth值
     * 
     * @param position
     */
    public void initPosition(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoPosition(position, false);
            }
        }, 100);
    }

    /**
     * 跳转到指定tab页面，默认具有平滑的动作的
     * 
     * @param position
     *            跳转到指定的位置，从0开始
     */
    public void gotoPosition(int position) {
        gotoPosition(position, true);
    }

    /**
     * 跳转到指定tab页面
     * 
     * @param position
     *            跳转到指定的位置，从0开始
     * @param smoothScroll
     *            是否需要平滑动画，true需要false不需要
     */
    public void gotoPosition(final int position, boolean smoothScroll) {
        if (currentPosition != position) {
            viewPage.setCurrentItem(position, smoothScroll);
            if (!smoothScroll) {
                // smoothScroll=true时会触发ViewPager的滚动监听
            	indicatorLayout.setPadding(position * indicatorWidth, 0, 0, 0);
            }

            currentPosition = position;
            if (null != onPositionChangeListener) {
                onPositionChangeListener.onPositionChange(currentPosition);
            }
        }
    }

    /**
     * 设置ViewPager的滑动监听
     * 
     * @param l
     */
    public void setOnPageChangeListener(OnPageChangeListener l) {
        this.onPageChangeListener = l;
    }

    /**
     * position位置变动监听
     * 
     * @param onPositionChangeListener
     */
    public void setOnPositionChangeListener(OnPositionChangeListener onPositionChangeListener) {
        this.onPositionChangeListener = onPositionChangeListener;
    }

    /**
     * 获取当前位置，从0开始数
     * 
     * @return
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    // /////////////////////////////////添加内容方法//////////////////////////////////////////////
    /**
     * 添加tab页列表到指定位置
     * 
     * @param index
     *            添加到指定位置
     * @param tabList
     *            tab的View列表
     * @param contentList
     *            内容的View列表
     */
    public void addTabsAndContents(int index, List<View> tabList, List<View> contentList) {
        tabList.addAll(index, tabList);
        tabContentList.addAll(index, contentList);
    }

    /**
     * 添加tab页列表到指定位置
     * 
     * @param index
     *            添加到指定位置
     * @param tab
     *            tab的View
     * @param content
     *            内容的View
     */
    public void addTabAndContent(int index, View tab, View content) {
        tabList.add(index, tab);
        tabContentList.add(index, content);
    }

    /**
     * 添加tab页列表到最后位置
     * 
     * @param tabList
     *            tab的View列表
     * @param contentList
     *            内容的View列表
     */
    public void addTabsAndContents(List<View> tabList, List<View> contentList) {
        tabList.addAll(tabList);
        tabContentList.addAll(contentList);
    }

    /**
     * 添加tab页列表到最后位置
     * 
     * @param tab
     *            tab的View
     * @param content
     *            内容的View
     */
    public void addTabAndContent(View tab, View content) {
        tabList.add(tab);
        tabContentList.add(content);
    }

    /**
     * 清理所有View
     */
    public void clearTabAndContent() {
        tabList.clear();
        tabContentList.clear();
    }
    
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 设置tab背景的高度
     * 
     * @param tabsLayoutHeight
     *            以px为单位
     */
    public void setTabsLayoutHeight(int tabsLayoutHeight) {
        this.tabsLayoutHeight = tabsLayoutHeight;
    }

    /**
     * 设置指示器的高度
     * 
     * @param indicateHeight
     *            以px为单位
     */
    public void setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
    }

    /**
     * 设置指示器的颜色
     * 
     * @param indicatorColor
     *            颜色int值
     */
    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    /**
     * 设置指示器的背景颜色
     * 
     * @param indicateLayoutColor
     *            颜色int值
     */
    public void setIndicateLayoutColor(int indicateLayoutColor) {
        this.indicateLayoutColor = indicateLayoutColor;
    }

    
	/**
	 * 设置tabHost，这个tabHost对象自定义了一些布局
	 * 
	 * @param tabHost
	 */
    public void setTabHost(ITabHost tabHost) {
		this.tabHost = tabHost;
	}

    // /////////////////////////////////////////////内部辅助方法///////////////////////////////////////////////////////
    /** tabs部分布局 */
    private void setupTabs() {
        tabsLayout = tabHost.getTabsLayout(getContext());
        if(-1 != tabsLayoutHeight){
        	//调整高度
    		ViewGroup.LayoutParams tabsLayoutLp = tabsLayout.getLayoutParams();
    		if(null == tabsLayoutLp){
    			tabsLayoutLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tabsLayoutHeight);
    		}else{
    			tabsLayoutLp.height = tabsLayoutHeight;
    		}
    		tabsLayout.setLayoutParams(tabsLayoutLp);
        }
        addView(tabsLayout);
        
        //循环设置Tab的View
        for (View view : tabList) {
            LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            viewLp.weight = 1;
            view.setLayoutParams(viewLp);
            tabsLayout.addView(view);
        }
    }

    /** 指示条部分布局 */
    private void setupIndicate() {
    	//指示器的背景布局设置
    	indicatorLayout = tabHost.getIndicatorLayout(getContext());
    	if(-1 != indicateLayoutColor){
    		//调整背景颜色
    		indicatorLayout.setBackgroundColor(indicateLayoutColor);
    	}
    	addView(indicatorLayout);

    	//指示器设置
    	indicator = tabHost.getIndicator(getContext());
    	if(-1 != indicatorHeight){
    		//调整高度
    		ViewGroup.LayoutParams indicatorLp = indicatorLayout.getLayoutParams();
    		if(null == indicatorLp){
    			indicatorLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, indicatorHeight);
    		}else{
    			indicatorLp.height = indicatorHeight;
    		}
    		indicator.setLayoutParams(indicatorLp);
    	}
    	if(-1 != indicatorColor){
    		//调整背景颜色
    		indicator.setBackgroundColor(indicatorColor);
    	}
    	indicatorLayout.addView(indicator);
    }
    
    /**指示器和内容布局之间的分割线*/
    private void setupDividerFromIndicatorToTabContents(){
    	View divider = tabHost.getDividerFromIndicatorToTabContents(getContext());
    	if(null != divider){
    		addView(divider);
    	}
    }

    /** 内容部分布局 */
    private void setupContents() {
    	tabContentsLayout = tabHost.getTabContentsLayout(getContext());
    	addView(tabContentsLayout);
    	
        viewPage = new HackyViewPager(getContext());
        viewPage.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return tabContentList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(tabContentList.get(position));// 删除页卡
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(tabContentList.get(position), 0);// 添加页卡
                return tabContentList.get(position);
            }
        });
        viewPage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (currentPosition != position) {
                    currentPosition = position;
                    if (null != onPositionChangeListener) {
                        onPositionChangeListener.onPositionChange(currentPosition);
                    }
                }
                if (null != onPageChangeListener) {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int paddingLeft = position * indicatorWidth + positionOffsetPixels / tabList.size();
                indicatorLayout.setPadding(paddingLeft, 0, 0, 0);
                if (null != onPageChangeListener) {
                    onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (null != onPageChangeListener) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        tabContentsLayout.addView(viewPage);
    }

    /** 检查tabList和contentList这两个列表的长度必须要一样 */
    private void checkSizeEquals() {
        if (tabList.size() != tabContentList.size()) {
            throw new IllegalArgumentException("Size of tab must equals size of tabContent");
        }
    }

    ////////////////////////////////////内部接口/////////////////////////////////////////////
    /**
     * position位置变动通知监听
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-12-17 下午3:42:07 $
     */
    public static interface OnPositionChangeListener {
        void onPositionChange(int currentPosition);
    }

}
