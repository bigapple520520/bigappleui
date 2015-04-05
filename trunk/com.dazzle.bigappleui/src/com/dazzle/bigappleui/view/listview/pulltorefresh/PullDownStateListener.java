package com.dazzle.bigappleui.view.listview.pulltorefresh;

/**
 * 下拉刷新各种动作监听
 * 
 * @author xuan
 * 
 */
public interface PullDownStateListener {
	/**
	 * 释放刷新状态
	 * 
	 * @param headerView
	 */
	void releaseToRefresh(HeaderView headerView);

	/**
	 * 下拉刷新状态
	 * 
	 * @param headerView
	 */
	void pullToRefresh(HeaderView headerView);

	/**
	 * 刷新中状态
	 * 
	 * @param headerView
	 */
	void refreshing(HeaderView headerView);

	/**
	 * 刷新完成状态
	 * 
	 * @param headerView
	 */
	void done(HeaderView headerView);
}
