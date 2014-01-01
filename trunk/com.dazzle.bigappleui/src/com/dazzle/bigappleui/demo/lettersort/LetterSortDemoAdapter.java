/* 
 * @(#)DefaultLetterSortAdapter.java    Created on 2013-7-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.lettersort;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.lettersort.entity.ItemContent;
import com.dazzle.bigappleui.lettersort.entity.ItemDivide;
import com.dazzle.bigappleui.lettersort.view.LetterSortAdapter;
import com.dazzle.bigappleui.view.SwipeView;
import com.dazzle.bigappleui.view.SwipeView.SwipeCompleteListener;

/**
 * 字母排序列表控件demo的数据适配器实现
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-16 下午5:00:36 $
 */
public class LetterSortDemoAdapter extends LetterSortAdapter {
	private final Context context;

	public LetterSortDemoAdapter(List<ItemContent> fromList, Context context) {
		super(fromList);
		this.context = context;
	}

	@Override
	public View drawItemContent(int position, View convertView,
			ViewGroup parent, ItemContent itemContent) {
		// 侧滑的正面部分
		View content = LayoutInflater.from(context).inflate(
				R.layout.demo_swipe_above, null);
		TextView leftText = (TextView) content.findViewById(R.id.leftText);
		leftText.setText(itemContent.getName());
		View contentBg = content.findViewById(R.id.itemBg);

		// 侧滑的背后部分
		View behind = LayoutInflater.from(context).inflate(
				R.layout.demo_swipe_behind, null);

		// 侧滑容器
		final SwipeView swipeView = new SwipeView(context);
		swipeView.addContentAndBehind(content, behind);
		swipeView.setBehindWidthRes(R.dimen.demo_swipe_behind_offset);
		swipeView.setSwipeCompleteListener(new SwipeCompleteListener() {
			@Override
			public void whichScreen(int which) {
				if (which == SwipeView.CURSCREEN_CONTENT) {
					Toast.makeText(context, "我侧滑到了content页面",
							Toast.LENGTH_SHORT).show();
				} else if (which == SwipeView.CURSCREEN_BEHIND) {
					Toast.makeText(context, "我侧滑到了behind页面", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

		contentBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SwipeView.CURSCREEN_BEHIND == swipeView.getCurScreen()) {
					swipeView.snapToScreen(SwipeView.CURSCREEN_CONTENT);
				}
			}
		});

		behind.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "我点击了删除", Toast.LENGTH_SHORT).show();
			}
		});

		return swipeView;
	}

	@Override
	public View drawItemDivide(int position, View convertView,
			ViewGroup parent, ItemDivide itemDivide) {
		TextView textView = (TextView) LayoutInflater.from(context).inflate(
				R.layout.demo_lettersort_item_split, null);
		textView.setText(itemDivide.getLetter());
		return textView;
	}

}
