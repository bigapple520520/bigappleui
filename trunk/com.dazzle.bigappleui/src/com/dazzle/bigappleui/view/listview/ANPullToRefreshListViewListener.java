package com.dazzle.bigappleui.view.listview;

/**
 * 刷新监听事件
 * 
 * @author xuan
 * 
 */
public interface ANPullToRefreshListViewListener {
	/**
	 * 下拉刷新
	 */
	void onPullDownRefresh();

	/**
	 * 上拉加载更多
	 */
	void onScrollUpRefresh();
}
