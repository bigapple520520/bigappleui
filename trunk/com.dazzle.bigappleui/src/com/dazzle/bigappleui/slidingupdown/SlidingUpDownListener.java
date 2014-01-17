/* 
 * @(#)SlidingUpDownListener.java    Created on 2014-1-17
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.slidingupdown;

/**
 * 滑动的监听，0表示above滑动到上面，1表示above正好在中间，2表示above正好在下面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-17 下午7:21:49 $
 */
public interface SlidingUpDownListener {
    public void whichScreen(int which);
}
