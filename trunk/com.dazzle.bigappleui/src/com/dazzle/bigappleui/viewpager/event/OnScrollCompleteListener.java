package com.dazzle.bigappleui.viewpager.event;

/**
 * 滚动时，屏幕发生变化事件。观察者模式中的观察者。
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-12 下午8:17:58 $
 */
@Deprecated
public interface OnScrollCompleteListener {

    /**
     * 滚动完成，屏幕发生变化
     * 
     * @param scrollEvent
     */
    void onScrollComplete(ScrollEvent scrollEvent);
}
