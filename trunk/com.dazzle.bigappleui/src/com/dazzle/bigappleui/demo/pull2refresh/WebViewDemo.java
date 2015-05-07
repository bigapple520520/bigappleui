/* 
 * @(#)WebViewDemo.java    Created on 2014-11-20
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.demo.pull2refresh;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dazzle.bigappleui.pullrefresh.core.PullToRefreshBase;
import com.dazzle.bigappleui.pullrefresh.widget.PullToRefreshWebView;
import com.dazzle.bigappleui.utils.DateUtils;

/**
 * 基于WebView实现下拉刷新
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-20 上午9:38:59 $
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebViewDemo extends Activity {
    private PullToRefreshWebView pullToRefreshWebView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pullToRefreshWebView = new PullToRefreshWebView(this);
        setContentView(pullToRefreshWebView);

        webView = pullToRefreshWebView.getRefreshableView();
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            };
        });
        webView.loadUrl("http://zj.qq.com");

        pullToRefreshWebView.setPullRefreshEnabled(true);// 允许下拉刷新
        pullToRefreshWebView.setPullLoadEnabled(true);// 允许上拉加载更多
        pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<WebView> refreshView) {
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {
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
            pullToRefreshWebView.onPullDownRefreshComplete();
            pullToRefreshWebView.onPullUpRefreshComplete();
            pullToRefreshWebView.setLastUpdatedLabel(DateUtils.date2StringBySecond(new Date()));
            super.onPostExecute(result);
        }
    }

}
