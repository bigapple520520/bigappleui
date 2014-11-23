/* 
 * @(#)Pull2RefreshUtils.java    Created on 2013-9-26
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.pull2refresh;

import java.util.Date;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dazzle.bigappleui.pull2refresh.PullToRefreshListView;
import com.dazzle.bigappleui.utils.DateUtils;
import com.dazzle.bigappleui.utils.M;

/**
 * 封装一些简单的初始化，使用户使用更加简单
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-26 下午2:38:03 $
 */
public abstract class PullToRefreshUtils {

    /**
     * 默认初始化下拉刷新
     * 
     * @param context
     *            上下文
     * @param listView
     *            列表控件
     * @param baseAdapter
     *            列表数据源
     * @param handler
     *            UI线程handler
     */
    public static void simpleInit(Context context, final PullToRefreshListView listView,
            final Pull2RefreshDemoAdapter baseAdapter, final Handler handler) {

        // 添加底部加载更多View
        final View pull2refresh_footer = LayoutInflater.from(context).inflate(M.layout(context, "pull2refresh_footer"),
                null);
        final TextView pull2refresh_footer_textview = (TextView) pull2refresh_footer.findViewById(M.id(context,
                "pull2refresh_footer_textview"));
        final ProgressBar pull2refresh_footer_progress = (ProgressBar) pull2refresh_footer.findViewById(M.id(context,
                "pull2refresh_footer_progressbar"));
        listView.addFooterView(pull2refresh_footer);

        // 设置下拉刷新事件
        listView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        try {
                            // 模式耗时操作,睡个3秒
                            Thread.sleep(3000);
                            baseAdapter.getDataList().add(0, "我新出来的：" + DateUtils.date2StringBySecond(new Date()));
                        }
                        catch (Exception e) {
                            // Ignore
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result) {
                        baseAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete("最新更新：" + DateUtils.date2StringBySecond(new Date()));
                    }
                }.execute();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                listView.onScrollStateChanged(view, scrollState);

                // 判断是否滚动到底部
                boolean scrollEnd = false;
                if (view.getLastVisiblePosition() == view.getCount() - 1
                        && listView.getVisibleItemCount() != view.getCount()) {
                    scrollEnd = true;

                    if (View.VISIBLE != pull2refresh_footer.getVisibility()) {
                        pull2refresh_footer.setVisibility(View.VISIBLE);
                    }
                }

                if (scrollEnd) {
                    pull2refresh_footer_textview.setText("努力加载中...");
                    pull2refresh_footer_progress.setVisibility(View.VISIBLE);

                    new AsyncTask<Object, Object, Object>() {
                        @Override
                        protected Object doInBackground(Object... params) {
                            try {
                                // 模式耗时操作,睡个3秒
                                Thread.sleep(3000);
                                baseAdapter.getDataList().add("我更多拉出来的：" + DateUtils.date2StringBySecond(new Date()));
                            }
                            catch (Exception e) {
                                // Ignore
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object result) {
                            baseAdapter.notifyDataSetChanged();
                            pull2refresh_footer_textview.setText("更多");
                            pull2refresh_footer_progress.setVisibility(View.GONE);
                        }
                    }.execute();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                listView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                if (visibleItemCount == totalItemCount) {
                    pull2refresh_footer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}
