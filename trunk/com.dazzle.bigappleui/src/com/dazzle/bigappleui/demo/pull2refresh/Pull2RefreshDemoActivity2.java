/* 
 * @(#)Pull2RefreshDemoActivity2.java    Created on 2014-11-17
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.pull2refresh;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.pullrefresh.widget.PullToRefreshListView;

/**
 * 这个类写了一个效果更不错的下拉刷新的demo
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:45:35 $
 */
public class Pull2RefreshDemoActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_pull2refresh_main2);

        PullToRefreshListView pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        ListView listView = pullToRefreshListView.getRefreshableView();
        listView.setAdapter(new BaseAdapter() {
            @Override
            public View getView(int arg0, View arg1, ViewGroup arg2) {
                TextView textView = new TextView(Pull2RefreshDemoActivity2.this);
                textView.setTextSize(30);
                textView.setText("发发生发撒旦法撒旦法");
                return textView;
            }

            @Override
            public long getItemId(int arg0) {
                return 0;
            }

            @Override
            public Object getItem(int arg0) {
                return null;
            }

            @Override
            public int getCount() {
                return 10;
            }
        });
    }

}
