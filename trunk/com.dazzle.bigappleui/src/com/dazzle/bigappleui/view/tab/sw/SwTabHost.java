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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.dazzle.bigappleui.utils.LogUtils;
import com.dazzle.bigappleui.utils.ui.DisplayUtils;
import com.dazzle.bigappleui.view.photoview.app.core.HackyViewPager;

/**
 * 基于LinearLayout的Tab切换控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-16 下午3:44:41 $
 */
public class SwTabHost extends LinearLayout {
    private static final String TAG = "SwTabHost";

    public static int DEFAULT_TABSLAYOUT_HEIGHT;
    public static int DEFAULT_INDICATE_HEIGHT;

    /** 当前位置 */
    private int position;

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
    private int indicateColor;
    /** 指示器容器背景颜色 */
    private int indicateLayoutColor;

    /** 具体tab页内容容器 */
    private FrameLayout tabContentsLayout;
    /** 内容可以进行滑动切换 */
    private HackyViewPager viewPage;

    /** 供外部调用的监听 */
    private OnPageChangeListener onPageChangeListener;

    /** 记录着上一次的滑动位置比例 */
    private float lastPositionOffset;
    /** 记录指示器容器的paddingLeft值 */
    private int indicateLayoutPaddingLeft;

    public SwTabHost(Context context) {
        super(context);
        init(context);
    }

    public SwTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwTabHost(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

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
        indicateLayoutColor = Color.WHITE;
        LinearLayout.LayoutParams indicateLayoutLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                indicateHeight);
        indicateLayout.setLayoutParams(indicateLayoutLp);
        indicateLayout.setBackgroundColor(indicateLayoutColor);
        addView(indicateLayout);

        indicateColor = Color.BLACK;
        indicate = new View(getContext());
        FrameLayout.LayoutParams indicateLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        indicate.setLayoutParams(indicateLp);
        indicate.setBackgroundColor(indicateColor);
        indicateLayout.addView(indicate);

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
        setupTabs();
        setupIndicate();
        setupContents();
    }

    /** tabs部分布局 */
    private void setupTabs() {
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

        indicate.setBackgroundColor(indicateColor);
        indicateLayout.setBackgroundColor(indicateLayoutColor);
    }

    /** 内容部分布局 */
    private void setupContents() {
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
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (0 != lastPositionOffset) {
                    float intervalRadio = positionOffset - lastPositionOffset;
                    LogUtils.d(TAG, "intervalRadio:" + intervalRadio);

                    indicateLayoutPaddingLeft = (int) (indicateLayoutPaddingLeft + intervalRadio * indicateWidth);
                    indicateLayout.setPadding(indicateLayoutPaddingLeft, 0, 0, 0);
                    indicateLayout.invalidate();
                }

                lastPositionOffset = positionOffset;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
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

    // //////////////////////////////////////监听设置///////////////////////////////////////////////////
    public OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    // //////////////////////////////////position///////////////////////////////////////////////
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    // /////////////////////////////////添加内容方法//////////////////////////////////////////////
    public void addTabsAndContents(int index, List<View> tabList, List<View> contentList) {
        tabList.addAll(index, tabList);
        tabContentList.addAll(index, contentList);
    }

    public void addTabAndContent(int index, View tab, View content) {
        tabList.add(index, tab);
        tabContentList.add(index, content);
    }

    public void addTabsAndContents(List<View> tabList, List<View> contentList) {
        tabList.addAll(tabList);
        tabContentList.addAll(contentList);
    }

    public void addTabAndContent(View tab, View content) {
        tabList.add(tab);
        tabContentList.add(content);
    }

    public void clearTabAndContent() {
        tabList.clear();
        tabContentList.clear();
    }

    public int getTabsLayoutHeight() {
        return tabsLayoutHeight;
    }

    public void setTabsLayoutHeight(int tabsLayoutHeight) {
        this.tabsLayoutHeight = tabsLayoutHeight;
    }

    public int getIndicateHeight() {
        return indicateHeight;
    }

    public void setIndicateHeight(int indicateHeight) {
        this.indicateHeight = indicateHeight;
    }

    public int getIndicateColor() {
        return indicateColor;
    }

    public void setIndicateColor(int indicateColor) {
        this.indicateColor = indicateColor;
    }

    public int getIndicateLayoutColor() {
        return indicateLayoutColor;
    }

    public void setIndicateLayoutColor(int indicateLayoutColor) {
        this.indicateLayoutColor = indicateLayoutColor;
    }

}
