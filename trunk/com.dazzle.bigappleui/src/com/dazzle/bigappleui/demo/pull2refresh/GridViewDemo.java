/* 
 * @(#)GridViewDemo.java    Created on 2014-11-21
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.pull2refresh;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.dazzle.bigappleui.pullrefresh.core.PullToRefreshBase;
import com.dazzle.bigappleui.pullrefresh.widget.PullToRefreshGridView;

/**
 * 基于GridViewDemo实现下拉刷新
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-17 上午11:45:35 $
 */
public class GridViewDemo extends Activity {
    private List<String> dataList = new LinkedList<String>();

    private GridView gridView;
    private BaseAdapter adapter;
    private PullToRefreshGridView pullToRefreshGridView;

    private boolean mIsStart;
    private int i = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        pullToRefreshGridView = new PullToRefreshGridView(this);
        setContentView(pullToRefreshGridView);

        gridView = pullToRefreshGridView.getRefreshableView();
        gridView.setNumColumns(3);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        gridView.setAdapter(adapter);

        pullToRefreshGridView.setPullRefreshEnabled(true);
        pullToRefreshGridView.setPullLoadEnabled(true);

        // 设置下拉刷新和上拉加载回调
        pullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                mIsStart = true;
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                new GetDataTask().execute();
            }
        });
    }

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
            if (mIsStart) {
                dataList.add(0, "下拉刷新");
            }
            else {
                dataList.add("上拉加载");
            }

            adapter.notifyDataSetChanged();
            pullToRefreshGridView.onPullDownRefreshComplete();
            pullToRefreshGridView.onPullUpRefreshComplete();
            // mPullListView.setHasMoreData(hasMoreData);
            pullToRefreshGridView.setLastUpdatedLabel("哈哈");
            i--;
            if (i <= 0) {
                pullToRefreshGridView.setHasMoreData(false);
            }
            super.onPostExecute(result);
        }
    }

    private void initData() {
        dataList.add("1111111");
        dataList.add("222222");
        dataList.add("333333");
        dataList.add("44444444");
        dataList.add("5555555");
        dataList.add("6666666");
        dataList.add("7777777");
        dataList.add("8888888");
        dataList.add("99999999");
        dataList.add("1111111");
        dataList.add("1111111");
        dataList.add("1111111");
        dataList.add("222222");
        dataList.add("333333");
        dataList.add("44444444");
        dataList.add("5555555");
        dataList.add("6666666");
        dataList.add("7777777");
        dataList.add("8888888");
        dataList.add("99999999");
        dataList.add("1111111");
        dataList.add("1111111");
        dataList.add("1111111");
        dataList.add("222222");
        dataList.add("333333");
        dataList.add("44444444");
        dataList.add("5555555");
        dataList.add("6666666");
        dataList.add("7777777");
        dataList.add("8888888");
        dataList.add("99999999");
        dataList.add("1111111");
        dataList.add("1111111");
        dataList.add("1111111");
        dataList.add("222222");
        dataList.add("333333");
        dataList.add("44444444");
        dataList.add("5555555");
        dataList.add("6666666");
        dataList.add("7777777");
        dataList.add("8888888");
        dataList.add("99999999");
        dataList.add("1111111");
        dataList.add("1111111");
    }

}
