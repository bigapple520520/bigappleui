package com.dazzle.bigappleui.view.webview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.dazzle.bigappleui.utils.ui.DisplayUtils;

/**
 * 自定义一个WebView，方便实用
 * 
 * @author xuan
 * 
 */
public class ANWebView extends RelativeLayout {
	private final Activity activity;
	/** 进度条高 */
	public static int webViewProgressBarHeight;
	/** 进度条颜色 */
	public static String webViewProgressBarColor;

	private ANWebViewProgressBar progressBar;
	private WebView webView;

	public ANWebView(Context context) {
		super(context);
		activity = (Activity) context;
		init();
	}

	public ANWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (Activity) context;
		init();
	}

	public ANWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		activity = (Activity) context;
		init();
	}

	private void init() {
		webViewProgressBarHeight = (int) DisplayUtils.getPxByDp(activity, 2);
		webViewProgressBarColor = "#53b53e";
		initProgressBar();
		initWebView();
	}

	private void initProgressBar() {
		progressBar = new ANWebViewProgressBar(activity);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				webViewProgressBarHeight);
		progressBar.setLayoutParams(lp);
		addView(progressBar);
	}

	private void initWebView() {
		webView = new WebView(activity);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		webView.setLayoutParams(lp);
		addView(webView);

		WebSettings webSettings = webView.getSettings();
		/** 支持缩放 */
		webSettings.setSupportZoom(true);
		/** 隐藏缩放按钮 */
		webSettings.setBuiltInZoomControls(true);
		webSettings.setUseWideViewPort(true);
		/** 支持JS执行 */
		webSettings.setJavaScriptEnabled(true);

		/** 设置点击页面上的连接时，使用原webView显示（默认启动第三方浏览器显示） */
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			};

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				progressBar.setVisibility(View.GONE);
			}
		});

		/** 获取页面标题title、加载进度设置 */
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String t) {
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressBar.updateProgress(newProgress);
			}
		});
	}

	/**
	 * 进度条控件
	 * 
	 * @return
	 */
	public ANWebViewProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * WebView控件
	 * 
	 * @return
	 */
	public WebView getWebView() {
		return webView;
	}

}
