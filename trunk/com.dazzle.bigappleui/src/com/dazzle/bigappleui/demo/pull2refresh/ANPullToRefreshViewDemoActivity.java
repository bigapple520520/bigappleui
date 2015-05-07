package com.dazzle.bigappleui.demo.pull2refresh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.dazzle.bigappleui.R;
import com.dazzle.bigappleui.utils.DateUtils;
import com.dazzle.bigappleui.view.listview.ANPullToRefreshListView;
import com.dazzle.bigappleui.view.listview.ANPullToRefreshListViewListener;

/**
 * ANPullToRefreshListView DEMO
 * 
 * @author xuan
 */
public class ANPullToRefreshViewDemoActivity extends Activity {

	private ANPullToRefreshListView pullToRefreshListView;
	private Pull2RefreshDemoAdapter baseAdapter;
	private List<String> dataList;

	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_anpulltorefreshview);
		initData();
		initWidgets();
	}

	private void initData() {
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
	}

	private void initWidgets() {
		pullToRefreshListView = (ANPullToRefreshListView) findViewById(R.id.pullToRefreshListView);
		baseAdapter = new Pull2RefreshDemoAdapter(dataList, this);
		pullToRefreshListView.setAdapter(baseAdapter);

		pullToRefreshListView.setCanScrollUp(true);
		// 设置下拉刷新事件
		pullToRefreshListView
				.setPullToRefreshListViewListener(new ANPullToRefreshListViewListener() {
					@Override
					public void onScrollUpRefresh() {
						refreshData(false);
					}

					@Override
					public void onPullDownRefresh() {
						refreshData(true);
					}
				});
	}

	// 模拟加载数据
	private void refreshData(final boolean isPullDown) {
		new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... params) {
				try {
					// 模式耗时操作,睡个2秒
					Thread.sleep(2000);
					String text = null;
					if (isPullDown) {
						text = "我下拉刷新出来的："
								+ DateUtils.date2StringBySecond(new Date());
						baseAdapter.getDataList().add(0, text);
					} else {
						text = "我上滚加载更多出来的："
								+ DateUtils.date2StringBySecond(new Date());
						baseAdapter.getDataList().add(text);
					}
				} catch (Exception e) {
					// Ignore
				}

				return null;
			}

			@Override
			protected void onPostExecute(Object result) {
				baseAdapter.notifyDataSetChanged();
				if (isPullDown) {
					pullToRefreshListView.onPullDownRefreshComplete("最后更新："
							+ DateUtils.date2StringBySecond(new Date()));
				} else {
					// pullToRefreshListView.onScrollUpRefreshComplete("上滚加载更多");
					pullToRefreshListView.onScrollUpNoMoreData("没有更多数据了");
				}
			}
		}.execute();
	}

}
