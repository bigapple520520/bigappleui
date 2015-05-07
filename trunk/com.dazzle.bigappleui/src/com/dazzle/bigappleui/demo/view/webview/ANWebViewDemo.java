package com.dazzle.bigappleui.demo.view.webview;

import android.os.Bundle;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.view.webview.ANWebView;
import com.winupon.andframe.bigapple.ioc.InjectView;
import com.winupon.andframe.bigapple.ioc.app.AnActivity;

public class ANWebViewDemo extends AnActivity {
	@InjectView(R.id.webView)
	private ANWebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_anwebview);
		webView.getWebView().loadUrl("http://www.baidu.com");
	}

}
