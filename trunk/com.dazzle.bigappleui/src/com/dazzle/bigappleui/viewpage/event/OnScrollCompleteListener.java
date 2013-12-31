package com.dazzle.bigappleui.viewpage.event;

/**
 * 滚动时，屏幕发生变化事件。观察者模式中充到观察者要实现的接口，以便当事件发生变化时更新自己。
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-12 下午8:17:58 $
 */
public interface OnScrollCompleteListener {

    /**
     * 滚动完成，屏幕发生变化
     * 
     * @param scrollEvent
     */
    void onScrollComplete(ScrollEvent scrollEvent);
}
