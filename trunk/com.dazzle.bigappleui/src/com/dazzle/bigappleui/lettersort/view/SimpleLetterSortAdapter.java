/* 
 * @(#)DefaultLetterSortAdapter.java    Created on 2013-7-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.lettersort.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dazzle.bigappleui.lettersort.entity.ItemContent;
import com.dazzle.bigappleui.lettersort.entity.ItemDivide;

/**
 * 默认实现，效果很差，只是作为demo存在
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午5:00:36 $
 */
public class SimpleLetterSortAdapter extends LetterSortAdapter {
    private final Context context;

    public SimpleLetterSortAdapter(List<ItemContent> fromList, Context context) {
        super(fromList, context);
        this.context = context;
    }

    @Override
    public View drawItemContent(int position, View convertView, ViewGroup parent, ItemContent itemContent) {
        TextView textView = new TextView(context);
        textView.setTextSize(40);
        textView.setTextColor(Color.BLACK);
        textView.setText(itemContent.getName());
        return textView;
    }

    @Override
    public View drawItemDivide(int position, View convertView, ViewGroup parent, ItemDivide itemDivide) {
        TextView textView = new TextView(context);
        textView.setTextSize(20);
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.BLACK);
        textView.setText(itemDivide.getLetter());
        return textView;
    }

}
