package com.dazzle.bigappleui.pullrefresh.core;

/**
 * 下拉刷新和上拉加载更多的界面接口
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-13 下午3:48:40 $
 */
public interface ILoadingLayout {
    /**
     * 设置当前状态，派生类应该根据这个状态的变化来改变View的变化
     * 
     * @param state
     *            状态
     */
    public void setState(State state);

    /**
     * 得到当前的状态
     * 
     * @return 状态
     */
    public State getState();

    /**
     * 在拉动时调用
     * 
     * @param scale
     *            拉动的比例
     */
    public void onPull(float scale);

    /**
     * 下拉刷新各个过程的状态
     */
    public enum State {

        /**
         * 初始化状态
         */
        NONE,

        /**
         * 这个状态下不会跟刷新方法有交互
         */
        RESET,

        /**
         * UI处于下拉刷新状态，如果在这个状态放手，不会触发刷新的
         */
        PULL_TO_REFRESH,

        /**
         * UI处于放手刷新状态，如果在这个状态放手，就会触发刷新的
         */
        RELEASE_TO_REFRESH,

        /**
         * 当前正在刷新状态
         */
        REFRESHING,

        /**
         * 没有更多数据状态
         */
        NO_MORE_DATA,
    }

}
