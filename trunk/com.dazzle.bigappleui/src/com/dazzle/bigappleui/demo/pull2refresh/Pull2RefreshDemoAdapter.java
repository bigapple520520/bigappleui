/* 
 * @(#)Pull2RefreshDemoAdapter.java    Created on 2013-9-26
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.pull2refresh;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * demo设配器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-26 下午4:55:20 $
 */
public class Pull2RefreshDemoAdapter extends BaseAdapter {
	private List<String> dataList;
	private final Context context;

	public Pull2RefreshDemoAdapter(List<String> dataList, Context context) {
		this.dataList = dataList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String data = dataList.get(position);

		TextView textView = new TextView(context);
		textView.setHeight(100);
		textView.setText(data);

		return textView;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public List<String> getDataList() {
		return dataList;
	}

	public void setDataList(List<String> dataList) {
		this.dataList = dataList;
	}

}
