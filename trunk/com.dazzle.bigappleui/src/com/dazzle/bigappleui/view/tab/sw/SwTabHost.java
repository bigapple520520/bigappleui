/* 
 * @(#)SwTabHost.java    Created on 2014-12-16
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.view.tab.sw;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dazzle.bigappleui.utils.ui.DisplayUtils;
import com.dazzle.bigappleui.view.photoview.app.core.HackyViewPager;

/**
 * 基于LinearLayout的Tab切换控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-16 下午3:44:41 $
 */
public class SwTabHost extends LinearLayout {
    public static int DEFAULT_TABSLAYOUT_HEIGHT;
    public static int DEFAULT_INDICATE_HEIGHT;

    /** 当前位置 */
    private int currentPosition = -1;

    /** tab的View数组 */
    private List<View> tabList = new ArrayList<View>();
    /** tab内容页View数组 */
    private List<View> tabContentList = new ArrayList<View>();

    /** tab的布局容器 */
    private LinearLayout tabsLayout;
    private int tabsLayoutHeight;

    /** 下标指示容器 */
    private FrameLayout indicateLayout;
    /** 指示器View */
    private View indicate;
    /** 指示器View的高度 */
    private int indicateHeight;
    /** 指示器View的宽度 */
    private int indicateWidth;
    /** 指示器的颜色 */
    private int indicateColor = Color.BLACK;
    /** 指示器容器背景颜色 */
    private int indicateLayoutColor = Color.WHITE;

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
        DEFAULT_TABSLAYOUT_HEIGHT = (int) DisplayUtils.getPxByDp((Activity) context, 50);
        DEFAULT_INDICATE_HEIGHT = (int) DisplayUtils.getPxByDp((Activity) context, 5);
        tabsLayoutHeight = DEFAULT_TABSLAYOUT_HEIGHT;
        indicateHeight = DEFAULT_INDICATE_HEIGHT;

        // tebs布局容器
        tabsLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams tabsLayoutLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                tabsLayoutHeight);
        tabsLayout.setLayoutParams(tabsLayoutLp);
        tabsLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(tabsLayout);

        // 指示器容器
        indicateLayout = new FrameLayout(getContext());
        LinearLayout.LayoutParams indicateLayoutLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                indicateHeight);
        indicateLayout.setLayoutParams(indicateLayoutLp);
        indicateLayout.setBackgroundColor(indicateLayoutColor);
        addView(indicateLayout);

        // 内容布局容器
        tabContentsLayout = new FrameLayout(getContext());
        LinearLayout.LayoutParams tabContentsLayoutLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tabContentsLayout.setLayoutParams(tabContentsLayoutLp);
        addView(tabContentsLayout);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // w只能在这里取到，所以要在这里调整indicate的宽度
        FrameLayout.LayoutParams indicateLp = (FrameLayout.LayoutParams) indicate.getLayoutParams();
        indicateWidth = w / tabList.size();
        indicateLp.width = indicateWidth;
        indicate.setLayoutParams(indicateLp);
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
                indicateLayout.setPadding(position * indicateWidth, 0, 0, 0);
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

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 清理所有View
     */
    public void clearTabAndContent() {
        tabList.clear();
        tabContentList.clear();
    }

    /**
     * 设置tab的高度
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
    public void setIndicateHeight(int indicateHeight) {
        this.indicateHeight = indicateHeight;
    }

    /**
     * 设置指示器的颜色
     * 
     * @param indicateColor
     *            颜色int值
     */
    public void setIndicateColor(int indicateColor) {
        this.indicateColor = indicateColor;
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
     * 可以自己设置指示器的View
     * 
     * @param view
     */
    public void setIndicate(View view) {
        this.indicate = view;
    }

    // ///////////////////////////////////////////高度定制时用到//////////////////////////////////////////////////////
    /**
     * 获取指示器的容器对象
     * 
     * @return
     */
    public FrameLayout getIndicateLayout() {
        return indicateLayout;
    }

    // /////////////////////////////////////////////内部辅助方法///////////////////////////////////////////////////////
    /** tabs部分布局 */
    private void setupTabs() {
        tabsLayout.removeAllViews();
        if (DEFAULT_TABSLAYOUT_HEIGHT != tabsLayoutHeight) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabsLayout.getLayoutParams();
            lp.height = tabsLayoutHeight;
            tabsLayout.setLayoutParams(lp);
        }

        for (View view : tabList) {
            LinearLayout.LayoutParams viewLp = new LinearLayout.LayoutParams(0, tabsLayoutHeight);
            viewLp.weight = 1;
            view.setLayoutParams(viewLp);
            tabsLayout.addView(view);
        }
    }

    /** 指示条部分布局 */
    private void setupIndicate() {
        if (DEFAULT_INDICATE_HEIGHT != indicateHeight) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) indicateLayout.getLayoutParams();
            lp.height = indicateHeight;
            indicateLayout.setLayoutParams(lp);
        }

        if (null == indicate) {
            indicate = new View(getContext());
            FrameLayout.LayoutParams indicateLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            indicate.setLayoutParams(indicateLp);
            indicate.setBackgroundColor(indicateColor);
        }
        indicateLayout.addView(indicate);
        indicateLayout.setBackgroundColor(indicateLayoutColor);
    }

    /** 内容部分布局 */
    private void setupContents() {
        tabContentsLayout.removeAllViews();
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
                int paddingLeft = position * indicateWidth + positionOffsetPixels / tabList.size();
                indicateLayout.setPadding(paddingLeft, 0, 0, 0);
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
