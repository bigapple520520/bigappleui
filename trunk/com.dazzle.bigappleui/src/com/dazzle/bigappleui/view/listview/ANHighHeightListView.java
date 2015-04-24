/* 
 * @(#)MyGridView.java    Created on 2013-6-25
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id: MyGridView.java 39747 2013-06-26 07:41:38Z shenyc $
 */
package com.dazzle.bigappleui.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 定义一个超级高的ListView，使其在ScrollView等滚动的控件中能显示所有item
 * 
 * @author xuan
 */
public class ANHighHeightListView extends ListView {
	public ANHighHeightListView(Context context) {
		super(context);
	}

	public ANHighHeightListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ANHighHeightListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
