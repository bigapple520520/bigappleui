package com.dazzle.bigappleui.viewpager.event;

import java.util.LinkedList;

/**
 * 滚动时，屏幕发生变化事件，有点像设计模式中的：订阅者模式或者观察者模式。这个是被观察者，事件改变时负责通知观察者。
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-12 下午8:17:10 $
 */
public class ScrollEventAdapter {
    LinkedList<OnScrollCompleteListener> onScrollCompleteListenerList;

    public ScrollEventAdapter() {
        onScrollCompleteListenerList = new LinkedList<OnScrollCompleteListener>();
    }

    /**
     * 通知所有事件
     * 
     * @param scrollEvent
     */
    public void notifyEvent(ScrollEvent scrollEvent) {
        for (OnScrollCompleteListener onScrollCompleteListener : onScrollCompleteListenerList) {
            onScrollCompleteListener.onScrollComplete(scrollEvent);
        }
    }

    /**
     * 添加事件到事件列表
     * 
     * @param onScrollCompleteListener
     */
    public void addListener(OnScrollCompleteListener onScrollCompleteListener) {
        onScrollCompleteListenerList.add(onScrollCompleteListener);
    }

}
