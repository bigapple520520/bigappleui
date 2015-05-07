/* 
 * @(#)ScrollViewDemo.java    Created on 2014-11-20
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.pull2refresh;

import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dazzle.bigappleui.pullrefresh.core.PullToRefreshBase;
import com.dazzle.bigappleui.pullrefresh.widget.PullToRefreshScrollView;
import com.dazzle.bigappleui.utils.DateUtils;

/**
 * 基于ScrollView的下拉刷新
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-20 上午10:49:52 $
 */
public class ScrollViewDemo extends Activity {
    private PullToRefreshScrollView pullToRefreshScrollView;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pullToRefreshScrollView = new PullToRefreshScrollView(this);
        setContentView(pullToRefreshScrollView);

        scrollView = pullToRefreshScrollView.getRefreshableView();

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.GRAY);
        scrollView.addView(root);

        for (int i = 0; i < 100; i++) {
            TextView textView = new TextView(this);
            textView.setText("哈哈");
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(20);
            root.addView(textView);
        }

        pullToRefreshScrollView.setPullRefreshEnabled(true);// 允许下拉刷新
        pullToRefreshScrollView.setPullLoadEnabled(true);// 允许上拉加载更多
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
            }
        });
    }

    /**
     * 模拟网络请求
     * 
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-11-20 上午10:27:00 $
     */
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            pullToRefreshScrollView.onPullDownRefreshComplete();
            pullToRefreshScrollView.onPullUpRefreshComplete();
            pullToRefreshScrollView.setLastUpdatedLabel(DateUtils.date2StringBySecond(new Date()));
            super.onPostExecute(result);
        }
    }

}
