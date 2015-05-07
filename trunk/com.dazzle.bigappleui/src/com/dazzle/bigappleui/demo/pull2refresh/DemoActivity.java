package com.dazzle.bigappleui.demo.pull2refresh;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.pull2refresh.PullToRefreshListView;

/**
 * 下拉刷新控件，使用时，需要引入<br>
 * 一个箭头图片：pull2refresh_arrow.png<br>
 * 一个头部布局文件：pull2refresh_footer.xml<br>
 * 一个尾部布局文件：pull2refresh_head.xml（这个不是必须的，只有想用尾部刷新时，引入即可）
 * 
 * @author xuan
 */
public class DemoActivity extends Activity {

	private PullToRefreshListView listView;
	private Pull2RefreshDemoAdapter baseAdapter;
	private List<String> dataList;

	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_pull2refresh_main);
		Button scrollViewBtn = (Button) findViewById(R.id.scrollViewBtn);
		scrollViewBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(DemoActivity.this,
						ScrollViewDemo.class));
			}
		});

		Button webviewBtn = (Button) findViewById(R.id.webviewBtn);
		webviewBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(DemoActivity.this, WebViewDemo.class));
			}
		});

		Button listViewBtn = (Button) findViewById(R.id.listViewBtn);
		listViewBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(DemoActivity.this, ListViewDemo.class));
			}
		});

		Button gridViewBtn = (Button) findViewById(R.id.gridViewBtn);
		gridViewBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(DemoActivity.this, GridViewDemo.class));
			}
		});

		Button anPullToRefreshListViewBtn = (Button) findViewById(R.id.anPullToRefreshListViewBtn);
		anPullToRefreshListViewBtn
				.setOnClickListener(new Button.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(DemoActivity.this,
								ANPullToRefreshViewDemoActivity.class));
					}
				});

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listView.clickRefresh();
			}
		});

		dataList = new ArrayList<String>();
		dataList.add("二楼是王八");
		dataList.add("我是二楼");
		dataList.add("二楼在逗比吧");
		dataList.add("二楼威武");
		dataList.add("我是二楼爷爷的儿子");
		dataList.add("唉，你们都这么空");
		dataList.add("是你妈逼的");
		dataList.add("真的是你妈逼的，不是你爸");
		dataList.add("我是逗比");
		dataList.add("少年你太天真了，你以为真有这么多人给你回复啊，其实是我换了个账号一个人在回复，不信我换个账号再发个同样的话");
		dataList.add("少年你太天真了，你以为真有这么多人给你回复啊，其实是我换了个账号一个人在回复，不信我换个账号再发个同样的话");
		dataList.add("少年你太天真了，你以为真有这么多人给你回复啊，其实是我换了个账号一个人在回复，不信我换个账号再发个同样的话");

		baseAdapter = new Pull2RefreshDemoAdapter(dataList, this);
		listView = (PullToRefreshListView) findViewById(R.id.listview);
		listView.setAdapter(baseAdapter);

		// 最简单的用法
		PullToRefreshUtils.simpleInit(this, listView, baseAdapter, handler);
	}

}
