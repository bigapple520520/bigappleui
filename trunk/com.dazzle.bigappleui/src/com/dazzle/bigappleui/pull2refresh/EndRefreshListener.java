/* 
 * @(#)EndRefreshListener.java    Created on 2013-9-26
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.dazzle.bigappleui.pull2refresh;

/**
 * 下拉到尾部，进行数据刷新，在线程中执行的
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-9-26 下午3:00:01 $
 */
public interface EndRefreshListener {

    /**
     * 加载数据
     */
    public void endRefresh();
}
