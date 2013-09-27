/* 
 * @(#)Pull2RefreshUtils.java    Created on 2013-9-26
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.pull2refresh;

import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dazzle.bigappleui.utils.DateUtils;
import com.dazzle.bigappleui.utils.ResourceResidUtils;

/**
 * 封装一些简单的初始化，使用户使用更加简单
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-26 下午2:38:03 $
 */
public abstract class Pull2RefreshUtils {

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
     * @param headRefreshListener
     *            操作头部刷新
     * @param endRefreshListener
     *            操作尾部刷新
     */
    public static void simpleInit(Context context, final Pull2RefreshListView listView, final BaseAdapter baseAdapter,
            final Handler handler, final HeadRefreshListener headRefreshListener,
            final EndRefreshListener endRefreshListener) {

        boolean isInitFooter = null != endRefreshListener;

        // 初始化底部加载更多
        View pull2refresh_footer = null;
        TextView pull2refresh_footer_textview = null;
        ProgressBar pull2refresh_footer_progress = null;
        if (isInitFooter) {
            pull2refresh_footer = LayoutInflater.from(context).inflate(
                    ResourceResidUtils.getResidByLayoutName(context, "pull2refresh_footer"), null);
            pull2refresh_footer_textview = (TextView) pull2refresh_footer.findViewById(ResourceResidUtils
                    .getResidByIdName(context, "pull2refresh_footer_textview"));
            pull2refresh_footer_progress = (ProgressBar) pull2refresh_footer.findViewById(ResourceResidUtils
                    .getResidByIdName(context, "pull2refresh_footer_progressbar"));

            listView.addFooterView(pull2refresh_footer);
        }

        listView.setAdapter(baseAdapter);
        listView.setOnRefreshListener(new Pull2RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (null != headRefreshListener) {
                                headRefreshListener.headRefresh();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        // 刷新数据
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                baseAdapter.notifyDataSetChanged();
                                listView.onRefreshComplete("最新更新时间：" + DateUtils.date2StringBySecond(new Date()));
                            }
                        });
                    }
                }).start();
            }
        });

        // 滚动底部加载更多
        if (isInitFooter) {
            final View pull2refresh_footer_final = pull2refresh_footer;
            final TextView pull2refresh_footer_textview_final = pull2refresh_footer_textview;
            final ProgressBar pull2refresh_footer_progress_final = pull2refresh_footer_progress;
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    listView.onScrollStateChanged(view, scrollState);

                    // 判断是否滚动到底部
                    boolean scrollEnd = false;
                    try {
                        if (view.getPositionForView(pull2refresh_footer_final) == view.getLastVisiblePosition()) {
                            scrollEnd = true;
                        }
                    }
                    catch (Exception e) {
                        scrollEnd = false;
                        e.printStackTrace();
                    }

                    if (scrollEnd) {
                        pull2refresh_footer_textview_final.setText("加载中");
                        pull2refresh_footer_progress_final.setVisibility(View.VISIBLE);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 阻塞加载数据
                                if (null != endRefreshListener) {
                                    endRefreshListener.endRefresh();
                                }

                                // 刷新数据
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        baseAdapter.notifyDataSetChanged();
                                        pull2refresh_footer_textview_final.setText("更多");
                                        pull2refresh_footer_progress_final.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }).start();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    listView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            });
        }
    }

}
