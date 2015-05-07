/* 
 * @(#)LetterSortView.java    Created on 2013-7-15
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.lettersort.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dazzle.bigappleui.lettersort.view.LetterSortBar.OnLetterChange;
import com.dazzle.bigappleui.lettersort.view.LetterSortBar.OutLetterSeacherBar;

/**
 * 按字母排序分类，并用字母做检索
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-15 下午7:12:47 $
 */
public class LetterSortView extends ViewGroup {
    private final Context context;

    private LetterSortBar letterSortBar;// 字母条控件
    private TextView letterShow;// 字母提示控件
    private ListView listView;// 数据显示列表控件

    private int LetterSortBarWidth = 40;// 字母条的宽度，单位px
    private int LetterShowWidth = 100;// 显示字母提示的宽度，单位px

    public LetterSortView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSortView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    // 添加一些子控件初始化
    private void initView() {
        // 添加ListView
        if (null == listView) {
            listView = new ListView(context);
            listView.setVerticalScrollBarEnabled(false);
            // listView.setDividerHeight(0);
        }
        addView(listView);

        // 添加字母条
        if (null == letterSortBar) {
            letterSortBar = new LetterSortBar(context);

            letterSortBar.setOnLetterChange(new OnLetterChange() {
                @Override
                public void letterChange(String letter) {
                    letterShow.setText(letter);
                    if (letterShow.getVisibility() != View.VISIBLE) {
                        letterShow.setVisibility(View.VISIBLE);
                    }

                    // 定位ListView的显示区域
                    LetterSortAdapter lsa = (LetterSortAdapter) listView.getAdapter();
                    Integer indexInteger = lsa.getIndexMap().get(letter);
                    final int index = (null == indexInteger) ? -1 : indexInteger;

                    listView.setSelection(index);
                    listView.requestFocusFromTouch();
                }
            });

            letterSortBar.setOutLetterSeacherBar(new OutLetterSeacherBar() {
                @Override
                public void outBar(String lastLetter) {
                    letterShow.setVisibility(View.GONE);
                }
            });
        }
        addView(letterSortBar);

        // 添加字母提示
        if (null == letterShow) {
            letterShow = new TextView(context);
            letterShow.setTextSize(50);
            letterShow.setTextColor(Color.WHITE);
            letterShow.setBackgroundColor(Color.BLACK);
            letterShow.setGravity(Gravity.CENTER);
            letterShow.setVisibility(View.GONE);
            TextPaint tp = letterShow.getPaint();
            tp.setFakeBoldText(true);
        }
        addView(letterShow);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (null != listView) {
            listView.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        final int width = getWidth();
        final int height = getHeight();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);

            if (childView instanceof ListView) {
                childView.layout(0, 0, width, height);
            }
            else if (childView instanceof LetterSortBar) {
                childView.layout(width - LetterSortBarWidth, 0, width, height);
            }
            else if (childView instanceof TextView) {
                childView.layout((width - LetterShowWidth) / 2, (height - LetterShowWidth) / 2,
                        (width + LetterShowWidth) / 2, (height + LetterShowWidth) / 2);
            }
        }
    }

    // //////////////////////////////////////////右侧字母栏//////////////////////////////////////////////////////////////
    public LetterSortBar getLetterSortBar() {
        return letterSortBar;
    }

    public void setLetterSortBar(LetterSortBar letterSortBar) {
        removeView(this.letterSortBar);
        this.letterSortBar = letterSortBar;
        addView(letterSortBar, 1);
    }

    public int getLetterSortBarWidth() {
        return LetterSortBarWidth;
    }

    public void setLetterSortBarWidth(int letterSortBarWidth) {
        LetterSortBarWidth = letterSortBarWidth;
    }

    // ///////////////////////////////////////////////设置显示字母部分/////////////////////////////////////////////////
    public TextView getLetterShow() {
        return letterShow;
    }

    public void setLetterShow(TextView letterShow) {
        removeView(this.letterShow);
        this.letterShow = letterShow;
        addView(letterShow, 2);
        letterShow.setVisibility(View.GONE);
    }

    public int getLetterShowWidth() {
        return LetterShowWidth;
    }

    public void setLetterShowWidth(int letterShowWidth) {
        LetterShowWidth = letterShowWidth;
    }

    // ////////////////////////////////////////////数据列表设置部分/////////////////////////////////////////////////////
    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        removeView(this.listView);
        this.listView = listView;
        addView(listView, 0);
    }

}
