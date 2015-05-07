package com.dazzle.bigappleui.viewpager.event;

/**
 * 滚动事件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-12 下午8:22:25 $
 */
@Deprecated
public class ScrollEvent {
    public int curScreen;

    public ScrollEvent(int curScreen) {
        this.curScreen = curScreen;
    }

}
